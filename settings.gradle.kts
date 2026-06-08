pluginManagement {
    repositories {
        maven {
            name = "groupezReleases"
            url = uri("https://repo.groupez.dev/releases")
        }
        maven {
            name = "groupezSnapshots"
            url = uri("https://repo.groupez.dev/snapshots")
        }
        gradlePluginPortal()
    }
}

rootProject.name = "MoreBees"

include("api")
