plugins {
    application
    jacoco
    checkstyle
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.sonarqube") version "4.4.1.3373"

}

group = "ru.anseranser"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation(platform("org.junit:junit-bom:5.11.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.4.1")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.11.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

sonar {
    properties {
        property("sonar.projectKey", "sergeloie_NumberInWords")
        property("sonar.organization", "sergeloie")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
