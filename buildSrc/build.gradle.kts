import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.`kotlin-dsl`

object BuildDependenciesPluginsVersions {
    const val GRADLE = "4.2.+"
    const val KOTLIN = "1.5.10"
}

object BuildDependenciesPlugins {
    const val TOOLS_BUILD_GRADLE =
        "com.android.tools.build:gradle:${BuildDependenciesPluginsVersions.GRADLE}"
    const val KOTLIN_GRADLE_PLUGIN =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildDependenciesPluginsVersions.KOTLIN}"
}

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    google()
    jcenter()
}

dependencies {
    implementation(BuildDependenciesPlugins.TOOLS_BUILD_GRADLE)
    implementation(BuildDependenciesPlugins.KOTLIN_GRADLE_PLUGIN)
    implementation(kotlin("stdlib"))
//    implementation(BuildDependenciesPlugins.KOTLIN_STDLIB)
//    implementation(BuildDependenciesPlugins.detektGradlePlugin)
}
