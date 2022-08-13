import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.7.10" apply false
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev753" apply false
}

subprojects {
    group = "github.nwn"
    version = "0.1-SNAPSHOT"
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

}
