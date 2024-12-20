plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("org.jetbrains.kotlin.plugin.serialization")
    id ("com.google.gms.google-services")
}


android {
    namespace = "com.example.myjob"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myjob"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        resConfigs("en", "fr")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    //DaggerHilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    // retrofit
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    // GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    //navigation Componnent
    implementation("androidx.navigation:navigation-compose:2.6.0-alpha08")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0-alpha06")
    implementation("androidx.compose.material:material-icons-extended")

    // KotlinX Serialization
    implementation ("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    // Room
    implementation ("androidx.room:room-runtime:2.4.3")
    kapt ("androidx.room:room-compiler:2.4.3")
    implementation ("androidx.room:room-ktx:2.4.3")

    // Preferences DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    implementation ("androidx.datastore:datastore-preferences-core:1.0.0")
    // Proto DataStore
    implementation  ("androidx.datastore:datastore-core:1.0.0")

    //compose
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")
    // optional - Jetpack Compose integration
    implementation("androidx.paging:paging-compose:3.3.0-alpha04")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation ("androidx.compose.material:material:1.6.7")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("io.coil-kt:coil-compose:2.0.0-rc01")
    implementation("com.google.accompanist:accompanist-pager:0.22.0-rc")
    // The compose calendar library for Android
    implementation("com.kizitonwose.calendar:compose:2.3.0")
    /*implementation("com.google.maps.android:maps-compose:4.3.3")*/

    // exoplayer
    implementation ("com.google.android.exoplayer:exoplayer:2.18.0")
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    //gmail
    implementation ("com.google.firebase:firebase-auth-ktx:21.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.4.1")
    implementation ("org.greenrobot:eventbus:3.1.1")

    //// CAMERA STUFF ////
    implementation ("androidx.camera:camera-camera2:1.3.0-beta02")
    implementation ("androidx.camera:camera-lifecycle:1.3.0-beta02")
    implementation ("androidx.camera:camera-view:1.3.0-beta02")
    implementation ("androidx.camera:camera-extensions:1.3.0-beta02")

    //// ACCOMPANIST ////
    implementation ("com.google.accompanist:accompanist-permissions:0.31.6-rc")
    //coil
    implementation("io.coil-kt:coil-compose:1.3.1")

    //mlkit text recognition
    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:18.0.2")

    //lottie
    implementation( "com.airbnb.android:lottie-compose:5.2.0")

    //pager
    implementation ("com.google.accompanist:accompanist-pager:0.12.0")

    implementation ("androidx.appcompat:appcompat:1.7.0-alpha01")

    implementation("me.onebone:toolbar-compose:2.3.5")

    implementation ("androidx.paging:paging-runtime:3.2.0-alpha06")
    implementation ("androidx.paging:paging-compose:1.0.0-alpha20")
}