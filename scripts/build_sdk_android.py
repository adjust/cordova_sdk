from scripting_utils import *

def build(root_dir, android_submodule_dir, with_test_lib, is_release = True):
    # ------------------------------------------------------------------
    # Paths
    sdk_adjust_dir  = '{0}/ext/android/sdk'.format(root_dir)
    build_dir       = '{0}/Adjust'.format(sdk_adjust_dir)
    jar_out_dir     = '{0}/src/android'.format(root_dir)
    jar_in_dir      = '{0}/sdk-core/build/libs'.format(build_dir)
    jar_name        = ''

    change_dir(build_dir)

    # ------------------------------------------------------------------
    # Running make*Jar Gradle task.
    if is_release:
        debug_green('Running adjustCoreJarRelease Gradle task ...')
        jar_name = 'adjust-sdk-release.jar'
        gradle_make_release_jar()
    else:
        debug_green('Running adjustCoreJarDebug Gradle task ...')
        jar_name = 'adjust-sdk-debug.jar'
        gradle_make_debug_jar()

    # ------------------------------------------------------------------
    # Moving Android SDK JAR from jarIn to jarOut dir
    debug_green('Moving Android SDK JAR from {0} to {1} dir ...'.format(jar_in_dir, jar_out_dir))
    remove_file_if_exists('{0}/adjust-android.jar'.format(jar_out_dir))
    copy_file('{0}/{1}'.format(jar_in_dir, jar_name), '{0}/adjust-android.jar'.format(jar_out_dir))

    if with_test_lib:
        # ------------------------------------------------------------------
        # Test Library paths
        set_log_tag('ANROID-TEST-LIB-BUILD')
        debug_green('Building Test Library started ...')
        test_jar_in_dir             = '{0}/test-library/build/libs'.format(build_dir)
        test_jar_out_dir            = '{0}/test/plugin/src/android'.format(root_dir)
        test_options_jar_in_dir     = '{0}/test-options/build/intermediates/aar_main_jar/release'.format(build_dir)
        test_options_jar_out_dir    = '{0}/test/plugin/src/android'.format(root_dir)

        create_dir_if_not_exist(test_jar_out_dir)

        # ------------------------------------------------------------------
        # Running Gradle tasks: clean testlibrary:makeJar ...
        debug_green('Running Gradle tasks: clean test-library:adjustTestLibraryJarRelease ...')
        change_dir(build_dir)
        gradle_run([':test-library:adjustTestLibraryJarRelease'])

        # ------------------------------------------------------------------
        # Moving the generated Android SDK JAR from jar in to jar out dir ...
        debug_green('Moving the generated Android SDK JAR from {0} to {1} dir ...'.format(test_jar_in_dir, test_jar_out_dir))
        copy_file('{0}/test-library-release.jar'.format(test_jar_in_dir), '{0}/adjust-test-library.jar'.format(test_jar_out_dir))

        # ------------------------------------------------------------------
        # Running Gradle tasks: clean testOptions:makeJar ...
        debug_green('Running Gradle tasks: clean :test-options:assembleRelease ...')
        change_dir(build_dir)
        gradle_run([':test-options:assembleRelease'])

        # ------------------------------------------------------------------
        # Moving the generated Android SDK JAR from jar in to jar out dir ...
        debug_green('Moving the generated Android SDK JAR from {0} to {1} dir ...'.format(test_options_jar_in_dir, test_options_jar_out_dir))
        copy_file('{0}/classes.jar'.format(test_options_jar_in_dir), '{0}/adjust-test-options.jar'.format(test_options_jar_out_dir))
