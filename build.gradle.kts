plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.0.0"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("re.alwyn974.groupez.repository") version "1.0.0"
}

val targetFolder: File = rootProject.file("target")
val apiFolder: File = rootProject.file("target-api")

allprojects {
    group = "fr.traqueur"
    version = rootProject.property("version") as String

    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "re.alwyn974.groupez.repository")

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.groupez.dev/releases")
        maven("https://repo.groupez.dev/snapshots")
    }

    dependencies {
        compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

        // Runtime libraries — resolved at runtime by MoreBeesLoader (see plugin.yml `libraries:`).
        compileOnly("fr.traqueur.commands:platform-spigot:5.1.1")
        compileOnly("fr.traqueur:recipesapi:3.2.1")
        compileOnly("fr.traqueur.structura:structura-core:2.0.0")
    }

    java {
        toolchain.languageVersion = JavaLanguageVersion.of(21)
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    tasks.named<Jar>("shadowJar") {
        archiveBaseName.set(rootProject.name)
        archiveVersion.set(rootProject.version.toString())
        archiveClassifier.set("")
    }

    tasks.named("build") {
        dependsOn("shadowJar")
    }
}

repositories {
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://maven.devs.beer/")
    maven("https://repo.oraxen.com/releases")
    maven("https://repo.nexomc.com/releases")
}

dependencies {
    implementation(project(":api"))

    // Shaded into the jar and relocated to avoid clashes.
    implementation("org.bstats:bstats-bukkit:3.1.0")

    // Optional plugin integrations — wired via the Hooks enum, present only if the plugin is installed.
    compileOnly("com.ticxo.modelengine:ModelEngine:R4.0.8")
    compileOnly("dev.lone:api-itemsadder:4.0.10")
    compileOnly("io.th0rgal:oraxen:1.191.0")
    compileOnly("com.nexomc:nexo:1.9.0")
}

tasks {
    shadowJar {
        destinationDirectory.set(targetFolder)
        relocate("org.bstats", "fr.traqueur.morebees.libs.bstats")
    }

    runServer {
        minecraftVersion("1.21.4")
    }

    processResources {
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    register<Delete>("cleanTarget") {
        delete(targetFolder)
        delete(apiFolder)
    }

    named("clean") {
        dependsOn("cleanTarget")
    }
}
