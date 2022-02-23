pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    val kotlinVersion = "1.6.20-M1"

    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}

rootProject.name = "kmathx"

include(
    ":help",
    ":kmath-functions",
    ":kmath-discrete",
    ":planimetric-calculations"
)