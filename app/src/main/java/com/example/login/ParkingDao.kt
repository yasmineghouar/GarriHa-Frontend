package com.example.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ParkingDao {
    @Query("SELECT * FROM parkings")
    fun getAllParkings(): List<Parking>

    @Query("SELECT * FROM parkings WHERE parkingId = :parkingId")
    fun getParkingById(parkingId: Int): Parking?

    @Query("SELECT parkingName FROM parkings WHERE parkingId = :id")
    fun getParkingNameById(id: Int): String?

    @Query("DELETE FROM parkings")
    fun deleteAllParkings()

    @Insert
    fun addParking(parking: Parking)

}