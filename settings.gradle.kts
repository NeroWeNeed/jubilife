rootProject.name = "jubilife"
include("jubilife-database")
include("jubilife-database-commands")
include("jubilife-dashboard")
include("jubilife-pokemon-showdown-replay-analyzer")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("kotlin-exposed-version", "0.38.2")
            library("kotlin-exposed-core", "org.jetbrains.exposed", "exposed-core").versionRef("kotlin-exposed-version")
            library("kotlin-exposed-dao", "org.jetbrains.exposed", "exposed-dao").versionRef("kotlin-exposed-version")
            library("kotlin-exposed-jdbc", "org.jetbrains.exposed", "exposed-jdbc").versionRef("kotlin-exposed-version")
            bundle("kotlin-exposed", listOf("kotlin-exposed-core", "kotlin-exposed-dao", "kotlin-exposed-jdbc"))
        }
    }
}
