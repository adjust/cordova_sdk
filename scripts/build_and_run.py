#!/usr/bin/python
import argparse
from scripting_utils import *
import build_sdk_android    as android_builder
import run_android          as android_runner
import build_sdk_ios        as ios_builder
import run_ios              as ios_runner

set_log_tag('BUILD-SDK')

if __name__ != "__main__":
    error('Error. Do not import this script, but run it explicitly.')
    exit()

# ------------------------------------------------------------------
# set arguments
parser = argparse.ArgumentParser(description="""
    Script used to build SDK and run example or test apps :{) examples: 
    build android SDK with test library -> ./build_and_run.py build android -tl; 
    run ios test app (make sure the app is previously built) -> ./build_and_run.py run ios -t test """)
parser.add_argument('type', help='type of operation to be performed. use >b&r< to build & run', choices=['build', 'run', 'b&r'])
parser.add_argument('platform', help='platform on which the scripts will be ran', choices=['android', 'ios'])
parser.add_argument('-t', '--apptype', help='if run is selected, type of the application to be ran', choices=['example', 'test'])
parser.add_argument('-tl', '--withtestlib', help='build test library as well', action='store_true')
args = parser.parse_args()

if args.type == 'run' and args.apptype == None:
    error('Error. App type [ example | test ] not selected!')
    exit()

# ------------------------------------------------------------------
# common paths
script_dir              = os.path.dirname(os.path.realpath(__file__))
root_dir                = os.path.dirname(os.path.normpath(script_dir))
android_submodule_dir   = '{0}/ext/android'.format(root_dir)
ios_submodule_dir       = '{0}/ext/ios'.format(root_dir)

# ------------------------------------------------------------------
# call platform specific build method
with_test_lib = args.withtestlib or (args.type == 'b&r' and args.apptype == 'test')
try:
    if args.platform == 'ios':
        if args.type == 'build' or args.type == 'b&r':
            set_log_tag('IOS-SDK-BUILD')
            check_submodule_dir('iOS', ios_submodule_dir + '/sdk')
            ios_builder.build(root_dir, ios_submodule_dir, with_test_lib)
        elif args.type == 'run' or args.type == 'b&r':
            set_log_tag('IOS-SDK-RUN')
            ios_runner.run(root_dir, ios_submodule_dir, args.apptype)
    else:
        if args.type == 'build' or args.type == 'b&r':
            set_log_tag('ANROID-SDK-BUILD')
            check_submodule_dir('Android', android_submodule_dir + '/sdk')
            android_builder.build(root_dir, android_submodule_dir, with_test_lib)
        elif args.type == 'run' or args.type == 'b&r':
            set_log_tag('ANDROID-SDK-RUN')
            android_runner.run(root_dir, android_submodule_dir, args.apptype)
finally:
    # remove autocreated python compiled files
    remove_files('*.pyc', script_dir, log=False)

# ------------------------------------------------------------------
# Script completed
debug_green('Script completed!')
