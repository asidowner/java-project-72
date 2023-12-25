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
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.1")
    // https://search.maven.org/artifact/com.h2database/h2/2.2.224/jar
    implementation("com.h2database:h2:2.2.224")
    // https://search.maven.org/artifact/com.zaxxer/HikariCP/5.1.0/jar?eh=
    implementation("com.zaxxer:HikariCP:5.1.0")
    // https://mvnrepository.com/artifact/com.konghq/unirest-java
    implementation("com.konghq:unirest-java:3.14.5")
    // test
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/mockwebserver
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}