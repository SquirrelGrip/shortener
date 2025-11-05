plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.jpa") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "3.5.7"
}

group = "com.github.squirrelgrip"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val testAgent: Configuration by configurations.creating

dependencies {
    implementation("com.h2database:h2:2.4.240")
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.2.21")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.13")
    implementation("org.springframework.boot:spring-boot-starter-web:3.5.7")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.5.7")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.5.7")
    implementation("org.springframework.data:spring-data-jpa:3.5.5")

    testImplementation("org.assertj:assertj-core:3.27.6")
    testImplementation("org.awaitility:awaitility:4.3.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.14.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.14.1")
    testImplementation("org.mockito:mockito-core:5.20.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.20.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.7")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.14.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.14.1")

    testAgent("net.bytebuddy:byte-buddy-agent:1.17.8")
}

tasks.test {
    useJUnitPlatform()
    jvmArgumentProviders.add {
        testAgent.incoming.files.map { "-javaagent:${it.absolutePath}" } +
                "-XX:+EnableDynamicAgentLoading"
    }
}

kotlin {
    jvmToolchain(21)
}