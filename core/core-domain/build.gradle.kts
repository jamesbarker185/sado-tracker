plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sadotracker.corecoredomain"
    compileSdk = 35

    defaultConfig {
        minSdk = 29
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
    implementation(project(":core:core-database"))
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.datastore.preferences)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
}

