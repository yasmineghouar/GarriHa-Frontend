package com.example.login

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName ="users" )
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int = 0,
    var firstName: String,
    var lastName: String,
    var email: String,
    var motDePasse : String
)