val kmathVersion : String by project

plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }

        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-Xcontext-receivers")
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("space.kscience:kmath-core:$kmathVersion")
                api(project(":help"))
                api(project(":kmath-discrete"))
                api(project(":kmath-functions"))
            }
        }
        val commonTest by getting {
            dependencies {
                api("space.kscience:kmath-core:$kmathVersion")
                implementation(kotlin("test"))
            }
        }
    }
}