pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SadoTracker"
include(":app")
include(":core:core-database")
include(":core:core-domain")
include(":core:core-data")
include(":core:core-ui")
include(":feature:feature-workout")
include(":feature:feature-programs")
include(":feature:feature-you")
