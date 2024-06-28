package com.example.login.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel


class MapsViewModel : ViewModel() {
    fun getParkingData(context: Context): List<ParkingData> {
        // Implémentez ici le code pour récupérer les données des parkings depuis votre base de données
        // et les retourner sous forme d'une liste de données de parking (ParkingData)
        return listOf(
            ParkingData("Parking A", 36.7783, 3.0588),
            ParkingData("Parking B", 36.7578, 3.0793),
            ParkingData("Parking C", 36.7465, 3.0912)
        )
    }
}

data class ParkingData(val name: String, val latitude: Double, val longitude: Double)