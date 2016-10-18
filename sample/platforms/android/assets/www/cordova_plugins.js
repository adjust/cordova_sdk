cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
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
    "com.adjust.sdk": "4.10.0"
};
// BOTTOM OF METADATA
});