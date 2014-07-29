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

    var filePaths= [
        // android
        "platforms/android/assets/www/plugins/com.adjust.sdk/www/adjust.js",
        // ios
        "platforms/ios/www/plugins/com.adjust.sdk/www/adjust.js"
    ];

    filePaths.forEach(function(val, index, array) {
        var fullFilename = path.join(rootDir, val);
        if (fs.existsSync(fullFilename)) {
            for(key in configJson) {
                var adjust_key = "{adjust_" + key + "}";
                replaceStringInFile(fullFilename, adjust_key, configJson[key]);
            }
        } else {
            console.log("replace_adjust missing target file: " + fullFilename);
        }
    });
}

main();
