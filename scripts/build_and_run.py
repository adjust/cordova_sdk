#!/usr/bin/python
import argparse
from scripting_utils import *
import run_android          as android_runner
import run_ios              as ios_runner

set_log_tag('BUILD-SDK')

if __name__ != "__main__":
    error('Error. Do not import this script, but run it explicitly.')
    exit()

# ------------------------------------------------------------------
# set arguments
parser = argparse.ArgumentParser(description="""
    Script run example or test apps :{) examples:
    run android test app (make sure the app is previously built) -> ./build_and_run.py run -p android -tl
    run ios test app (make sure the app is previously built) -> ./build_and_run.py run -p ios -t test """)
parser.add_argument('type', help='type of operation to be performed. use >br< to run', choices=['clean', 'run', 'br'])
parser.add_argument('-p', '--platform', help='platform on which the scripts will be ran', choices=['android', 'ios'])
parser.add_argument('-t', '--apptype', help='if run is selected, type of the application to be ran', choices=['example', 'test'])
parser.add_argument('-tl', '--withtestlib', help='build test library as well', action='store_true')
args = parser.parse_args()

if args.type != 'clean' and args.platform == None:
    error('Error. Platform [ android | ios ] not selected!', do_exit=True)
if (args.type == 'run' or args.type == 'br' or args.type == 'clean') and args.apptype == None:
    args.apptype = 'example' # -> default application type is 'example'

# ------------------------------------------------------------------
# common paths
script_dir              = os.path.dirname(os.path.realpath(__file__))
root_dir                = os.path.dirname(os.path.normpath(script_dir))

# ------------------------------------------------------------------
# call platform specific build method
with_test_lib = args.withtestlib or (args.type == 'br' and args.apptype == 'test')
try:
    if args.type == 'clean':
        if args.apptype == 'example': clean_example_app(root_dir)
        else: clean_test_app(root_dir)
        exit()
    
    if args.platform == 'ios':
        if args.type == 'run' or args.type == 'br':
            set_log_tag('IOS-SDK-RUN')
            ios_runner.run(root_dir, args.apptype)
    else:
        if args.type == 'run' or args.type == 'br':
            set_log_tag('ANDROID-SDK-RUN')
            android_runner.run(root_dir, args.apptype)
finally:
    # remove autocreated python compiled files
    remove_files('*.pyc', script_dir, log=False)

# ------------------------------------------------------------------
# Script completed
debug_green('Script completed!')
