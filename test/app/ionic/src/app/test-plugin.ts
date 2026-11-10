/** Options the native SDK takes while under test; same fields as test_options.js in the Cordova test app. */
export class AdjustTestOptions {
  testUrlOverwrite: string | null = null;
  timerIntervalInMilliseconds: string | null = null;
  timerStartInMilliseconds: string | null = null;
  sessionIntervalInMilliseconds: string | null = null;
  subsessionIntervalInMilliseconds: string | null = null;
  attStatus: string | null = null;
  idfa: string | null = null;
  noBackoffWait: boolean | null = null;
  adServicesFrameworkEnabled: boolean | null = null;
  teardown: boolean | null = null;
  extraPath: string | null = null;
  deleteState = false;
  ignoreSystemLifecycleBootstrap: boolean | null = null;
}

/** JavaScript side of the com.adjust.test Cordova plugin, which talks to the test server. */
export interface AdjustTestPlugin {
  startTestSession(baseUrl: string, controlUrl: string, sdkVersion: string, callback: (json: string) => void): void;
  addInfoToSend(key: string, value: unknown): void;
  sendInfoToServer(basePath: string | null): void;
  addTest(test: string): void;
  addTestDirectory(testDirectory: string): void;
  setTestConnectionOptions(): void;
}

/** Test hooks of the com.adjust.sdk plugin. They are not public API, so the Ionic wrapper does not have them. */
export interface AdjustPlugin {
  setTestOptions(testOptions: AdjustTestOptions): void;
  teardown(testParam: string): void;
  onResume(testParam: string): void;
  onPause(testParam: string): void;
}

interface CordovaPlugins {
  Adjust: AdjustPlugin;
  AdjustTest: AdjustTestPlugin;
}

export function adjustTest(): AdjustTestPlugin {
  return (window as unknown as CordovaPlugins).AdjustTest;
}

export function adjustPlugin(): AdjustPlugin {
  return (window as unknown as CordovaPlugins).Adjust;
}
