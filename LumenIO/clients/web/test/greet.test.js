import { describe, it, expect } from 'vitest';
import { greet } from '../src/main.js';

describe('greet', () => {
  it('greets by name', () => {
    expect(greet('World')).toBe('Hello World');
  });
});
