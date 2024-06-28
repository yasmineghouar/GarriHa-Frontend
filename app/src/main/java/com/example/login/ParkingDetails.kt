package com.example.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.Parkingg
import com.example.login.ui.theme.Pink80
import com.example.login.ui.theme.PurpleGrey80
import retrofit2.Response



@Composable
fun ParkingDetailsScreen(navController: NavHostController) {
    val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry
    val parkingId: String? = navBackStackEntry?.arguments?.getString("parkingId")
    val context = LocalContext.current
    var reponseP by remember { mutableStateOf<Response<Parkingg>?>(null) }
    var parking by remember { mutableStateOf<Parkingg?>(null) }
    var parkingName by remember { mutableStateOf<String?>(null) }
    var parkingIdd by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(parkingId) {
        parkingId?.let {
            val idParking = it.toInt()

            val parkingResponse = try {
                Endpoint.createEndpoint().getParkingById(idParking)
            } catch (e: Exception) {
                Log.e("Parking de réservation", "Error fetching reservation parking", e)
                null
            }
            reponseP = parkingResponse //recuperer reponse de la requete get ParkingId
            parking = reponseP?.body()
            Log.d(
                "Reponse Parking",
                "${reponseP?.body()}"
            )//afficher la reponse requete get parking by ID
            // Store the parking name
            parking?.let {
                parkingName = it.parkingName
                parkingIdd = it.parkingId
            }

        }




    }


    val backgroundColor = Color(0xFF1D4460)
    val textColorPrimary = Color.White
    val textColorSecondary = Color(0xFF1D4460)
    val textColorTertiary = Color(0xFFE0FDFF)
    val textColorQuaternary = Color(0xFF7AC2D5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PurpleGrey80),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Titre de la page
        Text(
            text = "Details Parking",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = textColorSecondary,
            modifier = Modifier.padding(top=24.dp, bottom=24.dp,start=8.dp, end=8.dp)
        )

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            shadowElevation = 8.dp, // Ajoute une ombre avec une élévation de 8 dp
            modifier = Modifier.padding(4.dp)

        ) {
            LazyColumn(
                modifier = Modifier.padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                // Display parking name if available


                parking?.let {
                    item {
                        // Récupérer et afficher l'image du parking
                        val drawableResId = getDrawableResIdForParking(parking!!, context)
                        val painter = painterResource(id = drawableResId)
                        Image(

                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .width(300.dp)

                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                    item {
                        DetailRow(
                            label = "Nom du parking :",
                            value = it.parkingName,
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )
                    }
                    item {
                        DetailRow(
                            label = "Wilaya :",
                            value = it.WilayaParking.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )
                    }
                    item {
                        DetailRow(
                            label = "Commune :",
                            value = it.CommuneParking.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )
                    }
                    item {
                        DetailRow(
                            label = "Adresse :",
                            value = it.AdresseParking.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )}
                    item {
                        DetailRow(
                            label = "Description :",
                            value = it.DescriptionParking.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )}
                    item {
                        DetailRow(
                            label = "Surface :",
                            value = it.SurfaceParking.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )}
                    item {
                        DetailRow(
                            label = "Nombre de places :",
                            value = it.nombreDePlaces.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )}
                    item {
                        DetailRow(
                            label = "Prix par heure (DZ)  :",
                            value = it.PrixParHeure.toString(),
                            labelColor = textColorTertiary,
                            valueColor = textColorQuaternary
                        )
                    }
                    item{
                        Button(onClick = {
                            //envoyer id parking dans url create reservation
                            navController.navigate("/createReservation2/${it.parkingId}")

                        },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Pink80, // Couleur de fond du bouton (Pink80)
                                contentColor = Color.White // Couleur du texte du bouton
                            ),
                            modifier = Modifier
                                .padding(24.dp)

                        ) {
                            Text(text = "Réserver une place",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = Color.White
                                )
                            )

                        }
                    }


                }
            }
        }

    } ?: run {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(color = backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Détails de la réservation en cours de chargement ...",
                style = MaterialTheme.typography.bodyLarge,
                color = textColorPrimary,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(16.dp)
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