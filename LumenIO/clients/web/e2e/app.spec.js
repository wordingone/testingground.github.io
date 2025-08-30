import { test, expect } from '@playwright/test';

test('renders greeting', async ({ page }) => {
  await page.setContent('<div id="app">Hello LumenIO</div>');
  await expect(page.locator('#app')).toHaveText('Hello LumenIO');
});
