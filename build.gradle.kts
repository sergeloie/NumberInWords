val lombokVersion = "1.18.46"
val javacrumbsVersion = "3.4.1"
val swaggerVersion = "2.8.6"

plugins {
    application
    jacoco
    checkstyle
    id("org.springframework.boot") version "3.5.16"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.3.1.8318"

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
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:$javacrumbsVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
    }
}
