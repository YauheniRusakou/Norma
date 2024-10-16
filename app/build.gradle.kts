plugins {
    id("com.android.application")
}

android {
    namespace = "by.rusakou.norma"
    compileSdk = 34

    defaultConfig {
        applicationId = "by.rusakou.norma"
        minSdk = 24
        targetSdk = 34
        versionCode = 10 //для Google Play
        versionName = "2.1.2" //для пользователей

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            // isMinifyEnabled = false // до релиза
            isMinifyEnabled = true // Включает сжатие, обфускацию и оптимизацию кода всего за тип сборки релиза вашего проекта
            isShrinkResources = true  // Включает сжатие ресурсов, которое выполняется
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    bundle {
        language {
            // Указывает, что пакет приложения не должен поддерживать APK-файлы конфигурации для языковых ресурсов.
            // Эти ресурсы вместо этого упакованы с каждой базой и динамическая функция APK.
            enableSplit = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("com.google.android.gms:play-services-ads:23.4.0")
}
