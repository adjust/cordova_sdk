##
##  Various util python methods which can be utilized and shared among different scripts
##
import os, shutil, glob, time, sys, platform, subprocess
from distutils.dir_util import copy_tree

def set_log_tag(t):
    global TAG
    TAG = t

############################################################
### colors for terminal (does not work in Windows... of course)

CEND = '\033[0m'

CBOLD     = '\33[1m'
CITALIC   = '\33[3m'
CURL      = '\33[4m'
CBLINK    = '\33[5m'
CBLINK2   = '\33[6m'
CSELECTED = '\33[7m'

CBLACK  = '\33[30m'
CRED    = '\33[31m'
CGREEN  = '\33[32m'
CYELLOW = '\33[33m'
CBLUE   = '\33[34m'
CVIOLET = '\33[35m'
CBEIGE  = '\33[36m'
CWHITE  = '\33[37m'

CBLACKBG  = '\33[40m'
CREDBG    = '\33[41m'
CGREENBG  = '\33[42m'
CYELLOWBG = '\33[43m'
CBLUEBG   = '\33[44m'
CVIOLETBG = '\33[45m'
CBEIGEBG  = '\33[46m'
CWHITEBG  = '\33[47m'

CGREY    = '\33[90m'
CRED2    = '\33[91m'
CGREEN2  = '\33[92m'
CYELLOW2 = '\33[93m'
CBLUE2   = '\33[94m'
CVIOLET2 = '\33[95m'
CBEIGE2  = '\33[96m'
CWHITE2  = '\33[97m'

CGREYBG    = '\33[100m'
CREDBG2    = '\33[101m'
CGREENBG2  = '\33[102m'
CYELLOWBG2 = '\33[103m'
CBLUEBG2   = '\33[104m'
CVIOLETBG2 = '\33[105m'
CBEIGEBG2  = '\33[106m'
CWHITEBG2  = '\33[107m'

############################################################
### file system util methods

def copy_file(sourceFile, destFile):
    debug('copying: {0} -> {1}'.format(sourceFile, destFile))
    shutil.copyfile(sourceFile, destFile)

def copy_files(fileNamePattern, sourceDir, destDir):
    for file in glob.glob(sourceDir + '/' + fileNamePattern):
        debug('copying: {0} -> {1}'.format(file, destDir))
        shutil.copy(file, destDir)

def copy_dir_contents(sourceDir, destDir):
    copy_tree(sourceDir, destDir)

def remove_files(fileNamePattern, sourceDir, log=True):
    for file in glob.glob(sourceDir + '/' + fileNamePattern):
        if log:
            debug('deleting: ' + file)
        os.remove(file)

def rename_file(fileNamePattern, newFileName, sourceDir):
    for file in glob.glob(sourceDir + '/' + fileNamePattern):
        debug('rename: {0} -> {1}'.format(file, newFileName))
        os.rename(file, sourceDir + '/' + newFileName)

def clear_dir(dir):
    shutil.rmtree(dir)
    os.mkdir(dir)

def remove_dir_if_exists(path):
    if os.path.exists(path):
        debug('deleting dir: ' + path)
        shutil.rmtree(path)
    else:
        debug('canot delete {0}. dir does not exist'.format(path))

def remove_file_if_exists(path):
    if os.path.exists(path):
        debug('deleting: ' + path)
        os.remove(path)
    else:
        debug('canot delete {0}. file does not exist'.format(path))

def recreate_dir(dir):
    if os.path.exists(dir):
        shutil.rmtree(dir)
    os.mkdir(dir)

############################################################
### debug messages util methods

def debug(msg):
    if not is_windows():
        print(('{0}* [{1}][INFO]:{2} {3}').format(CBOLD, TAG, CEND, msg))
    else:
        print(('* [{0}][INFO]: {1}').format(TAG, msg))

def debug_green(msg):
    if not is_windows():
        print(('{0}* [{1}][INFO]:{2} {3}{4}{5}').format(CBOLD, TAG, CEND, CGREEN, msg, CEND))
    else:
        print(('* [{0}][INFO]: {1}').format(TAG, msg))

def error(msg, do_exit=False):
    if not is_windows():
        print(('{0}* [{1}][ERROR]:{2} {3}{4}{5}').format(CBOLD, TAG, CEND, CRED, msg, CEND))
    else:
        print(('* [{0}][ERROR]: {1}').format(TAG, msg))

    if do_exit:
        exit()

############################################################
### util

def check_submodule_dir(platform, submodule_dir):
    if not os.path.isdir(submodule_dir) or not os.listdir(submodule_dir):
        error('Submodule [{0}] folder empty. Did you forget to run >> git submodule update --init --recursive << ?'.format(platform))
        exit()

def is_windows():
    return platform.system().lower() == 'windows';

# https://stackoverflow.com/questions/17140886/how-to-search-and-replace-text-in-a-file-using-python
def replace_text_in_file(file_path, substring, replace_with):
    # Read in the file
    with open(file_path, 'r') as file:
        filedata = file.read()

    # Replace the target string
    filedata = filedata.replace(substring, replace_with)

    # Write the file out again
    with open(file_path, 'w') as file:
        file.write(filedata)

############################################################
### cordova specific

def _remove_platforms():
    debug_green('Removing platforms ...')
    subprocess.call(['cordova', 'platform', 'remove', 'ios'])
    subprocess.call(['cordova', 'platform', 'remove', 'android'])

def clean_test_app(root_dir):
    example_dir             = '{0}/example'.format(root_dir)
    sdk_name                = 'com.adjust.sdk'
    adjust_sdk_plugin_dir   = '{0}/plugins/com.adjust.sdk'.format(example_dir)

    debug_green('Removing cordova plugins ...')
    os.chdir(example_dir)
    subprocess.call(['cordova', 'plugin', 'rm', sdk_name])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-plugin-console'])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-plugin-customurlscheme'])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-plugin-dialogs'])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-plugin-whitelist'])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-plugin-device'])
    subprocess.call(['cordova', 'plugin', 'rm', 'cordova-universal-links-plugin'])

    remove_dir_if_exists(adjust_sdk_plugin_dir)
    _remove_platforms()

def clean_example_app(root_dir):
    test_dir                    = '{0}/test/app'.format(root_dir)
    sdk_name                    = 'com.adjust.sdk'
    test_plugin_name            = 'com.adjust.sdktesting'
    adjust_sdk_plugin_dir       = '{0}/plugins/com.adjust.sdk'.format(test_dir)
    adjust_sdk_test_plugin_dir  = '{0}/plugins/com.adjust.sdktesting'.format(test_dir)

    debug_green('Removing cordova plugins ...')
    os.chdir(test_dir)
    subprocess.call(['cordova', 'plugin', 'rm', sdk_name])
    subprocess.call(['cordova', 'plugin', 'rm', test_plugin_name])

    remove_dir_if_exists(adjust_sdk_plugin_dir)
    remove_dir_if_exists(adjust_sdk_test_plugin_dir)
    _remove_platforms()

############################################################
### nonsense, eyecandy and such

def waiting_animation(duration, step):
    if(duration <= step):
        return

    line = '-'
    line_killer = '\b'
    while duration >= 0:
        duration -= step
        sys.stdout.write(line)
        sys.stdout.flush()
        sys.stdout.write(line_killer)
        line += '-'
        line_killer += '\b'
        if len(line) > 65:
            line = '-'
        time.sleep(step)
