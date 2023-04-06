# NP6 android - SDK 

[![](https://jitpack.io/v/NP6/npush-android.svg)](https://jitpack.io/#NP6/npush-android)

## Introduction 
This library is a part of NP6 Push Notifications service, it allows interactions with users via Push Notifications sent via NP6 CM. 

## Table of content
1.	[Prerequisites](#prerequisites)
2.	[Installation](#installation)
3.	[Troubleshooting](#troubleshooting)

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

Before the next steps be sure to have the following informations :

* np6 application id (uuid v4)
* np6 identity (string)
* a default notification channel
* activities that will be used in the stackBuilder for deeplink


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

Suppose we have an application with a login & register form and we want to attach the current device subscription to the logged user.
We can only identify the users by hash, id or unicity criteria. 

**Note : All of these identifiers are strongly linked to the NP6 CM platform.**

Please be sure to have one of this 3 identifiers in your user representation before continue. 

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

## Troubleshooting

### Common issues 

In this part we will see how solve commons problems.  


If after calling **NPush.Instance().setContact(...)** one of the following message appears :

```
E/np6-messaging: context must be specified
```

Solution : Please ensure arguments passed to the function are not null

```
E/np6-messaging: linked must be specified
```

Solution : Please ensure arguments passed to the function are not null

```
E/np6-messaging: config must be specified
```

Solution : Ensure NPush.Instance().setConfig method is called before call setContact

```
E/np6-messaging: Subscription creation failed with http status code ...
```
Solution : Verify device network state and network security policy. If problem persist call np6 support.


### Error messages on calling **NPush.Instance().initialize()**

If after calling **initialize** one of the following message appears :

```
E/np6-messaging: context must be specified
``` 
Solution : Please ensure arguments passed to the function are not null

```
E/np6-messaging: config must be specified
```
Solution : Ensure NPush.Instance().setConfig method is called before call setContact


If after calling **NPush.Instance().handleNotification(..)** one of the following message appears :


```
E/np6-messaging: context must be specified
``` 
Solution : Please ensure arguments passed to the function are not null

```
E/np6-messaging: config must be specified
```
Solution : Ensure NPush.Instance().setConfig method is called before call setContact
