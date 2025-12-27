# npm-workspace-test

NPM workspace test project for Frogbot integration testing.

## Project Structure

```
npm-workspace-test/
├── package.json (workspace root)
└── packages/
    ├── app-a/
    │   └── package.json (lodash:4.17.15, axios:0.18.0)
    └── app-b/
        └── package.json (express:4.16.0, jquery:3.3.1)
```

## Vulnerable Dependencies

### App A (`packages/app-a/package.json`)
- `lodash:4.17.15` - Multiple CVEs
- `axios:0.18.0` - Known vulnerabilities

### App B (`packages/app-b/package.json`)
- `express:4.16.0` - Vulnerable version
- `jquery:3.3.1` - Multiple XSS vulnerabilities

## Testing

This project tests npm workspace support in Frogbot:
- Multi-package detection
- Correct working directory identification
- package-lock.json handling across workspace
- Minimal diffs (only version updates)

