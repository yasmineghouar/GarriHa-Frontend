package com.example.login



import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.ReservationDetails
import com.example.login.ui.theme.LoginTheme
import com.example.login.ui.theme.Pink80
import com.example.parkir.views.router.Router
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsListView(navController: NavHostController, firebaseAuth: FirebaseAuth, googleSignInClient: GoogleSignInClient) {
    LoginTheme {
        val context = LocalContext.current

        var response by remember { mutableStateOf<Response<List<ReservationDetails>>?>(null) }
        var reservations by remember { mutableStateOf<List<ReservationDetails>>(emptyList()) }
        var reservationsLocales by remember { mutableStateOf<List<Reservation>>(emptyList()) }
        var offlineMode by remember { mutableStateOf(false) } // Ajout d'une variable pour le mode hors connexion
        var searchQuery by remember { mutableStateOf("") }
        var filteredReservations by remember { mutableStateOf<List<ReservationDetails>>(emptyList()) }

        // Fetch the list of reservations when the composable is first launched
        LaunchedEffect(Unit) {
            val isConnectedInternet = isInternetAvailable(context)
            val reservationsResponse = try {
                //recuperer les reservations du USER
                Endpoint.createEndpoint().getAllUserReservations(1)
            } catch (e: Exception) {
                Log.e("ReservationsListView", "Error fetching reservations", e)
                null
            }
            Log.d("cnx","$isConnectedInternet")

            if (isConnectedInternet == false)  { //si pas d cnx recuperer les reservation from SERVEUR
                Log.d("cnx","$isConnectedInternet")
                //si pas d cnx recuperer les reservation from SERVEUR
                withContext(Dispatchers.IO) {
                    val db = UserDatabase.getDatabase(context)
                    reservationsLocales = db.getReservationDao().getAllReservations()
                }

                offlineMode = true
                Log.d("OFFLINE ", "OOFFLINE")
                ///PAS DE CONNEXION RECUPERER FROM LA BDDDDDD
            }else {
                if (reservationsResponse != null) {
                    //offlineMode = false
                    //si ya une reponse from le serveur RECUPERER FROM LE SERVEUUR
                    response = reservationsResponse
                    Log.d("reservationsResponse : ", "${reservationsResponse.body()}")
                    Log.d("reservationsList : ", "${response?.body()}")
                    //recuperer du backend les reservations
                    reservations = reservationsResponse.body() ?: emptyList()

                }
            }

        }

        val backgroundColor = Color(0xFF1D4460)
        Scaffold(
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(backgroundColor)
                ) {

                    TopAppBar(
                        title = {
                            Text(
                                text = "Liste des réservations",
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp,
                                color = Color(0xFFE0FDFF),
                                fontFamily = FontFamily.Serif,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
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
                }
            },
            bottomBar = { BottomMenu(navController = navController) }, // Ajout du BottomMenu ici
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
//                    .padding(16.dp)
                    .background(backgroundColor)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (offlineMode) {
                        items(reservationsLocales) { reservation ->
                            ReservationLocaleListItem(reservation = reservation, navController)
                        }
                    } else {
                        items(reservations) { reservation ->
                            ReservationListItem(reservation = reservation, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationLocaleListItem(reservation: Reservation, navController: NavHostController) {
    Surface(


        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Naviguer vers une autre page lors du clic sur l'élément de la liste
                navController.navigate("/ReservationDetails/${reservation.reservationId}")
            }
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),

        color = Pink80,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Numéro de reservation: ${reservation.reservationId}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )



            // Date reservation CONVERTIE EN DATE SANS TIME
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = LocalDateTime.parse(reservation.DateReservation, formatter)

            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = dateTime.format(dateFormatter)


            Text(

                text = "Date Réservation: ${formattedDate} ",
                style = MaterialTheme.typography.bodyLarge
            )
            // Heures de début et de fin de la réservation
            Text(
                text = "A partir de: ${reservation.HeureDebut} ,Jusqu'à: ${reservation.HeureFin}  ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Numero de place: ${reservation.numero} ",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
@Composable

fun ReservationListItem(reservation: ReservationDetails,navController: NavHostController) {
    // Affichage d'une carte pour chaque élément de réservation
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Naviguer vers une autre page lors du clic sur l'élément de la liste
                navController.navigate("/ReservationDetails/${reservation.reservationId}")
            }
            .shadow(4.dp, shape = MaterialTheme.shapes.medium),

        color = Pink80,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Numéro de réservation: ${reservation.reservationId}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = LocalDateTime.parse(reservation.DateReservation, formatter)

            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val formattedDate = dateTime.format(dateFormatter)


            Text(

                text = "Date Réservation: ${formattedDate} ",
                style = MaterialTheme.typography.bodyLarge
            )
            // Heures de début et de fin de la réservation
            Text(
                text = "A partir de: ${reservation.HeureDebut} ,Jusqu'à: ${reservation.HeureFin}  ",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Numero de place: ${reservation.numero} ",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}