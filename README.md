# NP6 android - SDK 

[![](https://jitpack.io/v/NP6/npush-android.svg)](https://jitpack.io/#NP6/npush-android)

## Introduction 
This library is a part of NP6 Push Notifications service, it allows interactions with users via Push Notifications sent via NP6. 

## Table of content
1. [Prerequisites](#prerequisites)
2. [Installation](#installation)
    * [Firebase Notification Service](#complete-myfirebasemessagingservicejava)
    * [Contact Subscription](#attach-contact-to-device-subscription)
	    * [Native implementation](#example-attaching-device-subscription-by-hash)
	    * [React native implementation](#react-native-implementation)
	    * [Flutter implementation](#flutter-implementation)


## Prerequisites
Here are all the steps needed before installing NPush SDK.

### Java support 
NP6 SDK requires Java 8 support. Ensure that **\<app\>/build.gradle** include following lines:

```gradle
compileOptions {
  sourceCompatibility JavaVersion.VERSION_1_8
  targetCompatibility JavaVersion.VERSION_1_8
}
```

### Firebase Messaging

#### Dependencies 
First of all, application must be declared in Firebase and **google-service.json** file must be placed in the application root directory.
If not please follow this [tutorial](https://firebase.google.com/docs/android/setup).

Add in **build.gradle** google-services dependency. Set the version to **4.3.10**. Reminder : **buildscript** object must be placed before **plugins** object.
```gradle
buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath 'com.google.gms:google-services:4.3.10'
    }
}
```

Then add Firebase messaging dependency in **build.gradle** in your application folder :
```gradle
implementation 'com.google.firebase:firebase-messaging:23.0.8'
```

And check if google-services plugin is present :
```gradle
plugins {
    ....
    id 'com.google.gms.google-services'
    ....
}
apply plugin: 'com.google.gms.google-services'
```

#### Create service 

In order to receive firebase messaging events, create a new service.  
For example **MyFirebaseMessagingService.java** : 

```java
import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // NP6 SDK codes that will come later in the Installation part
    }

    @Override
    public void onNewToken(@NonNull String token) {
        // NP6 SDK codes that will come later in the Installation part
    }
}
```

Once the service is created, modify **AndroidManifest.xml** to add the service : 
```xml
<application
    ...
    <service
        android:name=".<path>.MyFirebaseMessagingService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>
    ...
</application
```

Now you can build your application and debug it in an emulator or a physical device. 

If everything is done. You will see the following lines in your application log :

```
I/FirebaseApp: Device unlocked: initializing all Firebase APIs for app [DEFAULT]
I/FirebaseInitProvider: FirebaseApp initialization successful
```

## Installation 

Now that Firebase is configured, import NP6 SDK dependency. 

### Add dependency

In **settings.gradle**, add the np6 github url and your Personnal Access Token :

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
		maven { url 'https://jitpack.io' }
    }
}
```

In **\<app\>/build.gradle** :
```gradle
dependencies {
	implementation 'com.github.NP6:npush-android:latest'
}
```

Sync gradle file and build the project to apply changes. 

### Complete MyFirebaseMessagingService.java

Complete **MyFirebaseMessagingService.java** which was created in the Prerequisite section.
```java
import androidx.annotation.NonNull;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.np6.npush.NPush;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        NPush.Instance().HandleNotification(this, remoteMessage.getData());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        NPush.Instance().SetDeviceToken(this, token);
    }
}
```

### Set configuration informations

In MainActivity class, modify onCreate method with the following lines : 

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Config config = new Config("<your-application-id>", "<your-identity>", "<your-channel>", false);
        NPush.Instance().setConfig(config);

        ...
   }
```

### Initialization

Now that Config is set. Call **initialize** method as follow : 

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        NPush.Instance().initialize(this);
   }
```


### Attach contact to device subscription 

If you want to attach the current device subscription to the logged-in user in your application, you need to have a way to identify the user. This can be achieved through a hash, an ID, or any unique identifier associated with the user in the NP6 platform.

Please note that these identifiers are specific to the NP6 platform. Before proceeding, make sure that you have one of these identifiers in your user representation. This will enable you to attach the device subscription to the correct user.

##### Example attaching device subscription by hash
```java
            
    NPush.Instance().SetContact(context, new ContactHashRepresentation(data.hash));
        
```

##### Example attaching device subscription by unicity
```java
    
    NPush.Instance().SetContact(context, new ContactUnicityRepresentation(data.unicity));
        
```

##### Example attaching device subscription by id
```java
            
    NPush.Instance().SetContact(context, new ContactIdRepresentation(data.id));
        
```

If everything is done. You will see the following lines in your application log :

```
I/np6-messaging: Subscription created successfully
```
 

### React Native Implementation 

To implement NPush SDK in your React Native application, follow these steps:

### Create react package  

Create a new class called NPushPackage as follow : 

```java
public class NPushPackage implements ReactPackage {

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        modules.add(new NPushModule(reactContext));

        return modules;
    }

}
```

Then create a new implementation file called NPushModule in the same folder.

```java
public class NPushModule extends ReactContextBaseJavaModule {
    NPushModule(ReactApplicationContext context) {
        super(context);
    }


    @NonNull
    @Override
    public String getName() {
        return "NPushModule";
    }
}
```

This code sets up the basic structure of the module and exports it with the name "NPushModule".

The native module can then be accessed in JS like this:

```javascript
const {NPushModule} = ReactNative.NativeModules;
```
	
### Implement contact methods   

Add a new function depending on the type of credential you are using.

Example attaching device subscription by id

```java
    @ReactMethod
    public void setContactById(String id) {
        NPush.getInstance().setContact(getReactApplicationContext(), new ContactIdRepresentation(id));
    }
```

If everything is done. You will see the following lines in your application log :

```
I/np6-messaging: Subscription created successfully
```

&nbsp;
&nbsp;
&nbsp;
&nbsp;
&nbsp;
&nbsp;


### Flutter Implementation

### Create Flutter module 

To implement the NPush SDK in Flutter, you can create a new dart class called NPush.dart and add the following method to it:

```dart
class NPush {
  static const platform = MethodChannel('np6.messaging.npush/contact');

  static Future<void> setContactById(String value) async {
    var result = await platform
        .invokeMethod('SetContactById', {"value": value});

    return result;
  }
}
```

This method uses the MethodChannel class to communicate with the native platform, and the setContactById function takes a string parameter, which should be the identifier of the user you want to attach the device subscription to.

To use this method, simply import the NPush.dart file into your Flutter project and call the setContactById function with the appropriate user identifier value.

### Implement native channel

First, create a FlutterMethodChannel with a unique name (in this example, we use np6.messaging.npush/contact). Set the method call handler for this channel to handle incoming method calls.

In this example, we create a SetContactById method call to set the user's contact by ID. We guard against any other method calls and return FlutterMethodNotImplemented if the method name doesn't match.

We then extract the value of the contact from the arguments passed in the method call and set the contact using NPush.Instance().SetContact(this.context, ContactIdRepresentation(value)).

If there are any errors during the process, we return a FlutterError.

```kotlin

        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            "np6.messaging.npush/contact"
        ).setMethodCallHandler { call, result ->

            val value = call.argument<String>("value")

            when (call.method) {
                "SetContactById" -> {
                    NPush.Instance().SetContact(this.context, ContactIdRepresentation(value))
                    result.success(null)
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
	
```

The call the dart module **NPush.dart** inside the flutter application : 

```dart
NPush.setContactById("000T315");
```

If everything is done. You will see the following lines in your application log :

```
I/np6-messaging: Subscription created successfully
```

## Advanced 

### Deeplinking 

In order to handle deeplink by our way, set a custom deeplink interceptor as follow : 

```java

        NPush.Instance().setInterceptor(new DeeplinkInterceptor() {
            @Override
            public TaskStackBuilder getTaskStackBuilder(Context context, String deeplink) {
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                
                //Add activities for stacking if needed.
                // In this example, the stack is : deeplink activity, activity2, activity1
                Intent intentActivity1 = new Intent(context, Activity1.class);
                stackBuilder.addNextIntent(intentActivity1);

                Intent intentActivity2 = new Intent(context, Activity2.class);
                stackBuilder.addNextIntent(intentActivity2);

                Uri uri = Uri.parse(deeplink);
                Intent deeplinkIntent = new Intent(Intent.ACTION_VIEW)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setData(uri);
                stackBuilder.addNextIntent(deeplinkIntent);
                return stackBuilder;
            }
        })
```

