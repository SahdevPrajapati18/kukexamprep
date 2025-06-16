plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kuk.kukexamprep"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kuk.kukexamprep"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "SUPABASE_API_KEY", "\"${property("SUPABASE_API_KEY")}\"")
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
    buildFeatures{
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    // Firebase BoM (Bill of Materials)
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))

    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Authentication (remove version since we're using BoM)
    implementation("com.google.firebase:firebase-auth")

    // Firebase Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Realtime Database (using BoM, no version needed)
    implementation("com.google.firebase:firebase-database")

    // Image loading library
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
   // implementation("com.github.barteksc:android-pdf-viewer:2.8.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.google.android.material:material:1.12.0") // or latest
    //pdf library
    //implementation("com.github.barteksc:android-pdf-viewer:2.8.2")
    //dbshbs
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // build.gradle
    //implementation("io.github.jan-tennert.supabase:postgrest-kt:1.2.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    //implementation("com.joanzapata.pdfview:android-pdfview:2.8.2@aar")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // OkHttp for network requests
    implementation("com.squareup.okhttp3:okhttp:4.9.3")


    // JSON Processing
    implementation("org.json:json:20210307")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    // App dependencies
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}