package com.example.login

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="parkings")
data class Parking(
    @PrimaryKey(autoGenerate = false)
    var parkingId: Int ,
    val parkingName:String,
    val CommuneParking:String?,
    val WilayaParking:String?,
    val AdresseParking : String?,
    val photoParking : String,
    val nombreDePlaces : Int?,
    val PrixParHeure : Int?,
    val DescriptionParking : String?,
    val SurfaceParking : String?,
    val Latitude : Double,
    val Longitude : Double
)