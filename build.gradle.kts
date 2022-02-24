repositories {
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.3.2")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", "1.3.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
