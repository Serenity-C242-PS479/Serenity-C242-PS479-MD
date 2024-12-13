plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"
}

android {
    namespace = "com.serenity.serenityapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.serenity.serenityapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_BACKEND_URL", "\"https://serenity-621043177021.asia-southeast2.run.app/api/v1/\"")
        buildConfigField("String", "API_PREDICT_BREAK_REMINDER_URL", "\"https://asia-southeast2-c242-ps479.cloudfunctions.net/\"")
        buildConfigField("String", "API_PREDICT_SENTIMENT_URL", "\"https://asia-southeast2-c242-ps479.cloudfunctions.net/\"")
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
    viewBinding {
        enable = true
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    // General
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.activity.ktx)

    // Coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    // Chart
    implementation(libs.mpandroidchart)

    // Circle Image View
    implementation(libs.circleimageview)

    // Glide
    implementation (libs.glide)
    ksp(libs.glide.compiler)

    // Retrofit 2
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // Data Store
    implementation(libs.androidx.datastore.preferences)

    // Testing
    testImplementation(libs.junit)
}