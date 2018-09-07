import os, subprocess
from scripting_utils import *

if __name__ == "__main__":
    set_log_tag('RUN-ANDROID')
    error('Error. Do not run this script explicitly, but rather through "build_sdk.py" script.')
    exit()

def run(root_dir, android_submodule_dir, apptype):
    if apptype == 'example':
        _run_example(root_dir, android_submodule_dir)
    else:
        _run_testapp(root_dir, android_submodule_dir)

def _run_example(root_dir, android_submodule_dir):
    # ------------------------------------------------------------------
    # paths
    temp_plugin_dir = '{0}/temp_plugin'.format(root_dir)
    example_dir     = '{0}/example'.format(root_dir)
    sdk_name        = 'com.adjust.sdk'

    # ------------------------------------------------------------------
    # Removing app from test device
    debug_green('Removing app from test device ...')
    subprocess.call(['adb', 'uninstall', 'com.adjust.examples'])

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory
    debug_green('Packaging plugin content to custom directory ...')
    _recreate_test_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Installing Android platform
    debug_green('Installing Android platform ...')
    os.chdir(example_dir)
    subprocess.call(['cordova', 'platform', 'add', 'android'])

    # ------------------------------------------------------------------
    # Re-installing plugins
    debug_green('Re-installing plugins ...')
    subprocess.call(['cordova', 'plugin', 'remove', sdk_name])
    subprocess.call(['cordova', 'plugin', 'add', temp_plugin_dir])
    subprocess.call(['cordova', 'plugin', 'add', 'cordova-plugin-console'])
    subprocess.call(['cordova', 'plugin', 'add', 'cordova-plugin-customurlscheme', '--variable', 'URL_SCHEME=adjustExample'])
    subprocess.call(['cordova', 'plugin', 'add', 'cordova-plugin-dialogs'])
    subprocess.call(['cordova', 'plugin', 'add', 'cordova-plugin-whitelist'])
    subprocess.call(['cordova', 'plugin', 'add', 'https://github.com/apache/cordova-plugin-device.git'])
    subprocess.call(['cordova', 'plugin', 'add', 'cordova-universal-links-plugin'])

    # ------------------------------------------------------------------
    # Building cordova project
    debug_green('Building cordova project ...')
    subprocess.call(['cordova', 'run', 'android'])

    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful!')
    remove_dir_if_exists(temp_plugin_dir)

def _run_testapp(root_dir, android_submodule_dir):
    # ------------------------------------------------------------------
    # paths
    project_dir = '{0}/test/app'.format(root_dir)
    sdk_plugin_name = 'com.adjust.sdk'
    testing_plugin_name = 'com.adjust.sdktesting'
    testing_plugin_dir = '{0}/test/plugin'.format(root_dir)
    scripts_dir = '{0}/scripts'.format(root_dir)
    temp_plugin_dir = '{0}/temp_plugin'.format(root_dir)
    test_app_package = 'com.adjust.testapp'

    # ------------------------------------------------------------------
    # Removing app from test device
    debug_green('Removing app package [{0}] from test device ...'.format(test_app_package))
    subprocess.call(['adb', 'uninstall', test_app_package])

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory
    debug_green('Packaging plugin content to custom directory [{0}] ...'.format(temp_plugin_dir))
    _recreate_test_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Installing Android platform
    debug_green('Installing Android platform in [{0}] ...'.format(project_dir))
    os.chdir(project_dir)
    subprocess.call(['cordova', 'platform', 'add', 'android'])

    # ------------------------------------------------------------------
    # Re-installing plugins
    debug_green('Re-installing plugins ...')
    subprocess.call(['cordova', 'plugin', 'remove', sdk_plugin_name])
    subprocess.call(['cordova', 'plugin', 'remove', testing_plugin_name])
    subprocess.call(['cordova', 'plugin', 'add', '--verbose', temp_plugin_dir, '--nofetch'])
    subprocess.call(['cordova', 'plugin', 'add', '--verbose', testing_plugin_dir, '--nofetch'])
    subprocess.call(['cordova', 'plugin', 'add', '--verbose', 'cordova-plugin-device'])
    
    # ------------------------------------------------------------------
    # Running Cordova build
    debug_green('Running Cordova build ...')
    subprocess.call(['cordova', 'build', 'android', '--verbose'])
    subprocess.call(['adb', 'install', '-r', 'platforms/android/build/outputs/apk/debug/android-debug.apk'])
    subprocess.call(['adb', 'shell', '-p', test_app_package, '1'])

    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful!')
    remove_dir_if_exists(temp_plugin_dir)

def _recreate_test_plugin_dir(root_dir, temp_plugin_dir):
    recreate_dir(temp_plugin_dir)
    copy_dir_contents('{0}/www'.format(root_dir), '{0}/www'.format(temp_plugin_dir))
    copy_dir_contents('{0}/src'.format(root_dir), '{0}/src'.format(temp_plugin_dir))
    copy_file('{0}/package.json'.format(root_dir), '{0}/package.json'.format(temp_plugin_dir))
    copy_file('{0}/plugin.xml'.format(root_dir), '{0}/plugin.xml'.format(temp_plugin_dir))
