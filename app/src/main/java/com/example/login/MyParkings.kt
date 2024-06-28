package com.example.login

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.StringRes
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.Parkingg
import com.example.parkir.views.router.Router
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ParkingView(navController: NavHostController , firebaseAuth: FirebaseAuth , googleSignInClient: GoogleSignInClient) {
    val context = LocalContext.current
    var response by remember { mutableStateOf<Response<List<Parkingg>>?>(null) }
    var parkings by remember { mutableStateOf<List<Parkingg>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var filteredParkings by remember { mutableStateOf<List<Parkingg>>(emptyList()) }

    LaunchedEffect(Unit) {
        var parkingsResponse = try {
            Endpoint.createEndpoint().getAllParkings()
        } catch (e: Exception) {
            Log.e("ParkingView", "Error fetching parkings", e)
            null
        }

        response = parkingsResponse
        parkings = response?.body() ?: emptyList()
        filteredParkings = parkings
    }
    Scaffold(
        topBar = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1D4460)) // Appliquer la couleur de fond à toute la colonne
            ){
                TopAppBar(
                    title = {
                        Text(
                            text = "Liste des parkings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = Color(0xFFE0FDFF),
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color(0xFF1D4460)
                    ),
                    actions = {
                        TextButton(
                            onClick = {
                                logoutGoogle(navController, firebaseAuth, googleSignInClient){
                                    navController.navigate(Router.Home.route)
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
                    },
                    placeholder = "Cherchez un parking",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
            }
        },
        bottomBar = { BottomMenu(navController = navController) },
//        modifier = Modifier.fillMaxSize(), // Remplir toute la taille de l'écran
        modifier = Modifier.fillMaxSize()
//        backgroundColor = Color(0xFF1D4460) // Couleur de fond pour toute la page
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(16.dp)
//                .padding(start = 16.dp, end = 16.dp)
                .background(Color(0xFF1D4460)), // Couleur de fond de toute la page
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(160.dp))
            if (parkings.isEmpty()) {
                Text(text = "La liste des parkings est vide. Il n'y a actuellement aucune option de stationnement disponible.",
                    color = Color(0xFFE0FDFF),
                    textAlign = TextAlign.Center)

            } else {
                LazyColumn {
                    items(filteredParkings) { parking ->
                        ParkingListItem(parking = parking, context = context , navController = navController)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ParkingListItem(parking: Parkingg, context: Context ,navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(100.dp)
            .clickable {
                // Naviguer vers une Page details lors du clic sur l'élément de la liste
                // navController.navigate("/ParkingDetails/1")
                navController.navigate("/ParkingDetails/${parking.parkingId}")
            }
            .padding(8.dp)
    )  {
        // Récupérer et afficher l'image du parking
        val drawableResId = getDrawableResIdForParking(parking, context)
        val painter = painterResource(id = drawableResId)
        Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .aspectRatio(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = parking.parkingName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp),
                color = Color(0xFF7AC2D5),
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "${parking.AdresseParking ?: ""}, ${parking.CommuneParking ?: ""}, ${parking.WilayaParking ?: ""}",
//                modifier = Modifier.weight(1f),
                color = Color.White,
                fontFamily = FontFamily.Serif
            )
        }
    }
}


private fun getDrawableResIdForParking(parking: Parkingg, context: Context): Int {
    val photoName = parking.photoParking.lowercase().replace(" ", "_")
    Log.d("PhotoName", "Photo name to fetch: $photoName")
    val resourceId = context.resources.getIdentifier(photoName, "drawable", context.packageName)
    Log.d("ResourceID", "Resource ID: $resourceId")
    return if (resourceId != 0) resourceId else R.drawable.photoparking
}


@Composable
fun BottomMenu(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = PurpleGrey80,
        elevation = 10.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        BottomMenuScreen.values().forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomMenuScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    object Home : BottomMenuScreen("home", R.string.home, Icons.Filled.Home)
    object GoToMap : BottomMenuScreen("go_to_map", R.string.go_to_map, Icons.Filled.Map)
    object ReservationList : BottomMenuScreen("reservation_list", R.string.reservation_list, Icons.Filled.List)
    object ParkingList : BottomMenuScreen("parking_list", R.string.parking_list, Icons.Filled.LocalParking)

    companion object {
        fun values() = listOf(Home, GoToMap, ReservationList, ParkingList)
    }
}