plugins {
    kotlin("jvm") version "1.8.21"
    application
}

group = "dev.teapot"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("software.coley", "lljzip", "1.6.1")
    implementation("software.coley:lljzip:1.6.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("MainKt")
}