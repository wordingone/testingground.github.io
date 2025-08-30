#!/bin/bash
set -e
NAME="standalone"
DIR="variants/standalone"
mkdir -p "$DIR/tests"
cat > "$DIR/package.json" <<'JSON'
{
  "name": "@lumenio/standalone",
  "private": true,
  "scripts": {
    "test": "vitest"
  },
  "devDependencies": {
    "vitest": "^1.0.0"
  }
}
JSON
cat > "$DIR/tests/sample.spec.ts" <<'TS'
import { describe, it, expect } from 'vitest';

describe('sample', () => {
  it('works', () => {
    expect(true).toBe(true);
  });
});
TS
