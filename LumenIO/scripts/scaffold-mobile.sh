#!/bin/bash
set -e
NAME="mobile"
DIR="LumenIO/variants/mobile"
mkdir -p "$DIR/tests"
cat > "$DIR/package.json" <<'JSON'
{
  "name": "@lumenio/mobile",
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
