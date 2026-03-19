# Qualimetry Gherkin Analyzer - IntelliJ Plugin

Static analysis of Cucumber Gherkin `.feature` files in IntelliJ IDEA, Rider, and other JetBrains IDEs, with Qodana support for headless CI/CD analysis.

Powered by the same analysis engine as the [Qualimetry Gherkin Analyzer for VS Code](https://marketplace.visualstudio.com/items?itemName=qualimetry.qualimetry-vscode-gherkin-plugin) and the [Qualimetry Gherkin Analyzer for SonarQube](https://github.com/Qualimetry/sonarqube-gherkin-plugin).

## Features

- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Real-time diagnostics** as you edit `.feature` files.
- **Configurable** — enable/disable individual rules and override severities under Settings > Tools > Qualimetry Gherkin Analyzer.
- **SonarQube import** — import active rules from a SonarQube quality profile via Tools > Qualimetry Gherkin > Import Rules from SonarQube.
- **Default quality profile** — 53 rules active out of the box for immediate value.
- **Qodana support** — runs automatically in JetBrains Qodana for quality gates in CI/CD.

## Rule categories

| Category | Examples |
|----------|----------|
| Structure | Feature/scenario/step required, naming conventions |
| Design | Step ordering, single When, background best practices |
| Style & Convention | Indentation, trailing whitespace, line endings, spelling |
| Tags | Naming patterns, placement, duplicates, restrictions |
| Variables & Data | Unused variables, Examples column coverage |
| Step Patterns | Given/When/Then patterns, unknown step types |
| Comments & Markers | TODO, FIXME, comment patterns |
| Structural Integrity | Outline placeholders, Examples required, blank lines |
| Configurable Thresholds | Max scenarios, max steps, max tags, max lines |

## Installation

### From JetBrains Marketplace

1. Open **Settings > Plugins > Marketplace**.
2. Search for **Qualimetry Gherkin Analyzer**.
3. Click **Install** and restart.

### From source

```bash
# The shared engine must be installed to Maven local first
cd <monorepo-root>
mvn clean install -pl gherkin-analyzer

# Then build the IntelliJ plugin
cd intellij-plugin
./gradlew buildPlugin
```

The plugin ZIP is produced in `build/distributions/`.

## Configuration

After installation, configure the analyzer under **Settings > Tools > Qualimetry Gherkin Analyzer**:

- **Enable/disable** the analyzer globally.
- **Per-rule table** — enable/disable individual rules, set severity overrides, filter by name or key.
- **Reset to Defaults** — clear all overrides and return to the default profile.
- **Per-rule overrides** are stored in `qualimetry-gherkin.xml`.

### Import from SonarQube

Use **Tools > Qualimetry Gherkin > Import Rules from SonarQube** to fetch active rules from a SonarQube quality profile. Enter your server URL, an optional authentication token, and an optional profile name. The imported rules replace the current configuration. The server URL and profile name are remembered between sessions.

## Also available

The same analysis engine powers plugins for other platforms:

- **[VS Code extension](https://github.com/Qualimetry/vscode-gherkin-plugin)** — catch issues as you type in VS Code.
- **[SonarQube plugin](https://github.com/Qualimetry/sonarqube-gherkin-plugin)** — enforce quality gates in CI/CD pipelines.

Rule keys and severities align across all three tools so findings are directly comparable.

## Requirements

- IntelliJ IDEA 2024.3 or later (any JetBrains IDE based on the IntelliJ Platform).
- JDK 17+ runtime.

## License

Apache License 2.0. See [LICENSE](LICENSE) for details.
