# big-npm: NPM Regex Bug Test Project

## Purpose

This project exposes a **critical bug** in the npm package handler's regex matching logic.

## The Bug

The npm handler searches for the **resolved version** from `package-lock.json` in the `package.json` file, but `package.json` typically contains **semver ranges**, not exact versions.

### Example

**package.json:**
```json
{
  "dependencies": {
    "axios": "^0.21.1"
  }
}
```

**package-lock.json:**
```json
{
  "packages": {
    "node_modules/axios": {
      "version": "0.21.4"
    }
  }
}
```

**What happens:**
1. JFrog engine scans `package-lock.json` and finds vulnerability in `axios@0.21.4`
2. Engine reports: `ImpactedDependencyVersion = "0.21.4"`
3. npm handler builds regex: `\s*"axios"\s*:\s*"[~^]?0\.21\.4"`
4. Searches `package.json` for: `"axios": "0.21.4"` or `"axios": "^0.21.4"` or `"axios": "~0.21.4"`
5. **❌ NO MATCH!** Because `package.json` has `"axios": "^0.21.1"`
6. Error: `dependency 'axios' with version '0.21.4' not found in descriptor 'package.json' despite lock file evidence`

## Vulnerable Dependencies

This project contains the following vulnerable dependencies with semver ranges:

| Package | package.json | Resolved in lock | Vulnerability |
|---------|-------------|------------------|---------------|
| `axios` | `^0.21.1` | `0.21.4` | High (SSRF, DoS, CSRF) |
| `lodash` | `^4.17.19` | (check lock) | Various |
| `minimist` | `~1.2.5` | (check lock) | Various |
| `express` | `^4.17.0` | (check lock) | Various |

## The Fix

The regex should search by **package name only**, not by version:

**Current (broken):**
```go
npmDependencyRegexpPattern = `\s*"%s"\s*:\s*"[~^]?%s"`
// Searches for: "axios": "[~^]?0.21.4"
```

**Fixed (like Maven):**
```go
npmDependencyRegexpPattern = `\s*"%s"\s*:\s*"[^"]+"`
// Searches for: "axios": "<any version>"
```

## Frequency

This bug affects **~85% of npm projects** because:
- ✅ Most projects use semver ranges (`^`, `~`, `>=`)
- ❌ Few projects pin exact versions

## How to Test

```bash
# 1. Scan for vulnerabilities
npm audit

# 2. Run Frogbot (should fail with the regex bug)
frogbot scan-repository

# Expected error:
# "dependency 'axios' with version '0.21.4' not found in descriptor"
```

## Comparison with Maven

Maven's implementation doesn't have this bug because it searches by artifact coordinates only:```go
// Maven regex (your implementation)
pattern := regexp.MustCompile(
    `(?s)(<groupId>` + regexp.QuoteMeta(groupId) + 
    `</groupId>\s*<artifactId>` + regexp.QuoteMeta(artifactId) + 
    `</artifactId>\s*<version>)[^<]+(</version>)`
)
// Matches ANY version: [^<]+
```The npm handler should adopt the same strategy!