import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    mavenCentral()
    maven {
        name = "clojars.org"
        url = uri("https://repo.clojars.org")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation(project(mapOf("path" to ":common")))
    implementation(project(mapOf("path" to ":kotlin-python-interop")))
    implementation("net.clojars.suuft:libretranslate-java:1.0.5")
    implementation("org.languagetool:language-en:6.3")
    implementation ("org.jetbrains.kotlinx:kotlin-deeplearning-onnx:0.5.2")
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

tasks.withType<Test> {
    useJUnitPlatform()
}
