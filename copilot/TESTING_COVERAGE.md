# Test Coverage Configuration

## Overview

This project uses **Kover** (Kotlin Code Coverage) to measure and report test coverage.

## Running Coverage Reports

### Generate HTML Report
```bash
./gradlew koverHtmlReport
```

Report location: `composeApp/build/reports/kover/html/index.html`

### Generate XML Report (for CI/CD)
```bash
./gradlew koverXmlReport
```

### Verify Coverage
```bash
./gradlew koverVerify
```

## Coverage Targets

| Layer | Target |
|-------|--------|
| Use Cases | 80-90% |
| Repositories | 70-80% |
| ViewModels | 60-70% |
| UI Components | 30-40% |

Minimum overall: 20% (increasing to 70-80%)
