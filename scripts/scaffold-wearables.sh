#!/bin/bash
set -e
NAME="wearables"
DIR="variants/wearables"
mkdir -p "$DIR/tests"
cat > "$DIR/package.json" <<'JSON'
{
  "name": "@lumenio/wearables",
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
