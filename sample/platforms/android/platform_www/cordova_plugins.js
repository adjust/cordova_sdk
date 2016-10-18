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
        "id": "cordova-plugin-dialogs.notification_android",
        "file": "plugins/cordova-plugin-dialogs/www/android/notification.js",
        "pluginId": "cordova-plugin-dialogs",
        "merges": [
            "navigator.notification"
        ]
    },
    {
        "id": "cordova-plugin-customurlscheme.LaunchMyApp",
        "file": "plugins/cordova-plugin-customurlscheme/www/android/LaunchMyApp.js",
        "pluginId": "cordova-plugin-customurlscheme",
        "clobbers": [
            "window.plugins.launchmyapp"
        ]
    },
    {
        "id": "cordova-plugin-device.device",
        "file": "plugins/cordova-plugin-device/www/device.js",
        "pluginId": "cordova-plugin-device",
        "clobbers": [
            "device"
        ]
    },
    {
        "id": "cordova-universal-links-plugin.universalLinks",
        "file": "plugins/cordova-universal-links-plugin/www/universal_links.js",
        "pluginId": "cordova-universal-links-plugin",
        "clobbers": [
            "universalLinks"
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
    "cordova-plugin-whitelist": "1.3.0",
    "cordova-plugin-dialogs": "1.3.0",
    "cordova-plugin-console": "1.0.4",
    "cordova-plugin-customurlscheme": "4.2.0",
    "cordova-plugin-device": "1.1.3",
    "cordova-universal-links-plugin": "1.2.0",
    "com.adjust.sdk": "4.10.0"
};
// BOTTOM OF METADATA
});