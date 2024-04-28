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
}