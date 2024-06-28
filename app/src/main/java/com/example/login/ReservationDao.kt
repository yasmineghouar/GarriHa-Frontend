package com.example.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAllReservations(): List<Reservation>
    @Query("SELECT * FROM reservations WHERE reservationId = :id")
    fun getReservationById(id: Int): Reservation?


    @Insert
    fun AddReservations(reservation: Reservation)
}
