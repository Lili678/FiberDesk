plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id ("kotlin-parcelize")
}

android {
    namespace = "com.example.fiberdesk_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fiberdesk_app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
        buildConfig = true
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "DEBUG_MODE", "true")
            buildConfigField("String", "BASE_URL", "\"AUTO\"")
            buildConfigField("String", "LOCAL_IP", "\"192.168.12.208\"")
            buildConfigField("String", "API_PORT", "\"3000\"")
            // URL remota para datos móviles (cambia esto cuando uses ngrok/localtunnel/railway)
            buildConfigField("String", "REMOTE_URL", "\"https://busy-drinks-kiss.loca.lt\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "DEBUG_MODE", "false")
            buildConfigField("String", "BASE_URL", "\"AUTO\"")
            buildConfigField("String", "LOCAL_IP", "\"192.168.12.208\"")
            buildConfigField("String", "API_PORT", "\"3000\"")
            // URL remota para producción (usa Railway o similar)
            buildConfigField("String", "REMOTE_URL", "\"https://busy-drinks-kiss.loca.lt\"")
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
    }
}

dependencies {

    // Desde libs.versions.toml
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // Google Maps y ubicación
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    
    // Lifecycle & LiveData & ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // UI Components
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.12.0")
}