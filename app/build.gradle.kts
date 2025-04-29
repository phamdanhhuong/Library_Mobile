plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.phamhuong.library"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.phamhuong.library"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.swiperefreshlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.0")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.18.7")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.18.7")
    implementation ("com.google.android.exoplayer:exoplayer-hls:2.18.7")
    implementation ("com.google.android.exoplayer:exoplayer-dash:2.18.7")
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.7")

}