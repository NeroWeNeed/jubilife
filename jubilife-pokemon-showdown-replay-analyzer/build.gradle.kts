plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.kotlin.exposed)
                implementation("org.jsoup:jsoup:1.15.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
            }
        }
    }
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}