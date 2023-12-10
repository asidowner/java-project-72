plugins {
    application
    id("java")
    id("checkstyle")
    id ("jacoco")
    id ("se.patrikerdes.use-latest-versions") version "0.2.18"
    id("com.github.ben-manes.versions") version "0.50.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.3"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("hexlet.code.App")
}

repositories {
    mavenCentral()
}

dependencies {
    // javalin
    implementation("io.javalin:javalin:5.6.3")
    implementation("gg.jte:jte:3.1.6")
    implementation("io.javalin:javalin-rendering:5.6.2")
    implementation("io.javalin:javalin-bundle:5.6.2")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.9")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}