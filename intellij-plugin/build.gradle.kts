plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = "com.qualimetry.intellij"
version = providers.gradleProperty("pluginVersion").getOrElse("1.3.3")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
    mavenLocal()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
    }

    implementation("com.qualimetry.sonar:gherkin-analyzer:1.3.3")

    implementation("org.sonarsource.api.plugin:sonar-plugin-api:11.1.0.2693")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}

intellijPlatform {
    pluginConfiguration {
        id = "com.qualimetry.gherkin"
        name = "Qualimetry Gherkin Analyzer"
        version = project.version.toString()
        description = """
            <p>Static analysis of Cucumber Gherkin feature files (<code>.feature</code>)
            in IntelliJ IDEA, Rider, and other JetBrains IDEs. Also runs in JetBrains Qodana.</p>
            <p>Powered by the same engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.</p>
            <ul>
              <li>83 rules covering structure, design, style, tags, variables, and spelling</li>
              <li>Real-time analysis as you edit</li>
              <li>Works in IntelliJ IDEA, Rider, and JetBrains Qodana</li>
            </ul>
        """.trimIndent()
        vendor {
            name = "Qualimetry"
            url = "https://qualimetry.com"
        }
        ideaVersion {
            sinceBuild = "243"
            untilBuild = provider { null }
        }
    }

    publishing {
        token = providers.environmentVariable("JETBRAINS_MARKETPLACE_TOKEN")
    }

    signing {
        certificateChain = providers.environmentVariable("PLUGIN_SIGNING_CERTIFICATE_CHAIN")
        privateKey = providers.environmentVariable("PLUGIN_SIGNING_KEY")
        password = providers.environmentVariable("PLUGIN_SIGNING_KEY_PASSWORD")
    }
}

tasks {
    buildSearchableOptions {
        enabled = false
    }
}
