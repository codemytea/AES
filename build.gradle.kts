import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins{
    id("org.jlleitschuh.gradle.ktlint") version "12.1.0" apply false
    kotlin("jvm") version "1.9.23" apply false
}

val localProperties = java.util.Properties()
val credentialsFile = rootProject.file("credentials.properties")
val keys = if (credentialsFile.exists()) {
    println("Credentials Found")
    localProperties.load(java.io.FileInputStream(credentialsFile))
    (localProperties["OPENAI_API_KEY"] as? String?) to (localProperties["TREFLE_API_KEY"] as? String?)
} else (null to null)

subprojects {
    tasks.withType<Test>{
        keys.first?.let {
            this.jvmArgs("-Dcom.aes.kotlinpythoninterop.openAIApiKey=$it")
        }
        keys.second?.let{
            this.jvmArgs("-Dcom.aes.expertsystem.trefle.apiKey=$it")
        }
    }
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    // Configure the ktLint plugin.
    configure<KtlintExtension> {
        version.set("1.2.1")
        reporters { reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML) }
    }
}