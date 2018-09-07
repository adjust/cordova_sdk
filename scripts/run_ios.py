import os, subprocess
from scripting_utils import *

if __name__ == "__main__":
    set_log_tag('RUN-IOS')
    error('Error. Do not run this script explicitly, but rather through "build_and_run.py" script.')
    exit()

def run(root_dir, ios_submodule_dir, apptype):
    if apptype == 'example':
        _run_example(root_dir, ios_submodule_dir)
    else:
        _run_testapp(root_dir, ios_submodule_dir)

def _run_example(root_dir, ios_submodule_dir):
    # ------------------------------------------------------------------
    # paths
    temp_plugin_dir = '{0}/temp_plugin'.format(root_dir)
    example_dir     = '{0}/example'.format(root_dir)
    sdk_name        = 'com.adjust.sdk'

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory
    debug_green('Packaging plugin content to custom directory ...')
    _recreate_temp_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Installing iOS platform
    debug_green('Installing iOS platform ...')
    os.chdir(example_dir)
    subprocess.call(['cordova', 'platform', 'add', 'ios'])

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

    # ------------------------------------------------------------------
    # Building cordova project
    debug_green('Building cordova project ...')
    subprocess.call(['cordova', 'build', 'ios'])

    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful! Run it from Xcode (platforms/ios/).')
    remove_dir_if_exists(temp_plugin_dir)

def _run_testapp(root_dir, ios_submodule_dir):
    # ------------------------------------------------------------------
    # paths
    project_dir         = '{0}/test/app'.format(root_dir)
    sdk_plugin_name     = 'com.adjust.sdk'
    testing_plugin_name = 'com.adjust.sdktesting'
    testing_plugin_dir  = '{0}/test/plugin'.format(root_dir)
    scripts_dir         = '{0}/scripts'.format(root_dir)
    test_plugin_dir     = '{0}/temp_plugin'.format(root_dir)
    test_app_package    = 'com.adjust.testapp'
    temp_plugin_dir = '{0}/temp_plugin'.format(root_dir)

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory
    debug_green('Packaging plugin content to custom directory [{0}] ...'.format(temp_plugin_dir))
    _recreate_temp_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Installing Android platform
    debug_green('Installing Android platform in [{0}] ...'.format(project_dir))
    os.chdir(project_dir)
    subprocess.call(['cordova', 'platform', 'add', 'ios'])

    # ------------------------------------------------------------------
    # Re-installing plugins
    debug_green('Re-installing plugins ...')
    subprocess.call(['cordova', 'plugin', 'remove', sdk_plugin_name])
    subprocess.call(['cordova', 'plugin', 'remove', testing_plugin_name])
    subprocess.call(['cordova', 'plugin', 'add', '--verbose', temp_plugin_dir, '--nofetch'])
    subprocess.call(['cordova', 'plugin', 'add', '--verbose', testing_plugin_dir, '--nofetch'])

    # ------------------------------------------------------------------
    # Running Cordova build
    debug_green('Running Cordova build ...')
    subprocess.call(['cordova', 'build', 'ios', '--verbose'])
    
    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful! Run it from Xcode ({0}/platforms/ios/)'.format(project_dir))
    remove_dir_if_exists(temp_plugin_dir)

def _recreate_temp_plugin_dir(root_dir, temp_plugin_dir):
    recreate_dir(temp_plugin_dir)
    copy_dir_contents('{0}/www'.format(root_dir), '{0}/www'.format(temp_plugin_dir))
    copy_dir_contents('{0}/src'.format(root_dir), '{0}/src'.format(temp_plugin_dir))
    copy_file('{0}/package.json'.format(root_dir), '{0}/package.json'.format(temp_plugin_dir))
    copy_file('{0}/plugin.xml'.format(root_dir), '{0}/plugin.xml'.format(temp_plugin_dir))
