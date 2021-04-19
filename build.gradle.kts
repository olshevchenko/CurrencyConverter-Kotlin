//import com.olshevchenko.currencyconverter.BuildDependencies

plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(BuildDependencies.BuildPlugins.TOOLS_BUILD_GRADLE)
//        classpath(BuildDependencies.BuildPlugins.KOTLIN_GRADLE_PLUGIN)
        classpath(kotlin("gradle-plugin", version = BuildVersions.BuildPlugins.KOTLIN))
        classpath(kotlin("serialization", version = BuildVersions.BuildPlugins.KOTLIN))
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

//task clean(type: Delete) {
//    delete rootProject.buildDir
//}