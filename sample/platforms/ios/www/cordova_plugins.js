cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "id": "cordova-plugin-dialogs.notification",
        "file": "plugins/cordova-plugin-dialogs/www/notification.js",
        "pluginId": "cordova-plugin-dialogs",
        "merges": [
            "navigator.notification"
        ]
    },
    {
        "id": "cordova-plugin-console.console",
        "file": "plugins/cordova-plugin-console/www/console-via-logger.js",
        "pluginId": "cordova-plugin-console",
        "clobbers": [
            "console"
        ]
    },
    {
        "id": "cordova-plugin-console.logger",
        "file": "plugins/cordova-plugin-console/www/logger.js",
        "pluginId": "cordova-plugin-console",
        "clobbers": [
            "cordova.logger"
        ]
    },
    {
        "id": "cordova-plugin-customurlscheme.LaunchMyApp",
        "file": "plugins/cordova-plugin-customurlscheme/www/ios/LaunchMyApp.js",
        "pluginId": "cordova-plugin-customurlscheme",
        "clobbers": [
            "window.plugins.launchmyapp"
        ]
    },
    {
        "id": "com.adjust.sdk.Adjust",
        "file": "plugins/com.adjust.sdk/www/adjust.js",
        "pluginId": "com.adjust.sdk",
        "clobbers": [
            "Adjust"
        ]
    },
    {
        "id": "com.adjust.sdk.AdjustConfig",
        "file": "plugins/com.adjust.sdk/www/adjust_config.js",
        "pluginId": "com.adjust.sdk",
        "clobbers": [
            "AdjustConfig"
        ]
    },
    {
        "id": "com.adjust.sdk.AdjustEvent",
        "file": "plugins/com.adjust.sdk/www/adjust_event.js",
        "pluginId": "com.adjust.sdk",
        "clobbers": [
            "AdjustEvent"
        ]
    }
];
module.exports.metadata = 
// TOP OF METADATA
{
    "cordova-plugin-dialogs": "1.3.0",
    "cordova-plugin-whitelist": "1.3.0",
    "cordova-plugin-console": "1.0.4",
    "cordova-plugin-customurlscheme": "4.2.0",
    "com.adjust.sdk": "4.10.0"
};
// BOTTOM OF METADATA
});