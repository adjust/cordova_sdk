#!/usr/bin/env node

/**
 * Applies the iOS plugin variables that select which native pods are installed.
 *
 * Cordova expands plugin variables in a pod's spec but not in its name, so the pods can't be selected from
 * plugin.xml alone. This script runs when the plugin package is installed, before Cordova copies it into the
 * project, and rewrites its plugin.xml according to the variables set in the project's config.xml or package.json:
 *
 * - IOS_GOOGLE_ODM=true replaces the Adjust pod with the Adjust/AdjustGoogleOdm subspec, which bundles the core SDK
 *   with the Google On-device Conversion Measurement (ODM) plugin
 *   (https://dev.adjust.com/en/sdk/ios/plugins/google-odm).
 * - IOS_GOOGLE_ADS_ON_DEVICE_CONVERSION=true also installs the Google ODM framework, for apps that don't already get
 *   it through Google Analytics for Firebase 11.14.0 or later. Ignored unless IOS_GOOGLE_ODM is true.
 *
 * The project's Podfile is only updated when the plugin is (re)installed: after changing a variable, remove and add
 * the plugin again.
 */

const fs = require('fs');
const path = require('path');

const PLUGIN_ID = 'com.adjust.sdk';
const LOG_PREFIX = `[${PLUGIN_ID}]`;

const ADJUST_POD_REGEXP = /<pod name="Adjust(?:\/AdjustGoogleOdm)?" spec="([^"]+)" \/>/;
const ODM_FRAMEWORK_POD_REGEXP = /(?:<!--)?<pod name="GoogleAdsOnDeviceConversion" spec="([^"]+)" \/>(?:-->)?/;

const pluginPath = path.resolve(__dirname, '..');
// Installed as <project>/node_modules/com.adjust.sdk
const projectPath = path.resolve(pluginPath, '..', '..');

function isTrue(value) {
    return value === true || /^\s*true\s*$/i.test(String(value));
}

function readFile(filePath) {
    try {
        return fs.readFileSync(filePath, 'utf-8');
    } catch (e) {
        return null;
    }
}

function attributes(tag) {
    const result = {};
    const attributeRegExp = /([\w-]+)\s*=\s*"([^"]*)"/g;
    let match;
    while ((match = attributeRegExp.exec(tag)) !== null) {
        result[match[1]] = match[2];
    }
    return result;
}

function readPluginXmlDefaults(pluginXml) {
    const variables = {};
    (pluginXml.match(/<preference\b[^>]*>/g) || []).forEach(tag => {
        const {name, default: defaultValue} = attributes(tag);
        if (name) {
            variables[name] = defaultValue;
        }
    });
    return variables;
}

function readConfigXmlVariables() {
    const variables = {};
    const configXml = readFile(path.join(projectPath, 'config.xml'));
    if (!configXml) {
        return variables;
    }
    const pluginRegExp = /<plugin\b([^>]*?)(?:\/>|>([\s\S]*?)<\/plugin>)/g;
    let match;
    while ((match = pluginRegExp.exec(configXml)) !== null) {
        const {name, id} = attributes(match[1]);
        if ((name === PLUGIN_ID || id === PLUGIN_ID) && match[2]) {
            (match[2].match(/<variable\b[^>]*>/g) || []).forEach(tag => {
                const {name: variableName, value} = attributes(tag);
                if (variableName) {
                    variables[variableName] = value;
                }
            });
        }
    }
    return variables;
}

function readPackageJsonVariables() {
    const packageJson = readFile(path.join(projectPath, 'package.json'));
    if (!packageJson) {
        return {};
    }
    try {
        const plugins = (JSON.parse(packageJson).cordova || {}).plugins || {};
        return plugins[PLUGIN_ID] || {};
    } catch (e) {
        console.warn(`${LOG_PREFIX} Failed to parse the project package.json: ${e.message}`);
        return {};
    }
}

function replacePod(pluginXml, regExp, replacement, description) {
    const match = pluginXml.match(regExp);
    if (!match) {
        console.warn(`${LOG_PREFIX} Failed to find the ${description} pod in plugin.xml`);
        return pluginXml;
    }
    return pluginXml.replace(regExp, replacement(match[1]));
}

function main() {
    const pluginXmlPath = path.join(pluginPath, 'plugin.xml');
    const originalPluginXml = readFile(pluginXmlPath);
    if (!originalPluginXml) {
        console.warn(`${LOG_PREFIX} Failed to read ${pluginXmlPath}`);
        return;
    }

    // Project variables override the plugin.xml defaults, package.json wins over config.xml like in Cordova.
    const variables = Object.assign(
        readPluginXmlDefaults(originalPluginXml),
        readConfigXmlVariables(),
        readPackageJsonVariables()
    );
    const googleOdm = isTrue(variables.IOS_GOOGLE_ODM);
    const odmFramework = googleOdm && isTrue(variables.IOS_GOOGLE_ADS_ON_DEVICE_CONVERSION);

    let pluginXml = replacePod(originalPluginXml, ADJUST_POD_REGEXP,
        spec => `<pod name="${googleOdm ? 'Adjust/AdjustGoogleOdm' : 'Adjust'}" spec="${spec}" />`, 'Adjust');
    pluginXml = replacePod(pluginXml, ODM_FRAMEWORK_POD_REGEXP,
        spec => `${odmFramework ? '' : '<!--'}<pod name="GoogleAdsOnDeviceConversion" spec="${spec}" />${odmFramework ? '' : '-->'}`,
        'GoogleAdsOnDeviceConversion');

    console.log(`${LOG_PREFIX} Google ODM ${googleOdm ? 'enabled' : 'disabled'}` +
        (googleOdm ? `, Google ODM framework ${odmFramework ? 'installed by the plugin' : 'expected from the app'}` : ''));

    if (pluginXml !== originalPluginXml) {
        fs.writeFileSync(pluginXmlPath, pluginXml, 'utf-8');
        console.log(`${LOG_PREFIX} Updated the iOS pods in plugin.xml`);
    }
}

try {
    main();
} catch (e) {
    // Never fail the installation: the plugin keeps its default pods.
    console.warn(`${LOG_PREFIX} Failed to apply the iOS plugin variables: ${e.message}`);
}
