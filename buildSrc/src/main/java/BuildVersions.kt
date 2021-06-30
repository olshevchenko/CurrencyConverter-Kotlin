/**
 * Version constants for the Kotlin DSL dependencies
 */
object BuildVersions {

    object Android {
        const val APP_COMPAT = "1.4.0"
    }

    object App {}

    object Asynchronous {
        const val RX_ANDROID = "2.1.1"
        const val RX_JAVA = "2.2.21"
    }

    object BuildPlugins {
        const val GRADLE = "4.2.+"
        const val KOTLIN = "1.5.10"
        const val KOTLIN_CORE = "1.5.0"
        const val KOTLINX_SERIALIZ = "1.2.1"
    }

    object DI {
        const val KOIN = "3.1.0"
        const val KOIN_EXT = "3.0.2"
    }

    object Lifecycle {
        const val LIFECYCLE = "2.2.0"
    }

    object Navigation {
        const val NAVIGATION = "2.3.3"
        const val NAVIGATION_ARGS = "1.0.0"
    }

    object Network {
        const val GSON = "2.8.6"
        const val MOSHI = "1.11.0"
        const val RETROFIT = "2.9.0"
        const val OKHTTP = "4.9.1"
    }

    object Testing {
        const val ARCH_CORE = "2.1.0"
        const val ESPRESSO_CORE = "3.3.0"

        //        const val GOOGLE_TRUTH = "0.44"
        const val JUNIT = "4.12"
        const val JUNIT_ASSERT = "1.1.2"

        //        const val JUNIT5 = "3.4.2"
        const val MOCKK = "1.10.6"
        const val MOCKITO = "3.8.0"

        //        const val MOCKITO_INLINE = "2.13.0"
        const val POWERMOCK = "2.0.9"
        const val ROBOLECTRIC = "4.4"
        const val RUNNER = "1.2.0-beta01"
    }

    object UI {
        const val GLIDE = "4.11.0"
        const val CONSTRAINT_LAYOUT = "2.0.4"
        const val MATERIAL = "1.2.0"
        const val RECYCLER_VIEW = "1.1.0"
    }
}