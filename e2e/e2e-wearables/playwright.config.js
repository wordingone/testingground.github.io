import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
  testDir: './tests',
  projects: [
    { name: 'Pixel 5', use: { ...devices['Pixel 5'] } }
  ]
});
