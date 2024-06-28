package com.example.login

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.login.retrofit.Endpoint
import com.example.login.retrofit.UserData
import com.example.login.retrofit.UserDataa
import com.example.login.retrofit.loginData
import com.example.parkir.views.router.Router
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopAppBar(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                color = Color(0xFFE0FDFF),
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFF1D4460)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthView(navController: NavHostController,
             googleSignInClient: GoogleSignInClient,
             firebaseAuth: FirebaseAuth
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var motDePasse by remember { mutableStateOf("") }
    var responseLogin by remember { mutableStateOf<Response<UserData>?>(null) }
    var showDialogUser by remember { mutableStateOf(false) }
    var dialogUserMessage by remember { mutableStateOf("") }



    val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
        Log.d("AuthScreen", "Google sign-in result: resultCode=${result.resultCode}, data=${result.data}")
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                Log.d("AuthScreen", "Google sign-in success: account=${account.email}")
                firebaseAuthWithGoogleCredential(account.idToken!!, firebaseAuth, navController)
            } catch (e: ApiException) {
                Log.e("AuthScreen", "Google sign-in failed", e)
            }
        } else {
            Log.e("AuthScreen", "Google sign-in canceled or failed")
        }
    }

        Scaffold(
            topBar = { AuthTopAppBar(title = "Se connecter") },
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0FDFF))
        ) {
        Spacer(modifier = Modifier.height(36.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(horizontal = 16.dp)
                .background(Color(0xFFE0FDFF)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "Bienvenu !\n Connectez-vous et oubliez les tracas liés au stationnement.",
                color = Color(0xFF1D4460),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(bottom = 36.dp,)
                    .padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Adresse mail", color = Color(0xFF1D4460)) }, // Couleur du texte du label
                modifier = Modifier.padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            TextField(
                value = motDePasse,
                onValueChange = { motDePasse = it },
                label = { Text("Mot de Passe" , color = Color(0xFF1D4460))  },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.White // Couleur du curseur dans le champ de texte
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ou",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color(0xFF1D4460),
                fontWeight = FontWeight.Normal
            )
            // Google Sign In Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logogmail), // Replace with actual Google icon resource
                    contentDescription = null,
                    modifier = Modifier.size(34.dp)
                )
                TextButton(
                    onClick = {
                        Log.d("AuthScreen", "Launching Google sign-in intent")
                        logoutGoogle(navController, firebaseAuth, googleSignInClient) {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        }
                    }
                ) {
                    Text("Se connectez avec Gmail")
                }
            }
//        }

            Spacer(modifier = Modifier.height(26.dp))

            val activity = (LocalContext.current as? Activity)
            // Login Button
            Button(
                onClick = {
                    if (validateLoginFields(email, motDePasse)) {
                        val loginData = loginData(
                            email = email,
                            motDePasse = motDePasse
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            val loginResponse = try {
                                Endpoint.createEndpoint().login(loginData)
                            } catch (e: Exception) {
                                null
                            }

                            responseLogin = loginResponse
                            if (responseLogin?.body() != null) {
                                Log.d("loginResponse : ", "${loginResponse?.body()}")
                                dialogUserMessage = "Authentification réussie"
                                activity?.runOnUiThread { // Run on the main thread
                                    isConnected = true
                                    navController.navigate(Router.LoggedInHome.route)
                                }
                            } else {
                                Log.d(
                                    "FailedloginResponse : ",
                                    "${loginResponse?.errorBody()?.string()}"
                                )
                                dialogUserMessage = "Adresse ou mot de passe incorrect"
                                activity?.runOnUiThread { // Run on the main thread
                                    showDialogUser = true
                                }
                            }
                        }
                    } else {
                        activity?.runOnUiThread { // Run on the main thread
                            dialogUserMessage = "Veuillez remplir tous les champs"
                            showDialogUser = true
                        }
                    }
                },
                modifier = Modifier
                    .size(width = 180.dp, height = 45.dp) // Définir la largeur et la hauteur ici
                    .fillMaxWidth() // Pour centrer horizontalement
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Se connecter",
                    fontFamily = FontFamily.Serif,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up Link
            ClickableText(
                text = AnnotatedString.Builder().apply {
                    append("Vous n'avez pas de compte ? ")
                    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    append("Inscrivez-vous.")
                    pop()
                }.toAnnotatedString(),
                onClick = {
                    navController.navigate(Router.SignUpScreen.route)
                },
                style = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF1D4460),
                    fontWeight = FontWeight.Bold
                )
            )

            // Affichage du popup
            if (showDialogUser) {
                AlertDialog(
                    onDismissRequest = { showDialogUser = false },
                    title = { Text(text = "Statut de l'authentification") },
                    text = { Text(text = dialogUserMessage) },
                    confirmButton = {
                            Button(
                                onClick = { showDialogUser = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1D4460),
                                )
                            ) {
                                Text("OK")
                            }
                    }
                )
            }
        }
    }
}
private fun firebaseAuthWithGoogleCredential(
    idToken: String,
    firebaseAuth: FirebaseAuth,
    navController: NavHostController
) {

    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                val googleUserId = user?.uid
                val firstName = user?.displayName?.split(" ")?.getOrNull(0) ?: ""
                val lastName = user?.displayName?.split(" ")?.getOrNull(1) ?: ""
                val email = user?.email ?: ""
                val photoUrl = user?.photoUrl

                // Create user data object
                val userData = UserDataa(
                    userId = 0,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    NUmeroTel = 0,
                    motDePasse = "",
                    PhotoUser = photoUrl
                )

                // Send user data to backend
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = Endpoint.createEndpoint().signupGoogle(userData)
                        if (response.isSuccessful) {
                            withContext(Dispatchers.Main) {
                                isConnected = true
                                navController.navigate(Router.LoggedInHome.route)
                            }
                        } else {
                            Log.e("AuthScreen", "Failed to add user to database: ${response.errorBody()?.string()}")
                        }
                    } catch (e: Exception) {
                        Log.e("AuthScreen", "Error adding user to database", e)
                    }
                }
            } else {
                // Sign in failed, handle the error
                val exception = task.exception
                if (exception is FirebaseNetworkException) {
                    Log.e("AuthScreen", "Network error occurred", exception)
                } else if (exception is FirebaseAuthInvalidCredentialsException) {
                    if (exception.errorCode == "EXPIRED_ID_TOKEN") {
                        Log.e("AuthScreen", "ID token expired, get a new token and try again")
                    } else {
                        Log.e("AuthScreen", "Invalid credentials: ${exception.message}", exception)
                    }
                } else {
                    Log.e("AuthScreen", "Authentication failed", exception)
                }
            }
        }
}

private fun validateFields(firstName: String, lastName: String, email: String, NUmeroTel: String, password: String, confirmPassword: String): Boolean {
    // Ensure all fields are filled and passwords match
    return firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && NUmeroTel.isNotBlank() && password.isNotBlank() && password == confirmPassword
}

private fun validateLoginFields(email: String, password: String) : Boolean {
    // ensure all fields are filled
    return email.isNotBlank() && password.isNotBlank()
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpView(navController: NavHostController) {
    val context = LocalContext.current

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var NUmeroTel by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var motDePasse by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var response by remember { mutableStateOf<Response<UserData>?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var showdialogMessage by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            photoUri = uri
        }
    )

    Scaffold(
        topBar = { AuthTopAppBar(title = "S'inscrire") },
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0FDFF))
    ) {
        Spacer(modifier = Modifier.height(66.dp))
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE0FDFF))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(110.dp))

            // Button to select photo
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier.padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7AC2D5),
                    contentColor = Color.White
                )
            ) {
                Text("Choisir une photo")
            }

            // Display selected image
            photoUri?.let {
                val bitmap = remember { mutableStateOf<Bitmap?>(null) }
                LaunchedEffect(it) {
                    context.contentResolver.openInputStream(it)?.let { stream ->
                        bitmap.value = BitmapFactory.decodeStream(stream)
                    }
                }
                bitmap.value?.let { bmp ->
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // TextField for first name
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom", color = Color(0xFF1D4460)) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            // TextField for last name
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom", color = Color(0xFF1D4460)) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            // TextField for email
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Adresse email", color = Color(0xFF1D4460)) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            // TextField for phone number
            TextField(
                value = NUmeroTel,
                onValueChange = { NUmeroTel = it },
                label = { Text("Numéro de téléphone", color = Color(0xFF1D4460)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            // TextField for password
            TextField(
                value = motDePasse,
                onValueChange = { motDePasse = it },
                label = { Text("Mot de passe", color = Color(0xFF1D4460)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(6.dp))

            // TextField for password confirmation
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmation du mot de passe", color = Color(0xFF1D4460)) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFF7AC2D5),
                    cursorColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(26.dp))

            val activity = (LocalContext.current as? Activity)
            // Button to submit form
            Button(
                onClick = {
                    // Validate form fields
                    if (validateFields(firstName, lastName, email, NUmeroTel, motDePasse, confirmPassword)) {
                        val userData = UserDataa(
                            userId = 0,
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            NUmeroTel = NUmeroTel.toInt(),
                            motDePasse = motDePasse,
                            PhotoUser = null
                        )

                        // Submit signup request
                        CoroutineScope(Dispatchers.IO).launch {
                            val signUpResponse = try {
                                Endpoint.createEndpoint().signUp(userData)
                            } catch (e: Exception) {
                                null
                            }

                                response = signUpResponse
                            withContext(Dispatchers.Main) {
                                // Show dialog based on signup response
                                if (response?.body() != null) {
                                    showdialogMessage = "Inscription réussie"
//                                    showDialog = true
                                    activity?.runOnUiThread { // Run on the main thread
                                        isConnected = true
                                        navController.navigate(Router.LoggedInHome.route)
                                    }
                                } else {
                                    showdialogMessage = "Adresse e-mail déjà utilisée"
                                    showDialog = true
                                }
                            }

                        }
                    } else {
                        // Show error dialog if form fields are not valid
                        showdialogMessage = "Veuillez remplir tous les champs et vous assurer que les mots de passe correspondent"
                        showDialog = true
                    }
                },
                modifier = Modifier
                    .size(width = 180.dp, height = 45.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4460),
                    contentColor = Color.White
                )
            ) {
                Text(text = "S'inscrire")
            }

            Spacer(modifier = Modifier.height(16.dp))


            // Login Up
            ClickableText(
                text = AnnotatedString.Builder().apply {
                    append("Vous avez déjà un compte ? ")
                    pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                    append("Connectez-vous.")
                    pop()
                }.toAnnotatedString(),
                onClick = {
                    navController.navigate(Router.AuthScreen.route)
                },
                style = androidx.compose.ui.text.TextStyle(
                    color = Color(0xFF1D4460),
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))


            // Dialog to display signup status
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Statut de l'inscription") },
                    text = { Text(text = showdialogMessage) },
                    confirmButton = {
                        Button(
                            onClick = { showDialog = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1D4460),
                            )
                        ) {
                            Text(
                                "OK",
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
        }
    }
}


