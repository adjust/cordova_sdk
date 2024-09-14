function handleOpenURL(url) {
    setTimeout(function() {
        console.log(`TestApp, handleOpenURL: initiate Adjust.processDeeplink, with URL = ${url}`);
        var adjustDeeplink = new AdjustDeeplink(url);
        Adjust.processDeeplink(adjustDeeplink);
    }, 0);
}

var app = {
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    onDeviceReady: function() {
        this.receivedEvent('deviceready');

        Adjust.getSdkVersion(function(sdkVersion) {
            // Register for universal links.
            if (device.platform == 'iOS') {
                universalLinks.subscribe('adjustDeepLinking', app.didLaunchAppFromLink);
            }

            var urlOverwrite = "";
            var ipAddress = "192.168.86.80";
            if (device.platform === "Android") {
                urlOverwrite = "https://" + ipAddress + ":8443";
            } else if (device.platform === "iOS") {
                urlOverwrite = "http://" + ipAddress + ":8080";
            }
            var controlUrl = "ws://" + ipAddress + ":1987";

            var commandExecutor = new CommandExecutor(urlOverwrite);
            // AdjustTest.addTestDirectory('purchase-verification');

            AdjustTest.startTestSession(urlOverwrite, controlUrl, sdkVersion, function(json) {
                var commandDict = JSON.parse(json);
                var className = commandDict['className'];
                var functionName = commandDict['functionName'];
                var params = commandDict['params'];
                var order = commandDict['order'];
                commandExecutor.scheduleCommand(className, functionName, params, order);
            });
        });
    },

    didLaunchAppFromLink: function(eventData) {
        console.log(`TestApp, didLaunchAppFromLink: initiate Adjust.processDeeplink, with URL = ${eventData.url}`);
        Adjust.processDeeplink(eventData.url);
    },

    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');
        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');
        console.log('Received Event: ' + id);
    }
};

app.initialize();
