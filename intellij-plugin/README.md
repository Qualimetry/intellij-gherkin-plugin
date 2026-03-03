# Qualimetry Gherkin Analyzer - IntelliJ Plugin

Static analysis of Cucumber Gherkin feature files (`.feature`) in IntelliJ IDEA, Rider, and other JetBrains IDEs. Also runs in JetBrains Qodana for headless analysis in CI/CD pipelines.

Powered by the same analysis engine as the [Qualimetry Gherkin Analyzer for VS Code](https://marketplace.visualstudio.com/items?itemName=qualimetry.qualimetry-vscode-gherkin-plugin) and the [Qualimetry Gherkin Analyzer for SonarQube](https://github.com/Qualimetry/sonarqube-gherkin-plugin).

## Features

- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Real-time diagnostics** as you edit `.feature` files.
- **Configurable** — enable/disable individual rules and override severities under Settings > Tools > Qualimetry Gherkin Analyzer.
- **Default quality profile** — 53 rules active out of the box for immediate value.
- **Qodana support** — runs automatically in JetBrains Qodana for quality gates in CI/CD.

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
- **Per-rule overrides** for enabled state and severity are stored in `qualimetry-gherkin.xml`.

## Rule categories

| Category | Examples |
|----------|----------|
| Structure | Feature/scenario/step required, naming conventions |
| Design | Step ordering, single When, background best practices |
| Style | Indentation, trailing whitespace, line endings, spelling |
| Tags | Naming patterns, placement, duplicates, restrictions |
| Variables | Unused variables, Examples column coverage |

## Requirements

- IntelliJ IDEA 2024.3 or later (any JetBrains IDE based on the IntelliJ Platform).
- JDK 17+ runtime.

## License

Apache License 2.0. See [LICENSE](LICENSE) for details.
