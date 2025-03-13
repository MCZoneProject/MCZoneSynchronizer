import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.spongepowered.asm.gradle.plugins.MixinExtension
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.minecraftforge.net")
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
    dependencies {
        classpath("net.minecraftforge.gradle", "ForgeGradle", "6.0.+")
        classpath("org.spongepowered", "mixingradle", "0.7-SNAPSHOT")
        classpath("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.7.21")
        classpath("org.jetbrains.kotlin", "kotlin-serialization", "1.7.21")
    }
}

apply(plugin = "kotlin")
apply(plugin = "kotlinx-serialization")
apply(plugin = "net.minecraftforge.gradle")
apply(plugin = "org.spongepowered.mixin")

group = getExtra("mod_group_id")
version = getExtra("mod_version")

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

minecraft {
    mappings(getExtra("mapping_channel"), getExtra("mapping_version"))

    copyIdeResources.set(true)

    runs {
        create("client") {
            workingDirectory(project.file("run/client"))
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")
        }
    }
}

configurations {
    minecraftLibrary {
        exclude("org.jetbrains", "annotations")
    }
    create("shade")
    getByName("implementation").extendsFrom(getByName("shade"))
}

val Project.mixin: MixinExtension get() = extensions.getByType()

mixin.run {
    add(sourceSets.main.get(), "${getExtra("mod_id")}.refmap.json")
    config("${getExtra("mod_id")}.mixins.json")
}

dependencies {
    minecraft("net.minecraftforge", "forge", "${getExtra("minecraft_version")}-${getExtra("forge_version")}")
    annotationProcessor("org.spongepowered", "mixin", "0.8.5", classifier = "processor")
    add("shade", "com.zaxxer:HikariCP:5.0.1")
    testImplementation(kotlin("test"))
}

sourceSets.main.configure {
    resources.srcDirs("src/generated/resources/")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${getExtra("mod_group_id")}.${getExtra("mod_id")}"
            artifactId = getExtra("mod_name")
            version = getExtra("mod_version")
            from(components["java"])
        }
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    shadowJar {
        archiveBaseName.set(getExtra("mod_id"))
        archiveClassifier.set("")
        destinationDirectory.set(File("D:\\서버\\1.20.1 - 마크존(본섭)\\2. 로비\\mods"))
        manifest {
            val map = mutableMapOf<String, String>()
            map["Specification-Title"] = getExtra("mod_id")
            map["Specification-Vendor"] = getExtra("mod_authors")
            map["Specification-Version"] = "1"
            map["Implementation-Title"] = project.name
            map["Implementation-Version"] = project.version.toString()
            map["Implementation-Vendor"] = getExtra("mod_authors")
            map["Implementation-Timestamp"] = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            attributes(map)
        }
        configurations = listOf(project.configurations.getByName("shade"))
        dependencies {
            exclude("org/slf4j/**")
        }
        /*relocate2("org.slf4j")
        relocate2("com.google.gson")*/
        finalizedBy("reobfShadowJar")
    }
    assemble {
        dependsOn("shadowJar")
    }
    reobf {
        create("shadowJar")
    }
}

fun ShadowJar.relocate2(group: String) {
    relocate(group, "${project.group}.$group")
}

fun getExtra(key: String): String {
    return extra[key]?.toString() ?: throw NullPointerException("$key is not exists.")
}