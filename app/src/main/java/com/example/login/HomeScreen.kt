package com.example.login


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.parkir.views.router.Router


@Composable
fun HomeView(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Home Page")
        Button(onClick = {
            if (!isConnected) {
                navController.navigate(Router.AuthScreen.route)
            } else {
                navController.navigate(Router.ReservationsScreen.route)
            }
        }) {
            Text(text = "Go To Reservations")
        }
        Button(onClick = {

                navController.navigate(Router.ParkingScreen.route)

        }) {
            Text(text = "Go To Parking List")
        }
        Button(onClick = {

            navController.navigate(Router.SignUpScreen.route)

        }) {
            Text(text = "Create an account")
        }
        Button(onClick = {

            navController.navigate(Router.MapScreen.route)

        }) {
            Text(text = "Go to the map")
        }

        Button(onClick = {

            navController.navigate(Router.DisplayStoredImage.route)

        }) {
            Text(text = "Go to Profil")
        }

        Button(onClick = {

            navController.navigate(Router.ReservationScreen.route)

        }) {
            Text(text = "Create a reservation")
        }

        Button(onClick = {

            navController.navigate(Router.ReservationScreen2.route)

        }) {
            Text(text = "Create a reservation 2 ")
        }
        Button(onClick = {

            navController.navigate(Router.ReservationsList.route)

        }) {
            Text(text = "Liste Reservations")
        }

        Button(onClick = {

            navController.navigate(Router.Home.route)

        }) {
            Text(text = "Go to page Home")
        }
    }
}