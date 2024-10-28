plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)

    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.example.githubexplorer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.githubexplorer"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        val keystoreFile = project.rootProject.file("local.properties")
        val githubProperties = org.jetbrains.kotlin.konan.properties.Properties()
        githubProperties.load(keystoreFile.inputStream())

        val clientId: String = System.getenv("GITHUB_CLIENT_ID")
            ?: githubProperties["github.properties.clentId"].toString()
        val appId: String =
            System.getenv("GITHUB_APP_ID") ?: githubProperties["github.properties.appId"].toString()
        buildConfigField("String", "GITHUB_CLIENT_ID", "\"${clientId}\"")
        buildConfigField("int", "GITHUB_APP_ID", appId)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
//        viewBinding = true
        buildConfig = true
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
//    implementation(platform(libs.androidx.compose.animation))
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.fragment.ktx)
    implementation(libs.androidx.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Timber
    implementation(libs.timber)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // OrbitMvi
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.orbit.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}