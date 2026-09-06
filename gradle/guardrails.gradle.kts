import com.diffplug.gradle.spotless.SpotlessExtension
import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsTask
import info.solidsoft.gradle.pitest.PitestPluginExtension
import net.ltgt.gradle.errorprone.errorprone
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.PmdExtension
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.sonarqube.gradle.SonarExtension
import java.math.BigDecimal
import java.net.URI

// Self-contained guardrails script plugin. Wire a project to the whole code
// check suite with one line in build.gradle.kts:
//
//     apply(from = "gradle/guardrails.gradle.kts")
//
// Every guardrail plugin loads from the buildscript classpath below, so the
// target build.gradle.kts needs NO plugins block changes and no extra
// dependencies. Project-specific build logic (Spring Boot, frameworks, own
// test deps) stays in the target build file - this script never touches it.
//
// An applied script has no type-safe accessors, so extension configuration is
// written explicitly via extensions.configure<T>(). String notation is used
// for dependency configurations. The buildscript classpath of an applied
// script is private to the script, so external plugin IDs are not resolvable
// through apply(plugin = "...") - the plugins below are applied by CLASS via
// project.pluginManager.apply(...). Built-in plugin IDs (java, checkstyle,
// pmd, jacoco) resolve normally.
//
// The ru.anseranser placeholder is replaced by the setup-guardrails skill
// with the project's deepest common package under src/main/java.

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:7.2.1")
        classpath("com.github.spotbugs.snom:spotbugs-gradle-plugin:6.4.4")
        classpath("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.19.0")
        classpath("org.sonarsource.scanner.gradle:sonarqube-gradle-plugin:6.3.1.5724")
        classpath("net.ltgt.gradle:gradle-errorprone-plugin:4.1.0")
    }
}

apply(plugin = "java")
apply(plugin = "checkstyle")
apply(plugin = "pmd")
apply(plugin = "jacoco")
// External plugins: applied by class - their ids are not resolvable in an
// applied KTS script (the buildscript classpath is private to the script).
project.pluginManager.apply(com.diffplug.gradle.spotless.SpotlessPlugin::class.java)
project.pluginManager.apply(com.github.spotbugs.snom.SpotBugsPlugin::class.java)
project.pluginManager.apply(info.solidsoft.gradle.pitest.PitestPlugin::class.java)
// SonarQube is NOT re-applied when the target project already declares its own
// (e.g. SonarCloud): the plugin versions would differ (script buildscript vs
// project plugins DSL) and a project-owned sonar setup already exists.
val projectHasSonarPlugin = project.pluginManager.hasPlugin("org.sonarqube")
if (!projectHasSonarPlugin) {
    project.pluginManager.apply(org.sonarqube.gradle.SonarQubePlugin::class.java)
}
project.pluginManager.apply(net.ltgt.gradle.errorprone.ErrorPronePlugin::class.java)

// External tool artifacts (palantir-java-format, ktlint-cli, checkstyle, pmd,
// jacoco, findsecbugs, pitest) resolve through the PROJECT repositories, not
// the script buildscript. Ensure mavenCentral is available regardless of the
// target project's own repository setup. Merging is a no-op when the target
// already declares it.
repositories {
    mavenCentral()
}

val guardrailsBasePackage = "ru.anseranser"

dependencies {
    "errorprone"("com.google.errorprone:error_prone_core:2.38.0")
    "spotbugsPlugins"("com.h3xstream.findsecbugs:findsecbugs-plugin:1.14.0")
    "testImplementation"("com.tngtech.archunit:archunit:1.4.0")
}

extensions.configure<CheckstyleExtension>("checkstyle") {
    toolVersion = "10.26.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxErrors = 0
    maxWarnings = 0
}

extensions.configure<PmdExtension>("pmd") {
    toolVersion = "7.12.0"
    ruleSetConfig = resources.text.fromFile("config/pmd/ruleset.xml")
    isIgnoreFailures = false
    reportsDir = file("build/reports/pmd")
}

extensions.configure<SpotlessExtension>("spotless") {
    java {
        palantirJavaFormat("2.76.0")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts", "*.kts")
        ktlint("1.4.0")
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        disableWarningsInGeneratedCode.set(true)
        check("ReferenceEquality", net.ltgt.gradle.errorprone.CheckSeverity.ERROR)
        check("EqualsGetClass", net.ltgt.gradle.errorprone.CheckSeverity.ERROR)
        check("MissingOverride", net.ltgt.gradle.errorprone.CheckSeverity.ERROR)
    }
}

extensions.configure<SpotBugsExtension>("spotbugs") {
    ignoreFailures.set(false)
    effort.set(Effort.MAX)
    reportLevel.set(Confidence.MEDIUM)
    excludeFilter.set(file("config/spotbugs/exclude.xml"))
    reportsDir.set(file("build/reports/spotbugs"))
}

tasks.withType<SpotBugsTask>().configureEach {
    reports {
        create("html") {
            required.set(true)
        }
        create("xml") {
            required.set(true)
        }
    }
}

extensions.configure<JacocoPluginExtension>("jacoco") {
    toolVersion = "0.8.12"
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn("test")
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn("jacocoTestReport")
    violationRules {
        rule {
            limit {
                minimum = BigDecimal.valueOf(0.5)
            }
        }
    }
}

extensions.configure<PitestPluginExtension>("pitest") {
    targetClasses.set(setOf("$guardrailsBasePackage.*"))
    targetTests.set(setOf("$guardrailsBasePackage.*"))
    threads.set(4)
    outputFormats.set(setOf("XML", "HTML"))
    mutators.set(setOf("DEFAULTS"))
    verbose.set(false)
    junit5PluginVersion.set("1.2.1")
}

val sonarJavaSource: String = project.findProperty("guardrails.javaVersion") as String? ?: "21"
val sonarProjectKey: String =
    if (project.group.toString().isNotBlank()) "${project.group}:${project.name}" else project.name

if (!projectHasSonarPlugin) {
    extensions.configure<SonarExtension>("sonar") {
        properties {
            property("sonar.projectKey", sonarProjectKey)
            property("sonar.projectName", project.name)
            property("sonar.host.url", System.getenv("SONAR_HOST_URL") ?: "http://localhost:9000")
            property("sonar.token", sonarToken())
            property("sonar.sourceEncoding", "UTF-8")
            property("sonar.java.source", sonarJavaSource)
            property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
            property("sonar.junit.reportPaths", "build/test-results/test")
            property("sonar.exclusions", "**/build/**,**/.gradle/**,**/.tmp/**,**/src/test/**")
        }
    }
}

tasks.named("check") {
    dependsOn("spotlessCheck", "pmdMain", "spotbugsMain", "jacocoTestCoverageVerification")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// ------------------------------------------------------------------
// Tier tasks: one call = whole level, summary without parsing reports
// ------------------------------------------------------------------

/** PASS / FAIL / SKIPPED from the actual task state in the current run. */
fun taskOutcome(taskName: String): String {
    val task = project.tasks.findByName(taskName) ?: return "SKIPPED"
    val s = task.state
    return when {
        s.failure != null -> "FAIL"
        s.upToDate -> "PASS" // verified on current inputs
        s.skipped -> "SKIPPED" // skipped via onlyIf or after a dependency failure
        s.executed -> "PASS"
        else -> "SKIPPED" // task was not in this run's graph
    }
}

fun countXmlTags(
    file: File,
    tag: String,
): Int {
    if (!file.exists()) return -1
    val content = file.readText()
    return Regex("<$tag\\b").findAll(content).count()
}

/** -1 = no report (tool did not run) -> SKIPPED; 0 -> PASS; >0 -> FAIL. */
fun reportStatus(count: Int): String =
    when {
        count == 0 -> "PASS"
        count > 0 -> "FAIL"
        else -> "SKIPPED"
    }

/** Parses JUnit XML (tests/failures/errors), not just file existence. */
fun junitSuiteOutcome(xmlPath: String): String {
    val xml = file(xmlPath)
    if (!xml.exists()) return "SKIPPED"
    val text = xml.readText()
    val tests =
        Regex("\\btests=\"(\\d+)\"")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull() ?: 0
    val failures =
        Regex("\\bfailures=\"(\\d+)\"")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull() ?: 0
    val errors =
        Regex("\\berrors=\"(\\d+)\"")
            .find(text)
            ?.groupValues
            ?.get(1)
            ?.toIntOrNull() ?: 0
    return when {
        tests == 0 -> "SKIPPED"
        failures > 0 || errors > 0 -> "FAIL"
        else -> "PASS"
    }
}

fun jacocoCoverage(): String {
    val xml = file("build/reports/jacoco/test/jacocoTestReport.xml")
    if (!xml.exists()) return "n/a"
    val text = xml.readText()
    // <counter type="INSTRUCTION" ...> - last one in the file = project total
    val regex = Regex("""<counter type="INSTRUCTION" missed="(\d+)" covered="(\d+)"/>""")
    val all = regex.findAll(text).toList()
    val match = all.lastOrNull() ?: return "n/a"
    val missed = match.groupValues[1].toInt()
    val covered = match.groupValues[2].toInt()
    val total = missed + covered
    if (total == 0) return "n/a"
    val pct = (covered * 100 / total)
    return "$pct% ($covered/$total)"
}

fun tierVerdict(
    name: String,
    statuses: List<String>,
): String =
    when {
        statuses.any { it == "FAIL" } -> "$name FAILED"
        statuses.any { it == "SKIPPED" } -> "$name PARTIAL (not all tools ran)"
        else -> "$name PASSED"
    }

/** Single source of the Sonar token: $SONAR_TOKEN -> .tmp/sonar-token.txt -> empty string. */
fun sonarToken(): String {
    System.getenv("SONAR_TOKEN")?.takeIf { it.isNotBlank() }?.let { return it }
    val tokenFile = file(".tmp/sonar-token.txt")
    if (tokenFile.exists()) {
        val token = tokenFile.readText().removePrefix("\uFEFF").trim()
        if (token.isNotBlank()) return token
    }
    return ""
}

fun resolveOnPath(executable: String): String? {
    val exeName = if (System.getProperty("os.name").startsWith("Windows")) "$executable.exe" else executable
    return System.getenv("PATH")?.split(File.pathSeparator)?.firstNotNullOfOrNull { dir ->
        File(dir, exeName).takeIf { it.isFile }?.absolutePath
    }
}

/** Find a binary in the winget package install dir (gitleaks/osv-scanner are installed outside PATH). */
fun resolveWingetPackage(
    pkgPrefix: String,
    exeName: String,
): String? {
    val localAppData = System.getenv("LOCALAPPDATA") ?: return null
    val pkgDir = File(localAppData, "Microsoft/WinGet/Packages")
    if (!pkgDir.isDirectory) return null
    return pkgDir.listFiles()?.firstNotNullOfOrNull { dir ->
        if (dir.isDirectory && dir.name.startsWith(pkgPrefix)) {
            val exe = File(dir, exeName)
            if (exe.isFile) exe.absolutePath else null
        } else {
            null
        }
    }
}

/** gitleaks: PATH -> winget fallback (same logic as in config/githooks/pre-commit). */
fun resolveGitleaksBinary(): String? = resolveOnPath("gitleaks") ?: resolveWingetPackage("Gitleaks.Gitleaks_", "gitleaks.exe")

/** osv-scanner: PATH -> winget fallback (winget package Google.OSVScanner). */
fun resolveOsvScannerBinary(): String? = resolveOnPath("osv-scanner") ?: resolveWingetPackage("Google.OSVScanner_", "osv-scanner.exe")

tasks.register("gitleaksDetect", Exec::class) {
    group = "verification"
    description = "Gitleaks scan of the working tree (secrets). SKIPPED if gitleaks is not installed."
    val binary = resolveGitleaksBinary()
    onlyIf { binary != null }
    if (binary != null) {
        commandLine(binary, "detect", "--source", ".", "--no-banner")
    }
}

tasks.register("osvScan", Exec::class) {
    group = "verification"
    description =
        "OSV-Scanner scan of dependency lockfiles (vulnerabilities, osv-scanner.toml baseline). SKIPPED if osv-scanner is not installed."
    val binary = resolveOsvScannerBinary()
    onlyIf { binary != null }
    if (binary != null) {
        doFirst {
            val lockfiles =
                listOf(
                    "gradle.lockfile",
                    "package-lock.json",
                    "yarn.lock",
                    "pnpm-lock.yaml",
                    "Cargo.lock",
                    "poetry.lock",
                    "composer.lock",
                    "go.sum",
                )
            val found = lockfiles.any { file(it).exists() }
            if (!found) {
                throw GradleException(
                    "osvScan: no dependency lockfile found. For Gradle run './gradlew dependencies --write-locks' and commit gradle.lockfile, then re-run.",
                )
            }
        }
        // Absolute path - a relative "." on Windows resolves against the daemon cwd, not the project.
        commandLine(binary, "scan", "source", "--recursive", layout.projectDirectory.asFile.absolutePath)
    }
}

/** Extensions where Russian (Cyrillic) text is allowed: documentation and resources. */
val russianAllowedExtensions = setOf("md", "txt", "json", "java", "html")

/** Repo-relative paths skipped by languageCheck: generated Gradle wrapper scripts keep (C) by design. */
val languageCheckSkipPaths = setOf("gradlew", "gradlew.bat")

// Enumerate tracked + untracked (non-gitignored) files via git, respecting .gitignore.
// Explicit --git-dir/--work-tree and a temp-file redirect (instead of a process pipe)
// are required: on Windows the daemon's pipe read of a child process can silently
// truncate the listing (observed: src/** files dropped, 21 of 54 lines survived).
fun listGitFiles(): List<String> {
    val listFile = File.createTempFile("guardrails-ls", ".txt")
    try {
        val gitDirArg = "--git-dir=${rootDir.resolve(".git").absolutePath.replace('\\', '/')}"
        val workTreeArg = "--work-tree=${rootDir.absolutePath.replace('\\', '/')}"
        val proc =
            ProcessBuilder("git", gitDirArg, workTreeArg, "ls-files", "--cached", "--others", "--exclude-standard")
                .directory(rootDir)
                .redirectErrorStream(true)
                .redirectOutput(listFile)
                .start()
        val finished = proc.waitFor(60, java.util.concurrent.TimeUnit.SECONDS)
        if (!finished || proc.exitValue() != 0) {
            throw GradleException("git ls-files failed (exit=${if (finished) proc.exitValue() else "timeout"}) - cannot enumerate files for languageCheck")
        }
        return listFile.readLines()
    } finally {
        listFile.delete()
    }
}

tasks.register("languageCheck") {
    group = "verification"
    description = "English-only for all files except .md/.txt/.json (RU allowed there). Uses git ls-files, skips gitignored/binary files."
    doLast {
        val files = listGitFiles()
        val violations = mutableListOf<String>()
        for (rel in files) {
            if (rel.isBlank()) continue
            if (rel.startsWith("build/") || rel.startsWith(".gradle/") || rel.startsWith(".tmp/") || rel.startsWith("plans/")) continue // artifacts/scratch, not source
            if (rel in languageCheckSkipPaths) continue // wrapper scripts (by-design (C)), exclude via languageCheckSkipPaths
            val f = file(rel)
            if (!f.isFile || f.length() > 1_048_576L) continue // skip dirs, large/binary candidates
            val bytes = f.readBytes()
            if (bytes.any { it == 0.toByte() }) continue // binary (e.g. gradle-wrapper.jar)
            val text = String(bytes, Charsets.UTF_8)
            val russianAllowed = f.extension.lowercase() in russianAllowedExtensions
            var reported = false
            text.forEachIndexed { idx, ch ->
                val cp = ch.code
                val isAscii = cp < 0x80
                val isCyrillic = cp in 0x0400..0x04FF // Russian + the whole Cyrillic block
                val isNumero = cp == 0x2116 // numero sign, used in Russian typography
                // Punctuation, typography and box-drawing glyphs that commonly accompany Russian docs
                // and ASCII-art directory trees (em-dash, arrows, guillemets, bullets, tree corners,
                // horizontal/vertical/tee lines, cell triples). Allowed only in .md/.txt/.json.
                val docAllowed =
                    setOf(
                        0x2013,
                        0x2014,
                        0x2190,
                        0x2192,
                        0x00AB,
                        0x00BB, // en-dash, em-dash, <-, ->, guillemets
                        0x00B7,
                        0x2022,
                        0x2026, // middle dot, bullet, ellipsis
                        0x2500,
                        0x2502,
                        0x250C,
                        0x2510,
                        0x2514,
                        0x2518, // box: -, |, corners
                        0x251C,
                        0x2524,
                        0x252C,
                        0x2534,
                        0x253C,
                        0x25B6, // box: tees and cross
                    )
                val ok = isAscii || (russianAllowed && (isCyrillic || isNumero || cp in docAllowed))
                if (!ok && !reported) {
                    // NOTE: `break` would exit the outer file loop (inline lambda), so record
                    // the first offending char per file and keep scanning the remaining files.
                    violations += "$rel:${idx + 1}: non-English char U+${cp.toString(16).uppercase()} ('$ch')"
                    reported = true
                }
            }
        }
        if (violations.isEmpty()) {
            println("Language: PASS (English-only outside .md/.txt/.json, RU allowed in docs)")
        } else {
            violations.forEach { println("  $it") }
            throw GradleException("languageCheck FAILED: ${violations.size} file(s) with non-English characters")
        }
    }
}

tasks.register("installGitHooks", Exec::class) {
    group = "other"
    description = "Install git hooks from config/githooks/ (sets core.hooksPath). Run once per clone."
    commandLine("git", "config", "core.hooksPath", "config/githooks")
}

tasks.register("tier1Summary") {
    group = "verification"
    description = "Tier 1 summary - always runs, even if Tier 1 failed"
    doLast {
        val csMain = countXmlTags(file("build/reports/checkstyle/main.xml"), "error")
        val csTest = countXmlTags(file("build/reports/checkstyle/test.xml"), "error")
        // spotlessCheck is an aggregate (lifecycle) task: its TaskState does not reflect
        // failures of children, so read the leaf spotlessJavaCheck / spotlessKotlinGradleCheck.
        val spotlessJava = taskOutcome("spotlessJavaCheck")
        val spotlessKotlin = taskOutcome("spotlessKotlinGradleCheck")
        val checkstyleMain = reportStatus(csMain)
        val checkstyleTest = reportStatus(csTest)
        val errorprone = taskOutcome("compileJava")
        val gitleaks = taskOutcome("gitleaksDetect")
        val language = taskOutcome("languageCheck")
        println("\n=== Tier 1 Summary ===")
        println("Spotless (Palantir): java=$spotlessJava, kotlin-gradle=$spotlessKotlin")
        println(
            "Checkstyle: main=${if (csMain == -1) "no report" else csMain} ($checkstyleMain)," +
                " test=${if (csTest == -1) "no report" else csTest} ($checkstyleTest)",
        )
        println("ErrorProne (compileJava): $errorprone")
        println("Gitleaks (detect --source .): $gitleaks")
        println("Language (RU only in .md/.txt/.json): $language")
        println("ArchUnit: via test (see Tier 2)")
        println(tierVerdict("Tier 1", listOf(spotlessJava, spotlessKotlin, checkstyleMain, checkstyleTest, errorprone, gitleaks, language)))
    }
}

tasks.register("tier1") {
    group = "verification"
    description = "Tier 1 - fast pre-commit: Spotless, Checkstyle, ErrorProne (compileJava), Gitleaks detect, Language check"
    dependsOn("spotlessCheck", "checkstyleMain", "checkstyleTest", "compileJava", "gitleaksDetect", "languageCheck")
    finalizedBy("tier1Summary")
}

tasks.named("spotlessCheck") { finalizedBy("tier1Summary") }
tasks.named("checkstyleMain") { finalizedBy("tier1Summary") }
tasks.named("checkstyleTest") { finalizedBy("tier1Summary") }
// compileJava does NOT finalize tier1Summary: it is a transitive dependency of every
// verification task (pmd/spotbugs/test), otherwise tier1Summary would print on each tier2/tier3/check.
// ErrorProne status is read from the compileJava state inside tier1Summary.
tasks.named("gitleaksDetect") { finalizedBy("tier1Summary") }
tasks.named("languageCheck") { finalizedBy("tier1Summary") }

tasks.register("tier2Summary") {
    group = "verification"
    description = "Tier 2 summary - always runs"
    doLast {
        val pmdMain = countXmlTags(file("build/reports/pmd/main.xml"), "violation")
        val pmdTest = countXmlTags(file("build/reports/pmd/test.xml"), "violation")
        val sbMain = countXmlTags(file("build/reports/spotbugs/main.xml"), "BugInstance")
        val sbTest = countXmlTags(file("build/reports/spotbugs/test.xml"), "BugInstance")
        val archUnitXml = "build/test-results/test/TEST-$guardrailsBasePackage.ArchitectureTest.xml"
        val archUnit = junitSuiteOutcome(archUnitXml)
        val jacoco = taskOutcome("jacocoTestCoverageVerification")
        val tests = taskOutcome("test")
        val cov = jacocoCoverage()
        val osv = taskOutcome("osvScan")
        println("\n=== Tier 2 Summary ===")
        println(
            "PMD: main=${if (pmdMain == -1) "no report" else pmdMain} (${reportStatus(pmdMain)})," +
                " test=${if (pmdTest == -1) "no report" else pmdTest} (${reportStatus(pmdTest)})",
        )
        println(
            "SpotBugs: main=${if (sbMain == -1) "no report" else sbMain} (${reportStatus(sbMain)})," +
                " test=${if (sbTest == -1) "no report" else sbTest} (${reportStatus(sbTest)})",
        )
        println("ArchUnit: $archUnit (8 rules, via test)")
        println("JaCoCo: coverage $cov (gate 0.5) - $jacoco")
        println("Tests (JUnit): $tests")
        println("OSV-Scanner (deps vulns, osv-scanner.toml baseline): $osv")
        println(
            tierVerdict(
                "Tier 2",
                listOf(
                    reportStatus(pmdMain),
                    reportStatus(pmdTest),
                    reportStatus(sbMain),
                    reportStatus(sbTest),
                    archUnit,
                    jacoco,
                    tests,
                    osv,
                ),
            ),
        )
    }
}

tasks.register("tier2") {
    group = "verification"
    description = "Tier 2 - CI fast: PMD, SpotBugs, ArchUnit, JaCoCo, OSV-Scanner (deps vulns)"
    dependsOn("pmdMain", "pmdTest", "spotbugsMain", "spotbugsTest", "test", "jacocoTestReport", "jacocoTestCoverageVerification", "osvScan")
    finalizedBy("tier2Summary")
}

tasks.named("pmdMain") { finalizedBy("tier2Summary") }
tasks.named("pmdTest") { finalizedBy("tier2Summary") }
tasks.named("spotbugsMain") { finalizedBy("tier2Summary") }
tasks.named("spotbugsTest") { finalizedBy("tier2Summary") }
tasks.named("test") { finalizedBy("tier2Summary") }
tasks.named("jacocoTestReport") { finalizedBy("tier2Summary") }
tasks.named("jacocoTestCoverageVerification") { finalizedBy("tier2Summary") }
tasks.named("osvScan") { finalizedBy("tier2Summary") }

fun pitRatio(): String {
    val xml = file("build/reports/pitest/mutations.xml")
    if (!xml.exists()) return "no report"
    val text = xml.readText()
    // PIT writes detected='true' (single quotes) - match both styles
    val killed = Regex("""detected=(['"])true\1""").findAll(text).count()
    val total = Regex("<mutation ").findAll(text).count()
    return "$killed/$total killed"
}

fun sonarStatus(): String {
    if (projectHasSonarPlugin) {
        return "SKIPPED (SonarQube configured by the project itself - run ./gradlew sonar)"
    }
    val host = System.getenv("SONAR_HOST_URL") ?: "http://localhost:9000"
    val token = sonarToken()
    return try {
        val url = URI("$host/api/qualitygates/project_status?projectKey=$sonarProjectKey").toURL()
        val conn = url.openConnection() as java.net.HttpURLConnection
        conn.connectTimeout = 3000
        conn.readTimeout = 3000
        if (token.isNotEmpty()) conn.setRequestProperty("Authorization", "Bearer $token")
        when (conn.responseCode) {
            200 -> {
                val body = conn.inputStream.bufferedReader().readText()
                val gate = Regex("\"status\":\"(\\w+)\"").find(body)?.groupValues?.get(1) ?: "UNKNOWN"
                when (gate) {
                    "OK" -> "PASS (quality gate OK)"
                    "ERROR" -> "FAIL (quality gate ERROR)"
                    else -> "UNKNOWN (gate=$gate)"
                }
            }
            401, 403 -> "SKIPPED (no/invalid token - SONAR_TOKEN or .tmp/sonar-token.txt)"
            else -> "FAIL (HTTP ${conn.responseCode})"
        }
    } catch (e: Exception) {
        "SKIPPED (SonarQube not reachable at $host - docker compose up -d)"
    }
}

tasks.register("tier3") {
    group = "verification"
    description = "Tier 3 - heavy/nightly: PIT (SonarQube is a separate ./gradlew sonar task, status in summary)"
    dependsOn("pitest")
    finalizedBy("tier3Summary")
}

tasks.named("pitest") { finalizedBy("tier3Summary") }

tasks.register("tier3Summary") {
    group = "verification"
    description = "Tier 3 summary - always runs"
    doLast {
        val pitTask = taskOutcome("pitest")
        val sonar = sonarStatus()
        println("\n=== Tier 3 Summary ===")
        println("PIT: ${pitRatio()} (task $pitTask)")
        println("SonarQube: $sonar")
        if (!projectHasSonarPlugin) {
            println("  (standalone: docker compose up -d && SONAR_TOKEN=xxx ./gradlew sonar)")
        } else {
            println("  (run ./gradlew sonar with the project's own SonarQube/SonarCloud config)")
        }
        val sonarFlag =
            when {
                sonar.startsWith("PASS") -> "PASS"
                sonar.startsWith("FAIL") -> "FAIL"
                else -> "SKIPPED"
            }
        println(tierVerdict("Tier 3", listOf(pitTask, sonarFlag)))
    }
}

tasks.register("tiers") {
    group = "verification"
    description = "All tiers summary (Tier 1 + Tier 2 + Tier 3)"
    dependsOn("tier1", "tier2", "tier3")
    doLast {
        println("\n=== All Tiers Summary ===")
        println("Tier 1: Spotless, Checkstyle, ErrorProne, Gitleaks")
        println("Tier 2: PMD, SpotBugs, ArchUnit, JaCoCo, OSV-Scanner")
        println("Tier 3: PIT (SonarQube - separate, standalone)")
        println("Run each tier separately for details: ./gradlew tier1 --continue, ./gradlew tier2 --continue, ./gradlew tier3")
    }
}