package com.example.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.parkir.views.router.Router
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


@Composable
fun LoggedInHomeView(navController: NavHostController, firebaseAuth: FirebaseAuth, googleSignInClient: GoogleSignInClient) {
    MaterialTheme {
        val backgroundColor = Brush.verticalGradient(
            colors = listOf(Color(0xFF7AC2D5), Color(0xFF1D4460))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App logo or name
            val logo: Painter = painterResource(id = R.drawable.ic_app_log)
            Image(
                painter = logo,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Trouvez votre place de parking en toute simplicité avec GarriHa,\nvoici nos services : ",
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = PurpleGrey80,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    //.shadow(2.dp, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            )

            // Button to navigate to "Mes réservations"
            Button(
                onClick = {
                    navController.navigate(Router.ReservationsList.route)
                },
                //  colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF009688)),
                shape = RoundedCornerShape(12.dp),

                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                )
            ) {
                Text(
                    text = "Mes réservations",
                    fontFamily = FontFamily.Serif,
                    color = PurpleGrey80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Button to navigate to "Les Parkings"
            Button(
                onClick = {
                    navController.navigate(Router.ParkingScreen.route)
                },

                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                )
            ) {
                Text(
                    text = "Voir tous les Parkings",
                    fontFamily = FontFamily.Serif,
                    color = PurpleGrey80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Button to navigate to "Consulter la carte"
            Button(
                onClick = {
                      navController.navigate(Router.MapScreen.route)
                },
                //colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF004D40)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .width(300.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                )

            ) {
                Text(
                    text = "Consulter la carte",
                    fontFamily = FontFamily.Serif,
                    color = PurpleGrey80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            TextButton(
                onClick = {
                    logoutGoogle(navController, firebaseAuth, googleSignInClient){
                        navController.navigate(Router.Home.route)
                    }
                }
            ) {
                Text("Logout", color = Color.White)
            }

        }
    }
}