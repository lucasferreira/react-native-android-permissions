package com.burnweb.rnpermissions;

import android.app.Activity;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RNPermissionsModule extends ReactContextBaseJavaModule {

    private static final String TAG = RNPermissionsModule.class.getSimpleName();
    private static final String SOME_GRANTED = "SOME_GRANTED";
    private static final String ALL_GRANTED = "ALL_GRANTED";
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_PERMISSION_DENIED = "E_PERMISSION_DENIED";

    private static HashMap<Integer, Promise> requestPromises = new HashMap<>();
    private static HashMap<Integer, WritableNativeMap> requestResults = new HashMap<>();

    public RNPermissionsModule(ReactApplicationContext reactContext) {
      super(reactContext);
    }

    @Override
    public String getName() {
      return "RNPermissionsAndroid";
    }

    @ReactMethod
    public void checkPermission(final String perm, final Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        int hasPermission = ContextCompat.checkSelfPermission(currentActivity, perm);
        if(hasPermission == PackageManager.PERMISSION_GRANTED) {
            promise.resolve(perm);
        } else {
            promise.reject(E_PERMISSION_DENIED, "Permission was not granted");
        }
    }

    @ReactMethod
    public void requestPermission(final ReadableArray permsArray, final int reqCode, final Promise promise) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        WritableNativeMap q = new WritableNativeMap();

        List<String> permList = new ArrayList<>();
        for (int i=0; i<permsArray.size(); i++) {
            int hasPermission = ContextCompat.checkSelfPermission(currentActivity, permsArray.getString(i));
            if(hasPermission != PackageManager.PERMISSION_GRANTED) {
                permList.add(permsArray.getString(i)); // list to request
                q.putBoolean(permsArray.getString(i), false);
            } else {
                q.putBoolean(permsArray.getString(i), true); // already granted
            }
        }

        if(permList.size() > 0)
        {
            requestPromises.put(reqCode, promise);
            requestResults.put(reqCode, q);

            String[] perms = permList.toArray(new String[permList.size()]);
            ActivityCompat.requestPermissions(currentActivity, perms, reqCode);
        }
        else
        {
            WritableNativeMap result = new WritableNativeMap();
            result.putString("code", ALL_GRANTED);
            result.putMap("result", q);

            promise.resolve(result);
        }
    }

    protected static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestPromises.containsKey(requestCode) && requestResults.containsKey(requestCode))
        {
            Promise promise = requestPromises.get(requestCode);
            WritableNativeMap q = requestResults.get(requestCode);

            for(int i=0; i<permissions.length; i++) {
                q.putBoolean(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }

            int countGranted = 0;
            int totalPerms = 0;
            ReadableMapKeySetIterator itr = q.keySetIterator();
            while(itr.hasNextKey()) {
                String permKey = itr.nextKey();
                if(q.hasKey(permKey) && q.getBoolean(permKey) == true)
                {
                    countGranted++;
                }
                totalPerms++;
            }

            if(countGranted > 0)
            {
                WritableNativeMap result = new WritableNativeMap();
                result.putString("code", countGranted == totalPerms ? ALL_GRANTED : SOME_GRANTED);
                result.putMap("result", q);

                promise.resolve(result); // some or all of permissions was granted...
            }
            else
            {
                promise.reject(E_PERMISSION_DENIED, "Permission request denied");
            }

            requestPromises.remove(requestCode);
            requestResults.remove(requestCode);
        }
    }

}
