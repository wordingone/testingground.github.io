import { describe, it, expect } from 'vitest';
import { greet } from '../src/greet.js';

describe('greet', () => {
  it('greets by name', () => {
    expect(greet('World')).toBe('Hello World');
  });
});
