from scripting_utils import *

def build(root_dir, android_submodule_dir, with_test_lib, is_release = True):
    # ------------------------------------------------------------------
    # Paths
    sdk_adjust_dir  = '{0}/ext/android/sdk'.format(root_dir)
    build_dir       = '{0}/Adjust'.format(sdk_adjust_dir)
    jar_out_dir     = '{0}/src/android'.format(root_dir)
    jar_in_dir      = ''

    change_dir(build_dir)

    # ------------------------------------------------------------------
    # Running make*Jar Gradle task.
    if is_release:
        debug_green('Running makeReleaseJar Gradle task ...')
        jar_in_dir = '{0}/adjust/build/intermediates/intermediate-jars/release'.format(build_dir)
        gradle_make_release_jar()
    else:
        debug_green('Running makeDebugJar Gradle task ...')
        jar_in_dir = '{0}/adjust/build/intermediates/intermediate-jars/debug'.format(build_dir)
        gradle_make_debug_jar()

    # ------------------------------------------------------------------
    # Moving Android SDK JAR from jarIn to jarOut dir
    debug_green('Moving Android SDK JAR from {0} to {1} dir ...'.format(jar_in_dir, jar_out_dir))
    remove_file_if_exists('{0}/adjust-android.jar'.format(jar_out_dir))
    copy_files('classes.jar', jar_in_dir, jar_out_dir)
    rename_file('classes.jar', 'adjust-android.jar', jar_out_dir)

    if with_test_lib:
        # ------------------------------------------------------------------
        # Test Library paths
        set_log_tag('ANROID-TEST-LIB-BUILD')
        waiting_animation(duration=4.0, step=0.025)
        debug_green('Building Test Library started ...')
        test_jar_in_dir  = '{0}/testlibrary/build/outputs'.format(build_dir)
        test_jar_out_dir = '{0}/test/plugin/src/android'.format(root_dir)

        create_dir_if_not_exist(test_jar_out_dir)

        # ------------------------------------------------------------------
        # Running Gradle tasks: clean testlibrary:makeJar ...
        debug_green('Running Gradle tasks: clean testlibrary:makeJar ...')
        change_dir(build_dir)
        gradle_run(['clean', ':testlibrary:makeJar'])

        # ------------------------------------------------------------------
        # Moving the generated Android SDK JAR from jar in to jar out dir ...
        debug_green('Moving the generated Android SDK JAR from {0} to {1} dir ...'.format(test_jar_in_dir, test_jar_out_dir))
        copy_files('*.jar', test_jar_in_dir, test_jar_out_dir)
        rename_file('*.jar', 'adjust-testing.jar', test_jar_out_dir)
