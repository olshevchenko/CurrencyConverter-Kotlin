plugins {
    // Application Specific Plugins
    id(BuildDependencies.Android.ANDROID_APPLICATION)
    kotlin(BuildDependencies.BuildPlugins.KOTLIN_ANDROID)
    kotlin(BuildDependencies.BuildPlugins.KOTLIN_KAPT)
    kotlin(BuildDependencies.BuildPlugins.KOTLIN_SERIALIZ_PLUGIN)
}

android {
    compileSdkVersion(BuildConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(BuildConfig.BUILD_TOOLS_VERSION)

    defaultConfig {
        applicationId(BuildConfig.APPLICATION_ID)

        minSdkVersion(BuildConfig.MIN_SDK_VERSION)
        targetSdkVersion(BuildConfig.TARGET_SDK_VERSION)

        versionCode(BuildConfig.VERSION_CODE)
        versionName(BuildConfig.VERSION_NAME)

        testInstrumentationRunner(BuildConfig.TEST_INSTRUMENTATION_RUNNER)
    }

    buildFeatures {
        dataBinding = true
//        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
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
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
//    sourceSets {
//        main.java.srcDirs += 'src/main/kotlin'
//    }
}

dependencies {

//    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation(BuildDependencies.App.CORE_KTX)
//    implementation(kotlin("stdlib"))
//    implementation(kotlin("serialization"))
    implementation(BuildDependencies.Kotlin.KOTLINX_SERIALIZ)

    //Lifecycle
    implementation(BuildDependencies.Lifecycle.LIVEDATA_KTX)
    implementation(BuildDependencies.Lifecycle.VIEWMODEL_KTX)

    //Navigation
    implementation(BuildDependencies.Navigation.FRAGMENT_KTX)
    implementation(BuildDependencies.Navigation.UI)
    implementation(BuildDependencies.Navigation.UI_KTX)

    //Network
    implementation(BuildDependencies.Network.GSON)
    implementation(BuildDependencies.Network.MOSHI)
    implementation(BuildDependencies.Network.RETROFIT)
    implementation(BuildDependencies.Network.RETROFIT_ADAPTER)
    implementation(BuildDependencies.Network.RETROFIT_MOSHI)
    implementation(BuildDependencies.Network.RETROFIT_LOGGER)

    //Rx
    implementation(BuildDependencies.Asynchronous.RX_ANDROID)
    implementation(BuildDependencies.Asynchronous.RX_JAVA)

    //UI
    implementation(BuildDependencies.UI.CONSTRAINT_LAYOUT)
    implementation(BuildDependencies.UI.MATERIAL)

    //DI
    implementation(BuildDependencies.DI.KOIN_MAIN)
    implementation(BuildDependencies.DI.KOIN_ANDROID)

    //Tests
    testImplementation(BuildDependencies.Testing.ARCH_CORE)
    testImplementation(BuildDependencies.Testing.JUNIT)
    testImplementation(BuildDependencies.Testing.KOIN_TEST)
    testImplementation(BuildDependencies.Testing.KOIN_TEST_JUNIT)
    testImplementation(BuildDependencies.Testing.MOCKK)
    testImplementation(BuildDependencies.Testing.MOCKITO_CORE)
    testImplementation(BuildDependencies.Testing.POWERMOCK_API)
    testImplementation(BuildDependencies.Testing.POWERMOCK_JUNIT)
    testImplementation(BuildDependencies.Testing.ROBOLECTRIC)
    androidTestImplementation(BuildDependencies.Testing.JUNIT_ASSERT)
    androidTestImplementation(BuildDependencies.Testing.ESPRESSO_CORE)
    androidTestImplementation(BuildDependencies.Testing.MOCKK_ANDROID)
}