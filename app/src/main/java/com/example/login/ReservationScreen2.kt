package com.example.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.Parkingg
import com.example.login.retrofit.ReservationData
import com.example.login.retrofit.ReservationDetails
import com.example.login.ui.theme.Pink80
import com.example.login.ui.theme.PurpleGrey80
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationCreateView2(navController: NavHostController) {
    val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry
    val parkingId: String? = navBackStackEntry?.arguments?.getString("parkingId")
    val context = LocalContext.current
    var DescriptionReservation by remember { mutableStateOf("") }
    var iduser by remember { mutableStateOf("") }
    var reservationId by remember { mutableStateOf("") }
    var numeroReservation by remember {mutableStateOf("")}
    var QRCodeReservation by remember {mutableStateOf("")}
    var DateReservationn by remember {mutableStateOf("")}
    var iduserReservation by remember {mutableStateOf("")}
    var idparkingReservation by remember {mutableStateOf("")}
    var HeureDebutReservation by remember {mutableStateOf("")}
    var HeureFinReservation by remember {mutableStateOf("")}

    var idparking by remember { mutableStateOf("") }
    var dateDebut by remember { mutableStateOf(LocalDateTime.now()) }
    var dateFin by remember { mutableStateOf(LocalDateTime.now()) }
    var heureDebut by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var heureFin by remember { mutableStateOf<Pair<Int, Int>?>(null) }
    var response by remember { mutableStateOf<Response<ReservationDetails>?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showdialogMessage by remember { mutableStateOf("") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var reponseP by remember { mutableStateOf<Response<Parkingg>?>(null) }
    var parking by remember { mutableStateOf<Parkingg?>(null) }
    var parkingPrix by remember { mutableStateOf<Int?>(null) }
    var allParkings by remember { mutableStateOf<List<Parking>>(emptyList()) }
    val calendarState = rememberSheetState()
    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        ),
        selection = CalendarSelection.Date { dateDebut ->
            Log.d("SelectedDates", "Start: $dateDebut")
        }
    )
    val clockState = rememberSheetState()
    val clockState2 = rememberSheetState()
    ClockDialog(state = clockState, selection = ClockSelection.HoursMinutes { hours, minutes ->
        heureDebut = Pair(hours, minutes)
        Log.d("SelectedHourEntree", "$hours:$minutes")
    })
    ClockDialog(state = clockState2, selection = ClockSelection.HoursMinutes { hours2, minutes2 ->
        heureFin = Pair(hours2, minutes2)
        Log.d("SelectedHourSortie", "$hours2:$minutes2")
    })
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
                parkingPrix = it.PrixParHeure

            }

        }
    }
    val backgroundColor = Color(0xFF1D4460)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = PurpleGrey80,
            shadowElevation = 8.dp, // Ajoute une ombre avec une élévation de 8 dp
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Réserve une place ! ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Pink80,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 20.dp)
                )
                Text(
                    text = "Choisis le jour de la réservation : ",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .clickable { calendarState.show() }
                        .padding(8.dp)
                )

                Text(
                    text = "Choisis une heure d'entrée :",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Select Hour",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .clickable { clockState.show() }
                        .padding(8.dp)
                )

                Text(
                    text = "Choisis une heure de sortie :",
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Select Hour",
                    tint = Color.DarkGray,
                    modifier = Modifier
                        .clickable { clockState2.show() }
                        .padding(8.dp)
                )

                if (heureDebut != null && heureFin != null) {
                    val debutCalendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, heureDebut!!.first)
                        set(Calendar.MINUTE, heureDebut!!.second)
                    }

                    val finCalendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, heureFin!!.first)
                        set(Calendar.MINUTE, heureFin!!.second)
                    }

                    val durationInMillis = (finCalendar.timeInMillis - debutCalendar.timeInMillis)* (parkingPrix!!)
                    val hours = durationInMillis / (1000 * 60 * 60)
                    val minutes = (durationInMillis / (1000 * 60)) % 60

                    val total = String.format("%02d.%02d", hours, minutes)

                    Text(
                        text = "Prix Total (DZ) : $total",
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Button(
                    onClick = {

                        if (validateFields(dateDebut, dateFin, heureDebut, heureFin)) {

                            val formattedHeureDebut = heureDebut?.let { String.format("%02d:%02d:00", it.first, it.second) }
                            val formattedHeureFin = heureFin?.let { String.format("%02d:%02d:00", it.first, it.second) }

                            val reservationData = ReservationData(
                                DateReservation = dateDebut.toString(),
                                iduser = 1,
                                //idparking = parkingId?.toInt() ?: 1,
                                idparking = parkingId?.toInt() ?: 1,
                                HeureDebut = formattedHeureDebut.toString(),
                                HeureFin = formattedHeureFin.toString()
                            )



                            CoroutineScope(Dispatchers.IO).launch {
                                // val reservationDao = UserDatabase.getDatabase(context).getReservationDao()
                                // reservationDao.AddReservations(reservationLocale)
                                val createReservationResponse = try {
                                    Endpoint.createEndpoint().createReservation(reservationData)
                                } catch (e: Exception) {
                                    null
                                }

                                // offlineMode = true


                                response = createReservationResponse

                                Log.d("Reservation created!", "${response?.body()}")
                                reservationId = response?.body()?.reservationId.toString()
                                numeroReservation = response?.body()?.numero.toString()
                                QRCodeReservation = response?.body()?.QRCode.toString()
                                DateReservationn = response?.body()?.DateReservation.toString()
                                iduserReservation = response?.body()?.iduser.toString()
                                idparkingReservation = response?.body()?.idparking.toString()
                                HeureDebutReservation = response?.body()?.HeureDebut.toString()
                                HeureFinReservation = response?.body()?.HeureFin.toString()

                                var reservationLocale = Reservation(
                                    reservationId = reservationId.toInt(),
                                    numero = numeroReservation.toInt(),
                                    QRCode = QRCodeReservation,
                                    DateReservation = DateReservationn,
                                    iduser = iduserReservation.toInt(),
                                    //idparking = parkingId?.toInt() ?: 1,
                                    idparking = idparkingReservation.toInt(),
                                    HeureDebut = HeureDebutReservation,
                                    HeureFin = HeureFinReservation

                                )

                                Log.d("Reservation created!", "$reservationId")


                                try {
                                    val db = UserDatabase.getDatabase(context)
                                    val userExists = db.getUserDao().getUserById(reservationLocale.iduser) != null
                                    val parkingExists = db.getParkingDao().getParkingById(reservationLocale.idparking) != null
                                    // Si l'utilisateur n'existe pas, le créer et mettre à jour la variable userExists
                                    if (!userExists) {
                                        // Créez un utilisateur
                                        val userr = User(
                                            firstName = "Yass",
                                            lastName = "Doe",
                                            email = "john.doe@example.com",
                                            motDePasse = "motDePasse123"
                                        )
                                        // Insérer l'utilisateur nouvellement créé dans la base de données
                                        db.getUserDao().addUser(userr)
                                        // Mettre à jour l'ID de l'utilisateur dans l'objet reservationLocale
                                        //reservationLocale.iduser = userId.toInt()
                                    }

                                    // Si le parking n'existe pas, le créer et mettre à jour la variable parkingExists
                                    /*if (!parkingExists) {
                                    // db.getParkingDao().deleteAllParkings()
                                    allParkings = db.getParkingDao().getAllParkings()
                                    Log.d("AllParkings","$allParkings")

                                    val parkingList = listOf(
                                    Parking(
                                    parkingId = 1,
                                    parkingName = "Parking Tipaza",
                                    CommuneParking = "Tipaza",
                                    WilayaParking = "Tipaza",
                                    AdresseParking = "Adresse de Tipaza",
                                    photoParking = "photoparking",
                                    nombreDePlaces = 100,
                                    PrixParHeure = 200,
                                    DescriptionParking = "Parking spacieux situé près de la plage de Tipaza, avec une capacité de 100 véhicules.",
                                    SurfaceParking = "500m²",
                                    Latitude = 36.58975000,
                                    Longitude = 2.44744000
                                    ),
                                    Parking(
                                    parkingId= 2,
                                    parkingName = "Parking Alger",
                                    CommuneParking = "Alger",
                                    WilayaParking = "Alger",
                                    AdresseParking = "Adresse de Oued Semar",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 150,
                                    PrixParHeure = 300,
                                    DescriptionParking = "Parking sécurisé au centre d Alger, près de la place du 1er Mai.",
                                    SurfaceParking = "600m²",
                                    Latitude = 36.73225000,
                                    Longitude = 3.08746000
                                    ),
                                    Parking(
                                    parkingId = 3,
                                    parkingName = "Parking Tizi",
                                    CommuneParking = "Tizi Ouzou",
                                    WilayaParking = "Tizi Ouzou",
                                    AdresseParking = "Adresse de Tedmait",
                                    photoParking = "photoparking",
                                    nombreDePlaces = 200,
                                    PrixParHeure = 250,
                                    DescriptionParking = "Parking moderne dans la zone industrielle de Tedmait, équipé de caméras de surveillance.",
                                    SurfaceParking = "700m²",
                                    Latitude = 36.71182000,
                                    Longitude = 4.04591000
                                    ),
                                    Parking(
                                    parkingId = 4,
                                    parkingName = "Parking Bejaia",
                                    CommuneParking = "Bejaia",
                                    WilayaParking = "Bejaia",
                                    AdresseParking = "Adresse de Akfadou",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 120,
                                    PrixParHeure = 150,
                                    DescriptionParking = "Parking bien entretenu à Bejaia, à proximité des zones commerciales et résidentielles.",
                                    SurfaceParking = "450m²",
                                    Latitude = 36.75250000,
                                    Longitude = 5.05670000
                                    ),
                                    Parking(
                                    parkingId = 5,
                                    parkingName = "Parking Ain Taya",
                                    CommuneParking = "Ain Taya",
                                    WilayaParking = "Alger",
                                    AdresseParking = "Adresse de Ain Taya",
                                    photoParking = "photoparking",
                                    nombreDePlaces = 80,
                                    PrixParHeure = 100,
                                    DescriptionParking = "Parking familial à Ain Taya, idéal pour les sorties en famille ou entre amis.",
                                    SurfaceParking = "300m²",
                                    Latitude = 36.79310000,
                                    Longitude = 3.28670000
                                    ),
                                    Parking(
                                    parkingId = 6,
                                    parkingName = "Parking Reghaia",
                                    CommuneParking = "Reghaia",
                                    WilayaParking = "Alger",
                                    AdresseParking = "Adresse de Reghaia",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 90,
                                    PrixParHeure = 120,
                                    DescriptionParking = "Parking couvert situé dans le centre de Reghaia, offrant une protection contre les intempéries.",
                                    SurfaceParking = "350m²",
                                    Latitude = 36.73510000,
                                    Longitude = 3.34020000
                                    ),
                                    Parking(
                                    parkingId = 7,
                                    parkingName = "Parking Blida",
                                    CommuneParking = "Blida",
                                    WilayaParking = "Blida",
                                    AdresseParking = "Adresse de Blida",
                                    photoParking = "photoparking.png",
                                    nombreDePlaces = 180,
                                    PrixParHeure = 220,
                                    DescriptionParking = "Grand parking à Blida, avec un accès facile depuis l'autoroute.",
                                    SurfaceParking = "650m²",
                                    Latitude = 36.47010000,
                                    Longitude = 2.82890000
                                    ),
                                    Parking(
                                    parkingId = 8,
                                    parkingName = "Parking Oran",
                                    CommuneParking = "Oran",
                                    WilayaParking = "Oran",
                                    AdresseParking = "Adresse de Oran",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 250,
                                    PrixParHeure = 300,
                                    DescriptionParking = "Parking central à Oran, à quelques pas des principaux sites touristiques.",
                                    SurfaceParking = "900m²",
                                    Latitude = 35.69710000,
                                    Longitude = -0.63080000
                                    ),
                                    Parking(
                                    parkingId = 9,
                                    parkingName = "Parking Constantine",
                                    CommuneParking = "Constantine",
                                    WilayaParking = "Constantine",
                                    AdresseParking = "Adresse de Constantine",
                                    photoParking = "photoconstantine.jpg",
                                    nombreDePlaces = 130,
                                    PrixParHeure = 170,
                                    DescriptionParking = "Parking sécurisé près du centre-ville de Constantine, avec des tarifs abordables.",
                                    SurfaceParking = "500m²",
                                    Latitude = 36.36500000,
                                    Longitude = 6.61470000
                                    ),
                                    Parking(
                                    parkingId = 10,
                                    parkingName = "Parking Tlemcen",
                                    CommuneParking = "Tlemcen",
                                    WilayaParking = "Tlemcen",
                                    AdresseParking = "Adresse de Tlemcen",
                                    photoParking = "photoconstantine.jpg",
                                    nombreDePlaces = 2,
                                    PrixParHeure = 140,
                                    DescriptionParking = "Parking bien éclairé à Tlemcen, ouvert 24h/24 pour répondre à vos besoins de stationnement.",
                                    SurfaceParking = "400m²",
                                    Latitude = 34.87830000,
                                    Longitude = -1.31800000
                                    ),
                                    Parking(
                                    parkingId = 11,
                                    parkingName = "parkingggTest",
                                    CommuneParking = "CommuneParking",
                                    WilayaParking = "WilayaParking",
                                    AdresseParking = "alger",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 500,
                                    PrixParHeure = 200,
                                    DescriptionParking = "testtesttt",
                                    SurfaceParking = "200 m2",
                                    Latitude = 36.73510000,
                                    Longitude = 2.44744000
                                    ),
                                    Parking(
                                    parkingId = 12,
                                    parkingName = "parkingggfTest",
                                    CommuneParking = "CommuneParking",
                                    WilayaParking = "WilayaParking",
                                    AdresseParking = "alger",
                                    photoParking = "photoParking",
                                    nombreDePlaces = 500,
                                    PrixParHeure = 200,
                                    DescriptionParking = "testtesttt",
                                    SurfaceParking = "200 m2",
                                    Latitude = 36.58975000,
                                    Longitude = 2.44744000
                                    ),
                                    Parking(
                                    parkingId = 13,
                                    parkingName = "parkingName",
                                    CommuneParking = "CommuneParking",
                                    WilayaParking = "WilayaParking",
                                    AdresseParking = "alger",
                                    photoParking = "photoconstantine",
                                    nombreDePlaces = 500,
                                    PrixParHeure = 200,
                                    DescriptionParking = "testtesttt",
                                    SurfaceParking = "200 m2",
                                    Latitude = 36.58975000,
                                    Longitude = 2.44744000
                                    )
                                    )
                                    parkingList.forEach { parking ->
                                    db.getParkingDao().addParking(parking)
                                    }
                                    allParkings = db.getParkingDao().getAllParkings()
                                    Log.d("AllParkings","$allParkings")

                                    }
                                    */

                                    db.getReservationDao().AddReservations(reservationLocale)




                                    //db.getReservationDao().AddReservations(reservationLocale)
                                    // Afficher la réservation créée dans la console
                                    Log.d("ReservationCreated", "Reservation créée : $reservationLocale")

                                    withContext(Dispatchers.Main) {
                                        showdialogMessage = "Reservation created with success"
                                        reservationId.let { id ->
                                            navController.navigate("/ReservationDetails/$id")
                                        }
                                        //showdialogMessage = "Reservation created locally with success"
                                        //showDialog = true
                                    }



                                } catch (e: Exception) {
                                    Log.e("ReservationCreation", "Error creating reservation: ${e.message}", e)
                                    // Gérer l'erreur ici
                                }

                            }
                        } else {
                            showdialogMessage = "Please fill all fields"
                            showDialog = true
                        }
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Valider ",
                        fontSize = 20.sp,
                        color = Color.White,

                        )
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "Reservation Status") },
                text = { Text(text = showdialogMessage) },
                confirmButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}




private fun validateFields(
    dateDebut: LocalDateTime?,
    dateFin: LocalDateTime?,
    heureDebut: Pair<Int, Int>?,
    heureFin: Pair<Int, Int>?
): Boolean {
    return dateDebut != null && dateFin != null && heureDebut != null && heureFin != null
}
