import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation
plugins {
    kotlin("jvm") version "1.6.0"
    id("kr.entree.spigradle") version "1.2.4"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "me.jantuck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven(url = "https://papermc.io/repo/repository/maven-public/")
    maven(url = "https://repo.aikar.co/content/groups/aikar/")
    maven(url = "https://repo.mineinabyss.com/releases")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("co.aikar", "acf-paper", "0.5.0-SNAPSHOT")
    implementation("com.okkero", "skedule", "1.2.8")
    compileOnly("com.destroystokyo.paper", "paper-api", "1.16.1-R0.1-SNAPSHOT")
}
spigot {
    authors = listOf("JanTuck")
    apiVersion = "1.13"
}

val autoRelocate by tasks.register<ConfigureShadowRelocation>("configureShadowRelocation", ConfigureShadowRelocation::class) {
    target = tasks.getByName("shadowJar") as ShadowJar?
    val packageName = "${project.group}.${project.name.toLowerCase()}"
    prefix = "$packageName.shaded"
}

tasks {
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<ShadowJar> {
        archiveClassifier.set("")
        dependsOn(autoRelocate)
        minimize()
    }
}
