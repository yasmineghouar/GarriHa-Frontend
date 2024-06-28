plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp") version "1.9.21-1.0.15"
    id("com.google.gms.google-services")
//    id ("com.google.gms.google-services")
}

android {
    namespace = "com.example.login"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.login"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.7")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
//    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("androidx.lifecycle:lifecycle-common-jvm:2.8.0")
//    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Room
    val room_version = "2.5.0"
    implementation ("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    testImplementation ("androidx.room:room-testing:$room_version")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.6.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.6.0")
    implementation ("com.google.code.gson:gson:2.8.5")
    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    //Coroutine tests
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.4.2")
    //MockWebserver
    testImplementation ("com.squareup.okhttp3:mockwebserver:4.9.1")

    implementation ("com.maxkeppeler.sheets-compose-dialogs:core:1.0.2")

    implementation ("com.maxkeppeler.sheets-compose-dialogs:calendar:1.0.2")

    implementation ("com.maxkeppeler.sheets-compose-dialogs:clock:1.0.2")
    implementation ("com.google.zxing:core:3.5.2")

    // google map
    implementation ("com.google.android.gms:play-services-maps:18.0.2")
    implementation ("com.google.maps.android:android-maps-utils:2.2.3")

    // icons
    implementation ("androidx.compose.material:material-icons-extended:1.0.0")

    // connexion gmail
//    implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
    implementation (platform("com.google.firebase:firebase-bom:31.5.0"))
    implementation ("com.google.firebase:firebase-auth-ktx")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // photo
    implementation ("com.google.accompanist:accompanist-permissions:0.23.1")

    // QR Code
    implementation("com.lightspark:compose-qr-code:1.0.1")

//    implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.0"
//    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
//
//    implementation ("com.google.dagger:hilt-android:2.44")
////    kapt ("com.google.dagger:hilt-compiler:2.44")
//    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10") // Check for the latest version
    implementation ("androidx.compose.material:material:1.0.5") // Check for the latest version
    implementation ("androidx.compose.ui:ui:1.0.5") // Check for the latest version
    implementation ("androidx.compose.runtime:runtime:1.0.5") // Check for the latest version

}

