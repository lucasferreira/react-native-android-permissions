/**
 * @providesModule AndroidPermissions
 */

'use strict';

import { Platform, NativeModules } from 'react-native';

export function checkPermission(perm) {
    return NativeModules.RNPermissionsAndroid.checkPermission(perm)
};

export function requestPermission(perm) {
    if(typeof perm == "string") perm = [perm];

    // permCode greater than 16 bits causes an error so limit code to max of 65535
    let maximum = 65535;
    let minimum = 1;

    let permCode = Math.floor(Math.random() * (maximum - minimum + 1)) + minimum;

    return NativeModules.RNPermissionsAndroid.requestPermission(perm, permCode);
};
