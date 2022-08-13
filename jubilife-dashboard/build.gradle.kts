plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "github.nwn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}


kotlin {
    jvm {
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(IR) {
        nodejs()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {

        }
        val commonTest by getting
        val jvmMain by getting {
            dependencies {
                implementation(libs.bundles.kotlin.exposed)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
        val jsTest by getting {

        }
    }
}


tasks.getByName<Test>("test") {
    useJUnitPlatform()
}