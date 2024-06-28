package com.example.login

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.Parkingg
import com.example.parkir.views.router.Router
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(navController: NavHostController, firebaseAuth: FirebaseAuth, googleSignInClient: GoogleSignInClient) {
        var response by remember { mutableStateOf<Response<List<Parkingg>>?>(null) }
        var parkings by remember { mutableStateOf<List<Parkingg>>(emptyList()) }
        val mapView = rememberMapViewWithLifecycle()
        var searchQuery by remember { mutableStateOf("") }
        var filteredParkings by remember { mutableStateOf<List<Parkingg>>(emptyList()) }

        LaunchedEffect(Unit) {
                val parkingsResponse = try {
                        Endpoint.createEndpoint().getAllParkings()
                } catch (e: Exception) {
                        Log.e("ParkingView", "Error fetching parkings", e)
                        null
                }
                response = parkingsResponse
                parkings = response?.body() ?: emptyList()
                filteredParkings = parkings
                Log.d("parkingsss : ", "$parkings")

                mapView.getMapAsync { googleMap ->
                        googleMap.uiSettings.isZoomControlsEnabled = true
                        updateMarkers(googleMap, filteredParkings)
                }
        }

        Scaffold(
                topBar = {
                        Column (
                                modifier = Modifier
                                        .background(Color(0xFF1D4460))  // Apply background color to the entire Column
                                        .fillMaxWidth()
                        ){
                                TopAppBar(
                                        title = {
                                                Text(
                                                        text = "Carte des parkings",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 30.sp,
                                                        color = Color(0xFFE0FDFF),
                                                        fontFamily = FontFamily.Serif
                                                )
                                        },
                                        colors = TopAppBarDefaults.smallTopAppBarColors(
                                                containerColor = Color(0xFF1D4460)
                                        ),
                                        actions = {
                                                TextButton(
                                                        onClick = {
                                                                logoutGoogle(navController, firebaseAuth, googleSignInClient){
                                                                        navController.navigate(
                                                                                Router.Home.route)
                                                                }
                                                        }
                                                ) {
                                                        Text("Logout", color = Color.White)
                                                }
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                SearchBar(
                                        searchQuery = searchQuery,
                                        onSearchQueryChange = { newQuery ->
                                                searchQuery = newQuery
                                                filteredParkings = if (newQuery.isEmpty()) {
                                                        parkings
                                                } else {
                                                        parkings.filter { it.parkingName.contains(newQuery, ignoreCase = true) }
                                                }
                                                mapView.getMapAsync { googleMap ->
                                                        updateMarkers(googleMap, filteredParkings)
                                                }
                                        },
                                        placeholder = "Cherchez un parking sur la carte",
                                        modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                        }

                },
                bottomBar = { BottomMenu(navController = navController) },
                modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp) // Adjust the height as needed

        )
        { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                        AndroidView({ mapView })
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
        searchQuery: String,
        onSearchQueryChange: (String) -> Unit,
        placeholder: String,
        modifier: Modifier = Modifier
) {
        OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                leadingIcon = {
                        Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFFE0FDFF)
                        )
                },
                placeholder = {
                        Text(
                                text = placeholder,
                                color = Color(0xFFE0FDFF)
                        )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color(0xFFE0FDFF),
                        focusedBorderColor = Color(0xFFE0FDFF),
                        cursorColor = Color(0xFFE0FDFF),
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE0FDFF), RoundedCornerShape(8.dp))
                        .background(Color(0xFF1D4460).copy(alpha = 0.5f)) // Background slightly transparent to match the nav bar
        )
}

fun updateMarkers(googleMap: com.google.android.gms.maps.GoogleMap, parkings: List<Parkingg>) {
        googleMap.clear()
        parkings.forEach { parking ->
                val location = LatLng(parking.Latitude, parking.Longitude)
                val snippetText = listOfNotNull(
                        parking.AdresseParking,
                        parking.CommuneParking,
                        parking.WilayaParking
                ).joinToString(", ")
                googleMap.addMarker(
                        MarkerOptions()
                                .position(location)
                                .title(parking.parkingName)
                                .snippet(snippetText)
                )
        }

        // Move the camera to the first parking
        if (parkings.isNotEmpty()) {
                val firstParkingLocation = LatLng(parkings[0].Latitude, parkings[0].Longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstParkingLocation, 12f))
        }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
        val context = LocalContext.current
        val mapView = remember { MapView(context) }

        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(lifecycle, mapView) {
                val lifecycleObserver = object : DefaultLifecycleObserver {
                        override fun onCreate(owner: LifecycleOwner) {
                                mapView.onCreate(Bundle())
                        }

                        override fun onStart(owner: LifecycleOwner) {
                                mapView.onStart()
                        }

                        override fun onResume(owner: LifecycleOwner) {
                                mapView.onResume()
                        }

                        override fun onPause(owner: LifecycleOwner) {
                                mapView.onPause()
                        }

                        override fun onStop(owner: LifecycleOwner) {
                                mapView.onStop()
                        }

                        override fun onDestroy(owner: LifecycleOwner) {
                                mapView.onDestroy()
                        }
                }

                lifecycle.addObserver(lifecycleObserver)
                onDispose {
                        lifecycle.removeObserver(lifecycleObserver)
                }
        }

        return mapView
}

