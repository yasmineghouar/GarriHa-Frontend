package com.example.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email AND motDePasse = :password")
    fun getUserByEmailAndPassword(email: String, password: String): User?

    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM users")
    fun getAllUsers() : List<User>

    suspend fun hasUsers(): Boolean {
        return getCount() > 0
    }
    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: Int): User?
    @Query("SELECT COUNT(*) FROM users")
    suspend fun getCount(): Int

}