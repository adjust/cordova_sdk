## Migrate your Adjust SDK for Cordova to 4.29.0 from 3.4.1

### Migration procedure

Go to your project's folder and remove any previous version of the adjust SDK plugin you have.

```
> cordova plugins rm com.adjust.sdk
Uninstalling com.adjust.sdk from android
Uninstalling com.adjust.sdk from ios
```

After you have successfully removed previous version of plugin, install latest version of plugin.

```
> cordova plugin add path_to_folder/cordova_sdk
Installing "com.adjust.sdk" for android
Installing "com.adjust.sdk" for ios
```

In previous versions, you had to copy the adjust `hooks` folder to your project's root folder. By
doing this, you had `hooks/after_prepare/replace_adjust.js` in your project's folder. If 
`after_prepare/replace_adjust.js` is the only content of your `hooks` folder, you can delete entire
`hooks` folder. If you have some other hooks you are using in your app, then delete just 
`replace_adjust.js` file from `hooks/after_prepare` folder.

### SDK initialization

We have changed how you configure and start the adjust SDK. All initial setup is now done with a new 
instance of the `AdjustConfig` object. The following steps should now be taken to configure the adjust SDK:

1. Create an instance of an `AdjustConfig` config object with the app token and environment.
2. Optionally, you can now call methods of the `AdjustConfig` object to specify available options.
3. Launch the SDK by invoking `Adjust.create` with the config object.

Here is an example of how the setup might look before and after the migration:

##### Before

You needed to edit `adjust.json` file and to replace predefined properties with your own.

```javascript
{
    "appToken" : "{yourAppToken}",
    "environment" : "sandbox",
    "logLevel" : "info",
    "enableEventBuffering" : "false"
}
```

`replace_adjust.js` was taking care that values you set in this file be used when your app starts
and the adjust instance was automatically initialized with these values for you.

##### After

```cs
var adjustConfig = new AdjustConfig("{YourAppToken}", AdjustConfig.EnvironmentSandbox);
adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);

Adjust.create(adjustConfig);
```

### Event tracking

We also introduced proper event objects that are set up before they are tracked. Again, an example of how it 
might look like before and after:

##### Before

```javascript
Adjust.trackEvent('{EventToken}');
```

##### After

```javascript
var adjustEvent = new AdjustEvent("{EventToken}");
Adjust.trackEvent(adjustEvent);
```

### Revenue tracking

Revenues are now handled like normal events. You just set a revenue and a currency to track revenues. 
Note that it is no longer possible to track revenues without associated event tokens. You might need 
to create an additional event token in your dashboard.

*Please note* - the revenue format has been changed from a cent float to a whole currency-unit float. 
Current revenue tracking must be adjusted to whole currency units (i.e., divided by 100) in order to 
remain consistent.

##### Before

```cs
Adjust.trackRevenue(1.0, "{EventToken}");
```

##### After

```cs
var adjustEvent = new AdjustEvent("{EventToken}");
adjustEvent.setRevenue(0.01, "EUR");

Adjust.trackEvent(adjustEvent);
```
