function handleOpenURL(url) {
    setTimeout(function() {
        console.log(`TestApp, handleOpenURL: initiate Adjust.appWillOpenUrl, with URL = ${url}`);
        Adjust.appWillOpenUrl(url);
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

            var baseUrl = "";
            var gdprUrl = "";
            var subscriptionUrl = "";
            var ipAddress = "192.168.86.67";
            if (device.platform === "Android") {
                baseUrl = "https://" + ipAddress + ":8443";
                gdprUrl = "https://" + ipAddress + ":8443";
                subscriptionUrl = "https://" + ipAddress + ":8443";
            } else if (device.platform === "iOS") {
                baseUrl = "http://" + ipAddress + ":8080";
                gdprUrl = "http://" + ipAddress + ":8080";
                subscriptionUrl = "http://" + ipAddress + ":8080";
            }
            var controlUrl = "ws://" + ipAddress + ":1987";

            var commandExecutor = new CommandExecutor(baseUrl, gdprUrl, subscriptionUrl);
            AdjustTest.startTestSession(baseUrl, controlUrl, sdkVersion, function(json) {
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
        console.log(`TestApp, didLaunchAppFromLink: initiate Adjust.appWillOpenUrl, with URL = ${eventData.url}`);
        Adjust.appWillOpenUrl(eventData.url);
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
