#!/usr/bin/env node

var fs = require('fs');
var path = require('path');

function replaceStringInFile(filename, to_replace, replace_with) {
    var data = fs.readFileSync(filename, 'utf8');

    var result = data.replace(new RegExp(to_replace, "g"), replace_with);
    fs.writeFileSync(filename, result, 'utf8');
}

function getRootDir() {
    var rootDir = process.argv[2];
    if (!rootDir){
        console.log("replace_adjust missing root folder: " + rootDir);
    }
    return rootDir;
}

function getConfigJson(rootDir) {
    var configFilename = path.join(rootDir, "plugins/com.adjust.sdk/config", "adjust.json");
    var configJson = null;

    if (!fs.existsSync(configFilename)) {
        console.log("replace_adjust missing config file: " + configFilename);
        return null;
    }

    try {
        var configFile = fs.readFileSync(configFilename, 'utf8');
        var configJson = JSON.parse(configFile);
    } catch(err) {
        console.log("replace_adjust err:", err.message);
        return null;
    }

    return configJson;
}

function main() {
    var rootDir = getRootDir();
    if (!rootDir) { return; }

    var configJson = getConfigJson(rootDir);
    if (!configJson) { return; }

    var androidPath = "platforms/android/assets/www/plugins/com.adjust.sdk/www/adjust.js";
    var iOsPath = "platforms/ios/www/plugins/com.adjust.sdk/www/adjust.js";

    replaceFiles([androidPath], configJson, rootDir, "_android");
    replaceFiles([iOsPath], configJson, rootDir, "_ios");
    replaceFiles([androidPath, iOsPath], configJson, rootDir);
}

function replaceFiles(filePaths, configJson, rootDir, suffix) {
    for (var i_path = 0; i_path < filePaths.length; i_path++) {
        var fullFilename = path.join(rootDir, filePaths[i_path]);
        if (fs.existsSync(fullFilename)) {
            for (key in  configJson) {
                if (suffix) {
                    if (key.match(suffix + "$") == suffix) {
                        var suffix_regex = new RegExp(suffix + "$");
                        var key_no_suffix = key.replace(suffix_regex, "");
                        var adjust_key = "{adjust_" + key_no_suffix + "}";
                        replaceStringInFile(fullFilename, adjust_key, configJson[key]);
                    }
                } else {
                    var adjust_key = "{adjust_" + key + "}";
                    replaceStringInFile(fullFilename, adjust_key, configJson[key]);
                }
            }
        } else {
            console.log("replace_adjust missing target file: " + fullFilename);
        }
    }
}

main();
