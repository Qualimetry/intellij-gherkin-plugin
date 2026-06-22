# Changelog

All notable changes to the Qualimetry Gherkin Analyzer for IntelliJ are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.4.4] - 2026-06-23

- Version-alignment release.

## [1.4.3] - 2026-06-17

### Added

- Quick-fix link from each inspection to its full-text rule documentation.
- Full-text, per-rule documentation covering what each rule checks, why it matters, and how to fix it.

## [1.4.2] - 2026-06-16

### Changed

- Updated for compatibility with the latest JetBrains IDE releases.

## [1.4.1] - 2026-06-16

### Fixed

- **no-byte-order-mark**: improved detection of a UTF-8 byte order mark.
- **shared-given-to-background**: only flags Given steps that form a common leading sequence across scenarios, eliminating false positives when scenarios differ on an earlier step.

## [1.4.0] - 2026-06-12

### Added

- New rules: **max-line-length**, **no-empty-data-table-cells**, and **examples-constant-column**.

### Changed

- Improved accuracy of more than twenty existing rules, including doc string indentation, duplicate detection with data tables, dialect-aware commented-step detection, and tag handling.
- SonarQube rule import now applies rule parameters and severities, and reuses the saved token.
- Improved BOM and parse-error handling.

## [1.3.12] - 2026-06-11

### Added

- Rules now re-sync automatically from the last-used SonarQube server and quality profile on IDE startup (toggle in Settings > Tools > Qualimetry Gherkin Analyzer).
- **Import from SonarQube** button on the settings page.

### Changed

- SonarQube tokens are now stored securely in the IDE credential store.

## [1.3.11] - 2026-06-11

### Added

- **Import from SonarQube** button on the settings page (Settings > Tools > Qualimetry Gherkin Analyzer), alongside the existing Tools menu action.

### Changed

- Documentation now clarifies where to find the SonarQube import: the IDE's main menu bar (Tools > Qualimetry Gherkin > Import Rules from SonarQube) or the new settings page button.

## [1.3.10] - 2026-03-19

### Changed

- Plugin distribution is now signed.

## [1.3.9] - 2026-03-04

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Per-rule settings panel with enable/disable, severity override, and search filter under Settings > Tools.
- **Import from SonarQube** — fetch active rules from a SonarQube quality profile via Tools > Qualimetry Gherkin > Import Rules from SonarQube.
- Per-rule inspection options for Qodana profile configuration.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.8] - 2026-03-03

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Per-rule settings panel with enable/disable, severity override, and search filter under Settings > Tools.
- **Import from SonarQube** — fetch active rules from a SonarQube quality profile via Tools > Qualimetry Gherkin > Import Rules from SonarQube.
- Per-rule inspection options for Qodana profile configuration.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.7] - 2026-03-03

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Configurable per-rule enable/disable and severity overrides under Settings > Tools.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.6] - 2026-03-03

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Configurable per-rule enable/disable and severity overrides under Settings > Tools.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.5] - 2026-03-03

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Configurable per-rule enable/disable and severity overrides under Settings > Tools.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.4] - 2026-03-03

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Configurable per-rule enable/disable and severity overrides under Settings > Tools.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.

## [1.3.3] - 2026-03-02

### Added

- Initial release of the Gherkin Analyzer plugin for IntelliJ IDEA, Rider, and other JetBrains IDEs.
- **83 analysis rules** covering structure, design, style, tags, variables, spelling, and more.
- **Default quality profile** with 53 rules active out of the box.
- Real-time diagnostics as you edit `.feature` files.
- Configurable per-rule enable/disable and severity overrides under Settings > Tools.
- Compatible with JetBrains Qodana for headless CI/CD analysis.
- Same analysis engine as the Qualimetry Gherkin Analyzer for VS Code and SonarQube.
