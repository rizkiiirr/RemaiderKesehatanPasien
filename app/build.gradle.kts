plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // HAPUS -> alias(libs.plugins.kotlin.compose) // Baris ini mungkin tidak benar

    id("kotlin-kapt") // <-- TAMBAHKAN INI
    id("com.google.dagger.hilt.android")
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.google.gms.google.services) // <-- TAMBAHKAN INI JUGA
}

android {
    namespace = "com.example.remainderkesehatanpasien"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.remainderkesehatanpasien"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    // Hilt memerlukan penambahan ini di dalam blok 'android'
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependensi Inti
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    // Dependensi UI Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended.android)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.foundation)
    implementation(libs.coil.compose)

    // Dependensi Room (Database) - Ini sudah benar
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation(libs.firebase.analytics)
    kapt("androidx.room:room-compiler:2.6.1")

    // Dependensi Hilt (Dependency Injection) - <-- TAMBAHKAN INI
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    // Dependensi Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Untuk konversi JSON ke objek Kotlin menggunakan Gson

    // OkHttp (Biasanya datang bersama Retrofit, tapi bisa ditambahkan secara eksplisit untuk logging)
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0") // Untuk logging request/response di Logcat

    // Coil untuk memuat gambar dari URL
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Import Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))

// Dependency untuk Analytics (seharusnya sudah ditambahkan oleh alat bantu)
    implementation("com.google.firebase:firebase-analytics")

// Dependency untuk Crashlytics
    implementation("com.google.firebase:firebase-crashlytics")
}

// Tambahkan blok ini di bagian paling bawah file untuk Hilt
kapt {
    correctErrorTypes = true
}