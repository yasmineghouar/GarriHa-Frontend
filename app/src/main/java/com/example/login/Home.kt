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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

// Define your color palette
val Purple80 = Color(0xFF1D4460)
val PurpleGrey80 = Color(0xFFE0FDFF)
val Pink80 = Color(0xFF7AC2D5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeVieww(navController: NavHostController) {
    // Apply your theme
    MaterialTheme {
        // Define the background color
        val backgroundColor = PurpleGrey80

        // Column to arrange elements vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App logo
            val logo: Painter = painterResource(id = R.drawable.ic_app_log)
            Image(
                painter = logo,
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )

            // App name
            Text(
                text = "GarriHa !",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Purple80,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Welcome message
            Text(
                text = "Bienvenue dans votre application de stationnement .",
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                color = Purple80,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp, top=32.dp)
            )

            Text(
                text = "- Réservez votre place en un clic -",
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.Serif,
                color = Purple80,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp, top=24.dp)
            )


            // Button to navigate to the login screen
            Button(
                onClick = {
                    navController.navigate(Router.AuthScreen.route)
                },
                //colors = ButtonDefaults.buttonColors(color = Pink80),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(12.dp)
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Se connecter",
                    color = PurpleGrey80,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}