from scripting_utils import *

def build(root_dir, ios_submodule_dir, with_test_lib):
    # ------------------------------------------------------------------
    # paths]
    ext_dir     = '{0}/sdk'.format(ios_submodule_dir)
    lib_out_dir = '{0}/src/ios'.format(root_dir)

    # ------------------------------------------------------------------
    # Removing old framework
    debug_green('Removing old framework ...')
    remove_dir_if_exists('{0}/AdjustSdk.framework'.format(lib_out_dir))

    # ------------------------------------------------------------------
    # Building new framework
    debug_green('Building new framework ...')
    change_dir(ext_dir)
    xcode_build('AdjustStatic')

    # ------------------------------------------------------------------
    # Copy built framework to designated location
    debug_green('Copy built framework to designated location ...')
    copy_dir_contents('{0}/Frameworks/Static/AdjustSdk.framework'.format(ext_dir), '{0}/AdjustSdk.framework'.format(lib_out_dir))

    # ------------------------------------------------------------------
    # Running symlink fix
    debug_green('Running symlink fix ...')
    # Remove any existing symlinks
    remove_file_if_exists('{0}/AdjustSdk.framework/AdjustSdk'.format(lib_out_dir))
    recreate_dir('{0}/AdjustSdk.framework/Headers'.format(lib_out_dir))
    # Move library and headers
    copy_file('{0}/AdjustSdk.framework/Versions/A/AdjustSdk'.format(lib_out_dir), '{0}/AdjustSdk.framework/AdjustSdk'.format(lib_out_dir))
    copy_dir_contents('{0}/AdjustSdk.framework/Versions/A/Headers'.format(lib_out_dir), '{0}/AdjustSdk.framework/Headers'.format(lib_out_dir))
    # Remove Versions folder
    remove_dir_if_exists('{0}/AdjustSdk.framework/Versions'.format(lib_out_dir))

    if with_test_lib:
        # ------------------------------------------------------------------
        # Test Library paths
        set_log_tag('ANROID-TEST-LIB-BUILD')
        debug_green('Building Test Library started ...')
        test_lib_project_dir    = '{0}/AdjustTests/AdjustTestLibrary'.format(ext_dir)
        test_lib_out_dir        = '{0}/test/plugin/src/ios'.format(root_dir)
        frameworks_dir          = '{0}/Frameworks/Static/'.format(ext_dir)

        # ------------------------------------------------------------------
        # Removing old framework
        debug_green('Removing old framework ...')
        remove_dir_if_exists('{0}/AdjustTestLibrary.framework'.format(test_lib_out_dir))

        # ------------------------------------------------------------------
        # Building new framework
        debug_green('Building new framework ...')
        change_dir(test_lib_project_dir)
        xcode_build('AdjustTestLibraryStatic', configuration='Debug')

        # ------------------------------------------------------------------
        # Copy built framework to designated location
        debug_green('Copy built framework to designated location ...')
        copy_dir_contents('{0}/AdjustTestLibrary.framework'.format(frameworks_dir), '{0}/AdjustTestLibrary.framework'.format(test_lib_out_dir))
        remove_dir_if_exists('{0}/AdjustTestLibrary.framework/Versions'.format(test_lib_out_dir))
