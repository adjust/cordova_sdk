import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.adjust.examples',
  appName: 'AdjustTestApp',
  webDir: 'www',
  plugins: {
    // the app has a light background, so the system bars need dark text and icons
    SystemBars: { style: 'LIGHT' },
  },
};

export default config;
