package com.example.login

import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth


fun logoutGoogle(
    navController: NavHostController,
    firebaseAuth: FirebaseAuth,
    googleSignInClient: GoogleSignInClient,
    onLogoutComplete: () -> Unit
) {
    firebaseAuth.signOut()
    googleSignInClient.signOut().addOnCompleteListener {
        googleSignInClient.revokeAccess().addOnCompleteListener {
            isConnected = false
            onLogoutComplete()
        }
    }
}

