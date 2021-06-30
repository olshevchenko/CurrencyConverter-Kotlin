object BuildDependencies {

    object Android {
        const val ANDROID_APPLICATION = "com.android.application"
//        const val APP_COMPAT = "androidx.appcompat:appcompat:${BuildVersions.Android.APP_COMPAT}"
    }

    object App {
        const val CORE_KTX = "androidx.core:core-ktx:${BuildVersions.BuildPlugins.KOTLIN_CORE}"
    }

    object Asynchronous {
        const val RX_ANDROID =
            "io.reactivex.rxjava2:rxandroid:${BuildVersions.Asynchronous.RX_ANDROID}"
        const val RX_JAVA = "io.reactivex.rxjava2:rxjava:${BuildVersions.Asynchronous.RX_JAVA}"
    }

    object BuildPlugins {
        const val KOTLIN_ANDROID = "android"
        const val KOTLIN_GRADLE_PLUGIN =
            "org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildVersions.BuildPlugins.KOTLIN}"
        const val KOTLIN_SERIALIZ_PLUGIN = "plugin.serialization"
        const val KOTLIN_KAPT = "kapt"
        const val TOOLS_BUILD_GRADLE =
            "com.android.tools.build:gradle:${BuildVersions.BuildPlugins.GRADLE}"
    }

    object DI {
        const val KOIN_MAIN = "io.insert-koin:koin-android:${BuildVersions.DI.KOIN}"
        const val KOIN_ANDROID = "io.insert-koin:koin-android-ext:${BuildVersions.DI.KOIN_EXT}"
    }

    object Kotlin {
        const val KOTLINX_SERIALIZ =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${BuildVersions.BuildPlugins.KOTLINX_SERIALIZ}"
    }

    object Lifecycle {
        const val LIFECYCLE =
            "androidx.lifecycle:lifecycle-extensions:${BuildVersions.Lifecycle.LIFECYCLE}"
        const val LIVEDATA_KTX =
            "androidx.lifecycle:lifecycle-livedata-ktx:${BuildVersions.Lifecycle.LIFECYCLE}"
        const val VIEWMODEL_KTX =
            "androidx.lifecycle:lifecycle-viewmodel-ktx:${BuildVersions.Lifecycle.LIFECYCLE}"
    }

    object Navigation {
        const val FRAGMENT_KTX =
            "androidx.navigation:navigation-fragment-ktx:${BuildVersions.Navigation.NAVIGATION}"
        const val UI = "androidx.navigation:navigation-ui:${BuildVersions.Navigation.NAVIGATION}"
        const val UI_KTX =
            "androidx.navigation:navigation-ui-ktx:${BuildVersions.Navigation.NAVIGATION}"
    }

    object Network {
        const val GSON = "com.google.code.gson:gson:${BuildVersions.Network.GSON}"
        const val MOSHI = "com.squareup.moshi:moshi-kotlin:${BuildVersions.Network.MOSHI}"
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${BuildVersions.Network.RETROFIT}"
        const val RETROFIT_ADAPTER =
            "com.squareup.retrofit2:adapter-rxjava2:${BuildVersions.Network.RETROFIT}"
        const val RETROFIT_MOSHI =
            "com.squareup.retrofit2:converter-moshi:${BuildVersions.Network.RETROFIT}"
        const val RETROFIT_LOGGER =
            "com.squareup.okhttp3:logging-interceptor:${BuildVersions.Network.OKHTTP}"
    }

    object UI {
        //        const val CARD_VIEW = "androidx.cardview:cardview:${BuildVersions.UI.CARD_VIEW}"
        const val CONSTRAINT_LAYOUT =
            "androidx.constraintlayout:constraintlayout:${BuildVersions.UI.CONSTRAINT_LAYOUT}"
        const val GLIDE = "com.github.bumptech.glide:glide:${BuildVersions.UI.GLIDE}"
        const val GLIDE_COMPILER = "com.github.bumptech.glide:compiler:${BuildVersions.UI.GLIDE}"
        const val MATERIAL = "androidx.appcompat:appcompat:${BuildVersions.UI.MATERIAL}"

        const val RECYCLER_VIEW =
            "androidx.recyclerview:recyclerview:${BuildVersions.UI.RECYCLER_VIEW}"
//        const val SUPPORT_DESIGN = "com.android.support:design:${BuildVersions.UI.SUPPORT_DESIGN}"
    }

    object Testing {
        const val ARCH_CORE =
            "androidx.arch.core:core-testing:${BuildVersions.Testing.ARCH_CORE}"

        const val ESPRESSO_CORE =
            "androidx.test.espresso:espresso-core:${BuildVersions.Testing.ESPRESSO_CORE}"

        const val JUNIT = "junit:junit:${BuildVersions.Testing.JUNIT}"
        const val JUNIT_ASSERT = "androidx.test.ext:junit:${BuildVersions.Testing.JUNIT_ASSERT}"

        const val KOIN_TEST = "io.insert-koin:koin-test:${BuildVersions.DI.KOIN}"
        const val KOIN_TEST_JUNIT = "io.insert-koin:koin-test-junit4:${BuildVersions.DI.KOIN}"

        const val MOCKK = "io.mockk:mockk:${BuildVersions.Testing.MOCKK}"
        const val MOCKK_ANDROID = "io.mockk:mockk-android:${BuildVersions.Testing.MOCKK}"

        const val MOCKITO_CORE = "org.mockito:mockito-core:${BuildVersions.Testing.MOCKITO}"

        const val POWERMOCK_API =
            "org.powermock:powermock-api-mockito2:${BuildVersions.Testing.POWERMOCK}"
        const val POWERMOCK_JUNIT =
            "org.powermock:powermock-module-junit4:${BuildVersions.Testing.POWERMOCK}"
        const val ROBOLECTRIC =
            "org.robolectric:robolectric:${BuildVersions.Testing.ROBOLECTRIC}"

//        const val MOCKITO_INLINE = "org.mockito:mockito-inline:${BuildVersions.Testing.MOCKITO_INLINE}"
//        const val RUNNER = "androidx.test:runner:${BuildVersions.Testing.RUNNER}"
    }
}