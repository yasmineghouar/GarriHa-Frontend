package com.example.login

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "reservations",
    foreignKeys = [
        ForeignKey(
            entity = Parking::class,
            parentColumns = ["parkingId"],
            childColumns = ["idparking"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["iduser"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reservation(
    @PrimaryKey(autoGenerate = false)
    var reservationId: Int,
    val numero: Int,
    val QRCode: String,
    val DateReservation: String,
    var iduser: Int,
    val idparking: Int,
    val HeureDebut: String,
    val HeureFin: String
)