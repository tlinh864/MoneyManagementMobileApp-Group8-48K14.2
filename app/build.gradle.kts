plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "org.o7planning.nhom8_quanlychitieu"
    compileSdk = 35

    defaultConfig {
        applicationId = "org.o7planning.nhom8_quanlychitieu"
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
    buildFeatures {
        viewBinding = true
    }

    // Thêm cờ compiler để hiển thị chi tiết về các API deprecated
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Thêm CardView cho giao diện
    implementation("androidx.cardview:cardview:1.0.0")

    // Thêm RecyclerView cho danh sách giao dịch
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Thêm thư viện MPAndroidChart cho biểu đồ
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}