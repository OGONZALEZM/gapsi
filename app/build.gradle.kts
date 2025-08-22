plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.ogonzalezm.test_gepsi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ogonzalezm.test_gepsi"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://axesso-walmart-data-service.p.rapidapi.com/wlm/\"")
            buildConfigField("String", "RAPID_API_KEY", "\"fce0e15738msh6a87c0c9db9505cp14b74fjsn54bc768f3bc7\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"https://axesso-walmart-data-service.p.rapidapi.com/wlm/\"")
            buildConfigField("String", "RAPID_API_KEY", "\"fce0e15738msh6a87c0c9db9505cp14b74fjsn54bc768f3bc7\"")
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
        buildConfig = true
    }

}

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Retrofit & GSON
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.gson)

    // Hilt
    implementation(libs.hilt)
    implementation(libs.hiltCompose)
    ksp(libs.hiltCompiler)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    testImplementation(kotlin("test"))

    // Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    implementation(libs.paging.runtime.ktx)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}