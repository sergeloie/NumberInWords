val lombokVersion = "1.18.48"
val javacrumbsVersion = "6.2.0"
val swaggerVersion = "3.1.0"

plugins {
    application
    jacoco
    checkstyle
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.3.1.8318"
}

// Boot 4.1.1 manages Tomcat 11.0.24; osv flags 3 CVEs fixed in 11.0.25.
extra["tomcat.version"] = "11.0.25"
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
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$swaggerVersion")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
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

// Guardrails: tier1-3 checks (Spotless, Checkstyle, PMD, SpotBugs, ErrorProne,
// ArchUnit, JaCoCo, PIT, Gitleaks, OSV-Scanner, languageCheck). See
// docs/agents/guardrails.md. SonarQube is NOT touched here - the project has
// its own SonarCloud config above, the script detects it via hasPlugin.
apply(from = "gradle/guardrails.gradle.kts")

// Lockfile for reproducible builds + full osvScan (regenerate: ./gradlew
// dependencies --write-locks).
dependencyLocking {
    lockAllConfigurations()
}
