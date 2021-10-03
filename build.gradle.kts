import org.apache.commons.io.output.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    id("au.com.dius.pact") version "4.1.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("au.com.dius.pact.provider:junit5:4.2.13")
    testImplementation("au.com.dius.pact.consumer:junit5:4.2.13")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun getGitBranch(): String {
    val stream = ByteArrayOutputStream()
    project.exec {
        commandLine = "git rev-parse --abbrev-ref HEAD".split(" ")
        standardOutput = stream
    }
    return String(stream.toByteArray()).trim()
}

pact {
    publish {
        pactBrokerUrl = System.getenv("PACT_BROKER_BASE_URL")
        pactBrokerUsername = System.getenv("PACT_BROKER_USERNAME")
        pactBrokerPassword = System.getenv("PACT_BROKER_PASSWORD")
        tags = listOf(getGitBranch())
        version = getGitBranch()
    }
}

