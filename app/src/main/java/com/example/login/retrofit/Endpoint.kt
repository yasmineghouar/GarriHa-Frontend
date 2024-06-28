package com.example.login.retrofit

import android.net.Uri
import com.example.login.consts.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Endpoint {

    @POST("user/signup")
    suspend fun signUp(
        @Body userData: UserDataa
    ): Response<UserData>

    @POST("user/signupGoogle")
    suspend fun signupGoogle(
        @Body userData: UserDataa
    ): Response<UserData>

    @POST("user/login")
    suspend fun login(
        @Body loginData: loginData
    ): Response<UserData>

    @GET("Parking/allParkings")
//    suspend fun getAllParkings(): Response<ParkingResponse>
      suspend fun getAllParkings(): Response<List<Parkingg>>

    @POST("Reservation/createReservation")
    suspend fun createReservation(
        @Body reservationData: ReservationData
    ): Response<ReservationDetails>

    @GET("Reservation/allReservations")
    suspend fun getAllReservations(): Response<List<ReservationAttributes>>

    @GET("Reservation/getreservation/{iduser}")
    suspend fun getReservationDetails(@Path("iduser") iduser: Int): Response<ReservationDetails>

    @GET("Reservation/allUserReservations/{iduser}")
    suspend fun getAllUserReservations(@Path("iduser") iduser: Int): Response<List<ReservationDetails>>


    @GET("Parking/getparking/{parkingId}")
    suspend fun getParkingById(@Path("parkingId") parkingId: Int): Response<Parkingg>

    companion object {
        private const val BASE_URL = url
        private var endpoint: Endpoint? = null

        fun createEndpoint(): Endpoint {
            if (endpoint == null) {
                synchronized(this) {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    endpoint = retrofit.create(Endpoint::class.java)
                    println(endpoint) ;
                }
            }
            return endpoint!!
        }
    }
}


data class SignUpData(
    val userId: Int,
    val firstName : String,
    val lastName: String,
    val email: String,
    val NUmeroTel : Int,
    val motDePasse : String,
//    val PhotoUser : String
    val PhotoUser: Uri? = null
)

data class SignUpResponse(
    val status: Int,
    val message: String,
    val data: SignUpData
)

data class UserData(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val NUmeroTel: Int,
    val motDePasse: String,
    val PhotoUser: String
)
data class UserDataa(
    val userId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val NUmeroTel: Int,
    val motDePasse: String,
    val PhotoUser: Uri? = null
)

data class loginData(
    val email: String,
    val motDePasse : String
)

data class ParkingResponse(
    val status: String,
    val data: List<Parkingg>
//    val parkings: List<Parkingg>
)

data class Parkingg(
    val parkingId:Int,
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

//ce qui sera envoyé en post pr creer reservation
data class ReservationData(
    val DateReservation: String,
    val iduser: Int,
    val idparking: Int,
    val HeureDebut: String,
    val HeureFin: String

)

data class CreateReservationResponse(
    val status: Int,
    val message: String,
    val data: ReservationData
)

data class ReservationAttributes(
    val reservationId: Int,
    val DescriptionReservation: String,
    val numero: String,
    val iduser: Int,
    val idparking: Int,
    val dateDebut: String,
    val dateFin: String
)

data class ReservationDetails(
    val reservationId: Int,
    val numero: Int,
    val QRCode: String,
    val DateReservation: String,
    val HeureDebut: String,
    val HeureFin: String,
    val iduser: Int,
    val idparking: Int
)
