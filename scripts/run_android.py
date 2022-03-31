from scripting_utils import *

if __name__ == "__main__":
    set_log_tag('RUN-ANDROID')
    error('Error. Do not run this script explicitly, but rather through "build_and_run.py" script.', do_exit=True)

def run(root_dir, apptype):
    if apptype == 'example':
        _run_example(root_dir)
    else:
        _run_testapp(root_dir)

def _run_example(root_dir):
    # ------------------------------------------------------------------
    # Paths
    plugin_temp_dir     = '{0}/temp_plugin'.format(root_dir)
    example_app_dir     = '{0}/example-cordova'.format(root_dir)
    sdk_plugin_package  = 'com.adjust.sdk'
    example_app_package = 'com.adjust.examples'

    # ------------------------------------------------------------------
    # Removing example app from test device.
    debug_green('Removing example app from test device ...')
    adb_uninstall(example_app_package)

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory.
    debug_green('Packaging plugin content to custom directory ...')
    _recreate_plugin_temp_dir(root_dir, plugin_temp_dir)

    # ------------------------------------------------------------------
    # Removing 'android' platform from example app.
    debug_green('Installing \'android\' platform from example app ...')
    change_dir(example_app_dir)
    cordova_remove_platform('android')

    # ------------------------------------------------------------------
    # Installing 'android' platform in example app.
    debug_green('Installing \'android\' platform in example app ...')
    cordova_add_platform('android')

    # ------------------------------------------------------------------
    # Re-installing plugins to example app.
    debug_green('Re-installing plugins to example app ...')
    cordova_remove_plugin(sdk_plugin_package)
    cordova_add_plugin(plugin_temp_dir, options=['--verbose', '--nofetch'])
    cordova_add_plugin('cordova-plugin-console')
    cordova_add_plugin('cordova-plugin-customurlscheme', options=['--variable', 'URL_SCHEME=adjust-example'])
    cordova_add_plugin('cordova-plugin-dialogs')
    cordova_add_plugin('cordova-plugin-whitelist')
    cordova_add_plugin('https://github.com/apache/cordova-plugin-device.git')
    cordova_add_plugin('cordova-universal-links-plugin')

    # ------------------------------------------------------------------
    # Building Cordova example app project.
    debug_green('Building Cordova example app project ...')
    cordova_build('android', options=['--verbose'])

    # ------------------------------------------------------------------
    # Running Cordova example app.
    # cordova_run('android') # <-- Does not seem to work, some Cordova specific error.
    debug_green('Installing & running Cordova example app ...')
    adb_install_apk('platforms/android/app/build/outputs/apk/debug/app-debug.apk')
    adb_shell(example_app_package)

    # ------------------------------------------------------------------
    # Cleaning up temporary stuff.
    debug_green('Cleaning up temporary directorie(s) ...')
    remove_dir_if_exists(plugin_temp_dir)

def _run_testapp(root_dir):
    # ------------------------------------------------------------------
    # paths
    test_app_dir        = '{0}/test/app'.format(root_dir)
    test_plugin_dir     = '{0}/test/plugin'.format(root_dir)
    scripts_dir         = '{0}/scripts'.format(root_dir)
    plugin_temp_dir     = '{0}/temp_plugin'.format(root_dir)
    sdk_plugin_package  = 'com.adjust.sdk'
    test_plugin_package = 'com.adjust.test'
    test_app_package    = 'com.adjust.examples'

    # ------------------------------------------------------------------
    # Removing test app from test device.
    debug_green('Removing test app package [{0}] from test device ...'.format(test_app_package))
    adb_uninstall(test_app_package)

    # ------------------------------------------------------------------
    # Packaging plugin content to custom directory.
    debug_green('Packaging plugin content to custom directory [{0}] ...'.format(plugin_temp_dir))
    _recreate_plugin_temp_dir(root_dir, plugin_temp_dir)

    # ------------------------------------------------------------------
    # Removing 'android' platform.
    debug_green('Removing \'android\' platform in [{0}] ...'.format(test_app_dir))
    change_dir(test_app_dir)
    cordova_remove_platform('android')

    # ------------------------------------------------------------------
    # Installing 'android' platform.
    debug_green('Installing \'android\' platform in [{0}] ...'.format(test_app_dir))
    cordova_add_platform('android')

    # ------------------------------------------------------------------
    # Re-installing plugins to test app.
    debug_green('Re-installing plugins to test app ...')
    cordova_remove_plugin(sdk_plugin_package)
    cordova_remove_plugin(test_plugin_package)
    cordova_add_plugin(plugin_temp_dir, options=['--verbose', '--nofetch'])
    cordova_add_plugin(test_plugin_dir, options=['--verbose', '--nofetch'])
    cordova_add_plugin('cordova-plugin-device', options=['--verbose'])
    cordova_add_plugin('cordova-universal-links-plugin')
    cordova_add_plugin('cordova-plugin-customurlscheme', options=['--variable', 'URL_SCHEME=adjust-test'])

    # ------------------------------------------------------------------
    # Building Cordova test app project.
    debug_green('Building Cordova test app project ...')
    cordova_build('android', options=['--verbose'])
    
    # ------------------------------------------------------------------
    # Running Cordova test app.
    debug_green('Installing & running Cordova test app ...')
    adb_install_apk('platforms/android/app/build/outputs/apk/debug/app-debug.apk')
    adb_shell(test_app_package)

    # ------------------------------------------------------------------
    # Cleaning up temporary stuff.
    debug_green('Cleaning up temporary directorie(s) ...')
    remove_dir_if_exists(plugin_temp_dir)

def _recreate_plugin_temp_dir(root_dir, plugin_temp_dir):
    recreate_dir(plugin_temp_dir)
    copy_dir_contents('{0}/www'.format(root_dir), '{0}/www'.format(plugin_temp_dir))
    copy_dir_contents('{0}/src'.format(root_dir), '{0}/src'.format(plugin_temp_dir))
    copy_file('{0}/package.json'.format(root_dir), '{0}/package.json'.format(plugin_temp_dir))
    copy_file('{0}/plugin.xml'.format(root_dir), '{0}/plugin.xml'.format(plugin_temp_dir))
