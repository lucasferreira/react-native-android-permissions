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

    let r = Math.random() * (perm[0].length||1);
    let permCode = Math.round(r + (new Date().getTime())/1000);

    return NativeModules.RNPermissionsAndroid.requestPermission(perm, permCode);
};
