plugins {
    id("maven-publish")
}

tasks.named<Jar>("shadowJar") {
    destinationDirectory.set(rootProject.file("target-api"))
    archiveFileName.set("${rootProject.name}-API-${rootProject.version}.jar")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.named("shadowJar"))
            groupId = rootProject.group.toString()
            artifactId = "api"
            version = rootProject.version.toString()
        }
    }
}
