from scripting_utils import *

if __name__ == "__main__":
    set_log_tag('RUN-IOS')
    error('Error. Do not run this script explicitly, but rather through "build_and_run.py" script.', do_exit=True)

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
    change_dir(example_dir)
    cordova_add_platform('ios')

    # ------------------------------------------------------------------
    # Re-installing plugins
    debug_green('Re-installing plugins ...')
    cordova_remove_plugin(sdk_name)
    cordova_add_plugin(temp_plugin_dir)
    cordova_add_plugin('cordova-plugin-console')
    cordova_add_plugin('cordova-plugin-customurlscheme', options=['--variable', 'URL_SCHEME=adjustExample'])
    cordova_add_plugin('cordova-plugin-dialogs')
    cordova_add_plugin('cordova-plugin-whitelist')
    cordova_add_plugin('https://github.com/apache/cordova-plugin-device.git')

    # ------------------------------------------------------------------
    # Building cordova project
    debug_green('Building cordova project ...')
    cordova_build('ios')

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
    temp_plugin_dir     = '{0}/temp_plugin'.format(root_dir)

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory
    debug_green('Packaging plugin content to custom directory [{0}] ...'.format(temp_plugin_dir))
    _recreate_temp_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Installing Android platform
    debug_green('Installing Android platform in [{0}] ...'.format(project_dir))
    change_dir(project_dir)
    cordova_add_platform('ios')

    # ------------------------------------------------------------------
    # Re-installing plugins
    debug_green('Re-installing plugins ...')
    cordova_remove_plugin(sdk_plugin_name)
    cordova_remove_plugin(testing_plugin_name)
    cordova_add_plugin(temp_plugin_dir, options=['--verbose', '--nofetch'])
    cordova_add_plugin(testing_plugin_dir, options=['--verbose', '--nofetch'])

    # ------------------------------------------------------------------
    # Running Cordova build
    debug_green('Running Cordova build ...')
    cordova_build('ios', options=['--verbose'])
    
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
