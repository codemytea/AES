import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.*

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    kotlin("plugin.jpa") version "1.9.21"
}

group = "com.aes"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    maven {
        name = "clojars.org"
        url = uri("https://repo.clojars.org")
    }
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:3.1.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("se.akerfeldt:okhttp-signpost:1.1.0")
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.27")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("oauth.signpost:signpost-core:2.1.1")
    implementation("org.json:json:20231013")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("net.clojars.suuft:libretranslate-java:1.0.5")
    implementation(project(mapOf("path" to ":common")))
    implementation(project(mapOf("path" to ":kotlin-python-interop")))
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

val localProperties = Properties()
val credentialsFile = rootProject.file("credentials.properties")
val key = if (credentialsFile.exists()) {
    localProperties.load(FileInputStream(credentialsFile))
    (localProperties["GATEWAY_API_KEY"] as? String)?.also{
        println("Credentials found")
    }
} else null
tasks.withType<Test> {
    useJUnitPlatform()
    tasks.withType<Test> {
        key?.let {
            this.jvmArgs("-Dcom.aes.smsservice.gatewayApiKey=$it")
        }
    }
}


