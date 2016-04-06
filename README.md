# react-native-android-permissions
React Native Android Permissions *experimental module* - Android M (6.0) permissions like to your React Native application.

### Installation

```bash
npm install react-native-android-permissions --save
```

### Add it to your android project

* In `android/setting.gradle`

```gradle
...
include ':RNPermissionsModule', ':app'
project(':RNPermissionsModule').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-android-permissions')
```

* In `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':RNPermissionsModule')
}
```

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

## Example
TO-DO.

## License
MIT
