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
    temp_plugin_dir    = '{0}/temp_plugin'.format(root_dir)
    example_app_dir    = '{0}/example-cordova'.format(root_dir)
    sdk_plugin_package = 'com.adjust.sdk'

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory.
    debug_green('Packaging plugin content to custom directory ...')
    _recreate_temp_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Removing 'ios' platform from example app.
    debug_green('Removing \'ios\' platform from example app ...')
    change_dir(example_app_dir)
    cordova_remove_platform('ios')

    # ------------------------------------------------------------------
    # Installing 'ios' platform to example app.
    debug_green('Installing \'ios\' platform to example app ...')
    cordova_add_platform('ios')

    # ------------------------------------------------------------------
    # Re-installing plugins to example app.
    debug_green('Re-installing plugins ...')
    cordova_remove_plugin(sdk_plugin_package)
    cordova_add_plugin(temp_plugin_dir)
    cordova_add_plugin('cordova-plugin-console')
    cordova_add_plugin('cordova-plugin-customurlscheme', options=['--variable', 'URL_SCHEME=adjust-example'])
    cordova_add_plugin('cordova-plugin-dialogs')
    cordova_add_plugin('cordova-plugin-whitelist')
    cordova_add_plugin('https://github.com/apache/cordova-plugin-device.git')

    # ------------------------------------------------------------------
    # Running example app.
    debug_green('Running Cordova example app project ...')
    cordova_run('ios')

    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful! (You can also run it from Xcode ({0}/platforms/ios/)).'.format(example_app_dir))
    remove_dir_if_exists(temp_plugin_dir)

def _run_testapp(root_dir, ios_submodule_dir):
    # ------------------------------------------------------------------
    # paths
    test_app_dir        = '{0}/test/app'.format(root_dir)
    scripts_dir         = '{0}/scripts'.format(root_dir)
    test_plugin_dir     = '{0}/test/plugin'.format(root_dir)
    temp_plugin_dir     = '{0}/temp_plugin'.format(root_dir)
    sdk_plugin_package  = 'com.adjust.sdk'
    test_plugin_package = 'com.adjust.test'
    test_app_package    = 'com.adjust.examples'

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory.
    debug_green('Packaging plugin content to custom directory [{0}] ...'.format(temp_plugin_dir))
    _recreate_temp_plugin_dir(root_dir, temp_plugin_dir)

    # ------------------------------------------------------------------
    # Removing 'android' platform from test app.
    debug_green('Removing \'android\' platform from test app in [{0}] ...'.format(test_app_dir))
    change_dir(test_app_dir)
    cordova_remove_platform('ios')

    # ------------------------------------------------------------------
    # Installing 'android' platform to test app.
    debug_green('Installing \'android\' platform to test app in [{0}] ...'.format(test_app_dir))
    cordova_add_platform('ios')

    # ------------------------------------------------------------------
    # Re-installing plugins to test app.
    debug_green('Re-installing plugins to test app ...')
    cordova_remove_plugin(sdk_plugin_package)
    cordova_remove_plugin(test_plugin_package)
    cordova_add_plugin('cordova-plugin-customurlscheme', options=['--variable', 'URL_SCHEME=adjust-test'])
    cordova_add_plugin(temp_plugin_dir, options=['--verbose', '--nofetch'])
    cordova_add_plugin(test_plugin_dir, options=['--verbose', '--nofetch'])

    # ------------------------------------------------------------------
    # Running test app.
    debug_green('Running Cordova test app project ...')
    cordova_run('ios')
    
    # ------------------------------------------------------------------
    # Build successful!
    debug_green('Build successful! (You can also run it from Xcode ({0}/platforms/ios/))'.format(test_app_dir))
    remove_dir_if_exists(temp_plugin_dir)

def _recreate_temp_plugin_dir(root_dir, temp_plugin_dir):
    recreate_dir(temp_plugin_dir)
    copy_dir_contents('{0}/www'.format(root_dir), '{0}/www'.format(temp_plugin_dir))
    copy_dir_contents('{0}/src'.format(root_dir), '{0}/src'.format(temp_plugin_dir))
    copy_file('{0}/package.json'.format(root_dir), '{0}/package.json'.format(temp_plugin_dir))
    copy_file('{0}/plugin.xml'.format(root_dir), '{0}/plugin.xml'.format(temp_plugin_dir))
