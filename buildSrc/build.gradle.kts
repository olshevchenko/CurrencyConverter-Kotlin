import org.gradle.kotlin.dsl.`kotlin-dsl`

object BuildDependenciesPlugins {
    const val TOOLS_BUILD_GRADLE = "com.android.tools.build:gradle:4.1.3"
//    const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.31"
//    const val detektGradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.14.1"
}

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation(BuildDependenciesPlugins.TOOLS_BUILD_GRADLE)
    implementation(kotlin("stdlib"))
//    implementation(BuildDependenciesPlugins.KOTLIN_STDLIB)
//    implementation(BuildDependenciesPlugins.detektGradlePlugin)
}
