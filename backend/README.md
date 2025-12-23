# Maven Backend - Multi-Ecosystem Test

Maven project sitting side-by-side with npm frontend in Backstage demo.

## Purpose

Test frogbot's multi-ecosystem detection:
- **npm** (frontend/Backstage)
- **Maven** (backend/Java)

## Vulnerable Dependencies (Intentional)

| Dependency | Version | Known CVEs |
|------------|---------|------------|
| Spring Core | 5.2.0.RELEASE | CVE-2020-5421, CVE-2021-22060 |
| Log4j Core | 2.14.1 | **CVE-2021-44228** (Log4Shell) |
| Commons Collections | 3.2.1 | CVE-2015-6420 |
| Jackson Databind | 2.9.8 | Multiple CVEs |
| Apache HttpClient | 4.5.2 | CVE-2020-13956 |
| Commons FileUpload | 1.3.1 | CVE-2016-1000031 |

## Expected Frogbot Behavior

1. Detect **both** npm and Maven projects
2. Scan **both** ecosystems
3. Report vulnerabilities from **both**
4. Create PRs with fixes for **both**

## Structure

```
demo/
├── package.json           ← npm frontend (Backstage)
├── packages/              ← npm workspaces
├── maven-backend/         ← Maven backend (NEW)
│   ├── pom.xml           ← Vulnerable Maven deps
│   └── src/
│       └── main/java/
```

## Test Commands

```bash
# Install Maven dependencies
cd maven-backend
mvn clean install

# Scan with frogbot
cd ..
frogbot scan-repository
```

## Expected Fixes

- Spring Core: 5.2.0 → 5.3.30+
- Log4j: 2.14.1 → 2.17.1+
- Commons Collections: 3.2.1 → 3.2.2+
- Jackson: 2.9.8 → 2.15.0+
- HttpClient: 4.5.2 → 4.5.13+
- FileUpload: 1.3.1 → 1.5+

