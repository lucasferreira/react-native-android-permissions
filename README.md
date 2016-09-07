# react-native-android-permissions
React Native Android Permissions *experimental module* - Android M (6.0) permissions like to your React Native application.

If you need to work with Android M (6.0+) permissions model this experimental module that can be helpful to you. This works with only two methods: *checkPermission* and *requestPermission*.

**FUTURE DEPRECATION WARNING:** The official RN *PermissionsAndroid* will be realized soon (https://github.com/facebook/react-native/pull/9292) and the future documentation are [here](http://facebook.github.io/react-native/releases/next/docs/permissionsandroid.html). So this module will be deprecated after that.

First of all you need to set in your *android/app/build.gradle* file the *targetSdkVersion* to **23** or a major SDK version:

```gradle
...
  defaultConfig {
      applicationId "com.awesomepermissions"
      minSdkVersion 16
      targetSdkVersion 23 // <--- this change
      versionCode 1
      versionName "1.0"
      ndk {
          abiFilters "armeabi-v7a", "x86"
      }
  }
```

After that, when you're testing your app in development mode, you have to allow the "Draw over other apps" permission:

![Setting the draw over permission](http://i.imgur.com/rdUzj0w.gif)

The recommended way of deal with permissions in Android is to preview and list all the permissions in your *AndroidManifest.xml* file:

```xml
...
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
...
```

Before that you can ask some of then in run time, when you need.

![Demo](http://i.imgur.com/bdMGD3d.gif)

### Installation

```bash
npm install react-native-android-permissions --save
```

### Add it to your android project

* In `android/setting.gradle`

```gradle
...
include ':RNPermissionsModule', ':app'
project(':RNPermissionsModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-permissions/android')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':RNPermissionsModule')
}
```

**React Native >= 0.29:**

*MainActivity.java:*

```java
import com.burnweb.rnpermissions.RNPermissionsPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      RNPermissionsPackage.onRequestPermissionsResult(requestCode, permissions, grantResults); // very important event callback
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  ......

}
```

*MainApplication.java:*

```java
import com.burnweb.rnpermissions.RNPermissionsPackage;  // <--- import

public class MainApplication extends Application implements ReactApplication {
  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
  ......

  @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(),
        new RNPermissionsPackage() // <------ add this line to your MainApplication class
      );
    }

  ......

}
```


**React Native <= 0.28:**

*MainActivity.java:*

```java
import com.burnweb.rnpermissions.RNPermissionsPackage;  // <--- import

public class MainActivity extends ReactActivity {
  ......

  @Override
  protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new RNPermissionsPackage()); // <------ add this line to your MainActivity class
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
      RNPermissionsPackage.onRequestPermissionsResult(requestCode, permissions, grantResults); // very important event callback
      super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  ......

}
```


## Example of checkPermission method:

```javascript
import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View
} from 'react-native';

import {checkPermission} from 'react-native-android-permissions';

class AwesomePermissions extends Component {
  componentDidMount() {
    checkPermission("android.permission.ACCESS_FINE_LOCATION").then((result) => {
      console.log("Already Granted!");
      console.log(result);
    }, (result) => {
      console.log("Not Granted!");
      console.log(result);
    });
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('AwesomePermissions', () => AwesomePermissions);
```

## Example of requestPermission method:

```javascript
import React, {
  AppRegistry,
  Component,
  StyleSheet,
  Text,
  View
} from 'react-native';

import {requestPermission} from 'react-native-android-permissions';

class AwesomePermissions extends Component {
  componentDidMount() {
    setTimeout(() => {
      requestPermission("android.permission.ACCESS_FINE_LOCATION").then((result) => {
        console.log("Granted!", result);
        // now you can set the listenner to watch the user geo location
      }, (result) => {
        console.log("Not Granted!");
        console.log(result);
      });
    // for the correct StatusBar behaviour with translucent={true} we need to wait a bit and ask for permission after the first render cycle
    // (check https://github.com/facebook/react-native/issues/9413 for more info)
    }, 0);
  }
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('AwesomePermissions', () => AwesomePermissions);
```

## License
MIT
