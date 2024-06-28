package com.example.parkir.views.router


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.login.AuthView
import com.example.login.BottomMenuScreen
import com.example.login.HomeView
import com.example.login.HomeVieww
import com.example.login.LoggedInHomeView
import com.example.login.MapView
import com.example.login.ParkingDetailsScreen
import com.example.login.ParkingView
import com.example.login.ReservationCreateView2
import com.example.login.ReservationDetailsScreen
import com.example.login.ReservationsListView
import com.example.login.SignUpView
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavigationHost(navController: NavHostController,
                   googleSignInClient: GoogleSignInClient,
                   firebaseAuth: FirebaseAuth
){

    NavHost(navController = navController, startDestination = Router.Home.route) {
        composable(route = Router.Home.route) {
            HomeVieww(navController = navController)
        }
        composable(route = Router.HomeScreen.route) {
            HomeView(navController = navController)
        }
        composable(route = Router.AuthScreen.route) {
            AuthView(navController = navController, googleSignInClient = googleSignInClient, firebaseAuth = firebaseAuth)
        }
        composable(route = Router.ParkingScreen.route) {
            ParkingView(navController = navController , firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
        composable(route = Router.SignUpScreen.route) {
            SignUpView(navController = navController)
        }
        composable(route = Router.MapScreen.route) {
            MapView(navController = navController,firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
        composable(route = Router.ReservationScreen2.route){
            ReservationCreateView2(navController = navController)
        }
        composable(route = Router.ReservationsList.route){
            ReservationsListView(navController = navController, firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
        composable(route = Router.ReservationDetails.route){
            ReservationDetailsScreen(navController = navController)
        }
        composable(route = Router.ParkingDetails.route){
            ParkingDetailsScreen(navController = navController)
        }
        composable(route = Router.Home.route){
            HomeVieww(navController = navController)
        }
        composable(route = Router.LoggedInHome.route){
            LoggedInHomeView(navController = navController, googleSignInClient = googleSignInClient, firebaseAuth = firebaseAuth)
        }
        composable(BottomMenuScreen.Home.route) {
            LoggedInHomeView(navController = navController, googleSignInClient = googleSignInClient, firebaseAuth = firebaseAuth)
        }
        composable(BottomMenuScreen.GoToMap.route) {
            MapView(navController = navController,firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
        composable(BottomMenuScreen.ReservationList.route) {
            ReservationsListView(navController = navController, firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
        composable(BottomMenuScreen.ParkingList.route) {
            ParkingView(navController = navController , firebaseAuth = firebaseAuth , googleSignInClient = googleSignInClient)
        }
    }
}