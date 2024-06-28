package com.example.login.views

class Signup {
}


//class SignUpActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up)
//
//        val signUpButton: Button = findViewById(R.id.signUpButton)
//        signUpButton.setOnClickListener {
//            val userData = UserData(
//                userId = 0, // Set appropriate values
//                firstName = firstNameEditText.text.toString(),
//                lastName = lastNameEditText.text.toString(),
//                email = emailEditText.text.toString(),
//                NUmeroTel = numeroTelEditText.text.toString(),
//                MotDepasse = passwordEditText.text.toString(),
//                PhotoUser = "" // Set appropriate value
//            )
//
//            // Call sign-up API
//            CoroutineScope(Dispatchers.IO).launch {
//                val response = try {
//                    RetrofitClient.endpoint.signUp(userData)
//                } catch (e: Exception) {
//                    null
//                }
//
//                withContext(Dispatchers.Main) {
//                    if (response != null && response.isSuccessful) {
//                        val signUpResponse = response.body()
//                        if (signUpResponse != null && signUpResponse.status == 200) {
//                            // Sign-up successful, handle response
//                        } else {
//                            // Sign-up failed, handle error
//                        }
//                    } else {
//                        // Network error, handle error
//                    }
//                }
//            }
//        }
//    }
//}


