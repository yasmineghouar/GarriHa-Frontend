package com.example.parkir.views.router

sealed class Router(val route: String) {

    object AuthScreen: Router("/auth")
    object HomeScreen: Router("/home")
    object ReservationsScreen: Router("/reservation")
    object ParkingScreen: Router("/parking")
    object SignUpScreen: Router("/signup")
    object MapScreen: Router("/map")
    object DisplayStoredImage: Router("/DisplayImage")
    object ReservationScreen: Router("/createReservation")
    object ReservationScreen2 :Router("/createReservation2/{parkingId}")
    object ReservationsList :Router("/allReservations")
    object ReservationDetails : Router("/ReservationDetails/{idReservation}")
    object ParkingDetails : Router("/ParkingDetails/{parkingId}")
    object Home : Router("/homeScreen")

    object LoggedInHome : Router("/loggedinhome")


}