package com.example.login

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.Parkingg
import com.example.login.retrofit.ReservationDetails
import com.example.login.ui.theme.PurpleGrey80
import com.example.parkir.views.router.Router
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.lightspark.composeqr.DotShape
import com.lightspark.composeqr.QrCodeColors
import com.lightspark.composeqr.QrCodeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.EnumMap

fun generateQrCodeBitmap(qrCodeContent: String, size: Int = 16): Bitmap? {
    return try {
        val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java).also {
            it[EncodeHintType.MARGIN] = 1
            it[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
        }
        val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bits[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    } catch (e: Exception) {
        Log.e("generateQrCodeBitmap", "Error generating QR code bitmap: ${e.message}", e)
        null
    }
}


@Composable
fun ReservationDetailsScreen(navController: NavHostController) {
    val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntry
    val idReservation: String? = navBackStackEntry?.arguments?.getString("idReservation")
    val context = LocalContext.current
    var response by remember { mutableStateOf<Response<ReservationDetails>?>(null) }
    var reponseP by remember { mutableStateOf<Response<Parkingg>?>(null) }
    var reservation by remember { mutableStateOf<ReservationDetails?>(null) }
    var parking by remember { mutableStateOf<Parkingg?>(null) }
    var parkingg by remember { mutableStateOf<Parking?>(null) }
    var qrCodeBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    // Mutable state variable to store parking name
    var parkingName by remember { mutableStateOf<String?>(null) }
    var CodeRESE    by remember { mutableStateOf<Int?>(null) }
    var DateRESE by remember { mutableStateOf<String?>(null) }
    var EntRESE by remember { mutableStateOf<String?>(null) }
    var SrtRESE by remember { mutableStateOf<String?>(null) }
    var QrRESE by remember { mutableStateOf<String?>(null) }
    var placeRESE by remember { mutableStateOf<Int?>(null) }
    var parkingId by remember { mutableStateOf<Int?>(null) }
    var prixTotal by remember { mutableStateOf<Double?>(null) }
    var ReservationDetailsLocales by remember { mutableStateOf<Reservation?>(null) }
    var offlineMode by remember { mutableStateOf(false) }
    LaunchedEffect(idReservation) {
        val isConnectedInternet = isInternetAvailable(context)
        idReservation?.let {

            val reservationId = it.toInt()
            val reservationsResponse = try {
                Endpoint.createEndpoint().getReservationDetails(reservationId)
            } catch (e: Exception) {
                Log.e("ReservationDetailsScreen", "Error fetching reservation details", e)
                null
            }


            if (isConnectedInternet == false) { //si pas d cnx recuperer les reservation from SERVEUR
                Log.d("cnx", "$isConnectedInternet")
                offlineMode=true
                //si pas d cnx recuperer les reservation from SERVEUR
                withContext(Dispatchers.IO) {
                    val db = UserDatabase.getDatabase(context)
                    ReservationDetailsLocales =
                        db.getReservationDao().getReservationById(reservationId)
                    //recuperer details parking from local
                    CodeRESE= ReservationDetailsLocales?.reservationId
                    DateRESE= ReservationDetailsLocales?.DateReservation
                    EntRESE = ReservationDetailsLocales?.HeureDebut
                    SrtRESE = ReservationDetailsLocales?.HeureFin
                    QrRESE = ReservationDetailsLocales?.QRCode
                    placeRESE = ReservationDetailsLocales?.numero



                    parkingName= ReservationDetailsLocales?.let { it1 ->
                        db.getParkingDao().getParkingNameById(
                            it1.idparking)
                    }
                }

            } else {
                response = reservationsResponse
                reservation = response?.body()

                reservation?.idparking?.let { idparking ->
                    Log.d("Parking de réservation", "Id parking: $idparking")
                    val parkingResponse = try {
                        Endpoint.createEndpoint().getParkingById(idparking)
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
                    }

                }


                reservation?.QRCode?.let { qrCode ->
                    Log.d("ReservationDetailsScreen", "QRCode Content: $qrCode")
                    generateQrCodeBitmap(qrCode, 1024)?.let { bitmap ->
                        qrCodeBitmap = bitmap.asImageBitmap()
                    } ?: Log.e("ReservationDetailsScreen", "Failed to generate QR code bitmap")
                }
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
            text = "Confirmation Reservation",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = textColorSecondary,
            modifier = Modifier.padding(top = 24.dp, bottom = 24.dp, start = 8.dp, end = 8.dp)
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = backgroundColor,
            shadowElevation = 8.dp, // Ajoute une ombre avec une élévation de 8 dp
            modifier = Modifier.padding(4.dp)

        ) {
            Column(
                modifier = Modifier.padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                // Display parking name if available

                reservation?.let {


                    // Date reservation CONVERTIE EN DATE SANS TIME
                    val formatter = DateTimeFormatter.ISO_DATE_TIME


                    val dateTime = LocalDateTime.parse(it.DateReservation, formatter)

                    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val formattedDate = dateTime.format(dateFormatter)


                    reservation?.reservationId?.toString()?.let { it1 ->
                        DetailRow(
                            label = "Code réservation :",
                            value = it1,
                            labelColor = textColorQuaternary,
                            valueColor = textColorTertiary
                        )
                    }

                    DetailRow(
                        label = "Date réservation :",
                        value = formattedDate ,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                    DetailRow(
                        label = "Heure Entrée :",
                        value = it.HeureDebut ,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                    DetailRow(
                        label = "Heure Sortie :",
                        value = it.HeureFin ,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                    DetailRow(
                        label = "Numéro de place :",
                        value = it.numero.toString(),
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                    QrCodeView(
                        data = it.reservationId.toString() ,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        colors = QrCodeColors(
                            background = Color.White,
                            foreground = Color.Black
                        ),
                        dotShape = DotShape.Square,
                    )

                    /***********************************/

                    /***********************************/


                }

                CodeRESE?.let{
                    DetailRow(
                        label = "Code Réservation :",
                        value = it.toString(),
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                    QrCodeView(
                        data = it.toString() ,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(16.dp),
                        colors = QrCodeColors(
                            background = Color.White,
                            foreground = Color.Black
                        ),
                        dotShape = DotShape.Square,
                    )
                }
                DateRESE?.let{
                    val formatter2 = DateTimeFormatter.ISO_DATE_TIME


                    val dateTime2 = LocalDateTime.parse(it
                        ?: "", formatter2)
                    val dateFormatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                    val formattedDate = dateTime2.format(dateFormatter2)


                    DetailRow(
                        label = "Date Réservation :",
                        value = formattedDate,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                }
                EntRESE?.let{
                    DetailRow(
                        label = "Heure entrée  :",
                        value = it,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                }
                SrtRESE?.let{
                    DetailRow(
                        label = "Heure sortie  :",
                        value = it,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                }

                placeRESE ?.let{
                    DetailRow(
                        label = "Numero de place :",
                        value = it.toString(),
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )
                }
                parkingName?.let {
                    DetailRow(
                        label = "Nom du parking :",
                        value = it,
                        labelColor = textColorQuaternary,
                        valueColor = textColorTertiary
                    )


                }
                Button(onClick = {

                    navController.navigate(Router.ReservationsList.route)

                }) {
                    Text(text = "Voir tout", color= textColorQuaternary)
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

@Composable
fun DetailRow(label: String, value: String, labelColor: Color, valueColor: Color) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp),
            color = labelColor,
            modifier = Modifier.widthIn(max = 250.dp) // Limite la largeur de l'étiquette
        )
        Spacer(modifier = Modifier.width(12.dp)) // Ajoute un espacement entre l'étiquette et la valeur
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp),
            color = valueColor
        )
    }
}



/****************************************************************************************/

