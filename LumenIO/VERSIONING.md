# LumenIO Versioning & Release Policy

## Core (LumenIO-Core)
- Semantic Versioning: MAJOR.MINOR.PATCH
- MAJOR: Breaking contract changes
- MINOR: Backward-compatible features
- PATCH: Bug fixes

## Variants
- Depend on Core with ranges like `^0.1.0`
- Version independently
- Must pass Core contract tests

## Release Process
1. Core release
2. Update variant core ranges
3. Matrix CI validates all combinations
4. Integration tests ensure compatibility

## CHANGELOG Format
```
## [Version] - YYYY-MM-DD
### Core
- Added: ...
- Changed: ...
- Fixed: ...
### Variant Updates
- PC: ...
- Mobile: ...
### Breaking Changes
- ...
```

## Compatibility Matrix
| Core | PC | Mobile | PCProj | MobileProj | Standalone | Wearables |
|------|----|--------|--------|------------|------------|-----------|
|0.1.x|✓|✓|✓|✓|✓|✓|
