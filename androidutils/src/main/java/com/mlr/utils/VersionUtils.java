/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mlr.utils;

import android.os.Build;

/**
 * 对比当前的系统版本
 */
public class VersionUtils {
    private VersionUtils() {
    };

    /**
     * October 2008: The original, first, version of Android. Yay!
     */
    public static final int BASE = 1;

    /**
     * February 2009: First Android update, officially called 1.1.
     */
    public static final int BASE_1_1 = 2;

    /**
     * May 2009: Android 1.5.
     */
    public static final int CUPCAKE = 3;

    /**
     * September 2009: Android 1.6.
     */
    public static final int DONUT = 4;

    /**
     * November 2009: Android 2.0
     */
    public static final int ECLAIR = 5;

    /**
     * December 2009: Android 2.0.1
     */
    public static final int ECLAIR_0_1 = 6;

    /**
     * January 2010: Android 2.1
     */
    public static final int ECLAIR_MR1 = 7;

    /**
     * June 2010: Android 2.2
     */
    public static final int FROYO = 8;

    /**
     * November 2010: Android 2.3
     */
    public static final int GINGERBREAD = 9;

    /**
     * February 2011: Android 2.3.3.
     */
    public static final int GINGERBREAD_MR1 = 10;

    /**
     * February 2011: Android 3.0.
     */
    public static final int HONEYCOMB = 11;

    /**
     * May 2011: Android 3.1.
     */
    public static final int HONEYCOMB_MR1 = 12;

    /**
     * June 2011: Android 3.2.
     */
    public static final int HONEYCOMB_MR2 = 13;

    /**
     * October 2011: Android 4.0.
     */
    public static final int ICE_CREAM_SANDWICH = 14;

    /**
     * December 2011: Android 4.0.3.
     */
    public static final int ICE_CREAM_SANDWICH_MR1 = 15;

    /**
     * June 2012: Android 4.1.
     * 
     */
    public static final int JELLY_BEAN = 16;

    /**
     * November 2012: Android 4.2, Moar jelly beans!
     */
    public static final int JELLY_BEAN_MR1 = 17;

    /**
     * July 2013: Android 4.3, the revenge of the beans.
     */
    public static final int JELLY_BEAN_MR2 = 18;

    /**
     * October 2013: Android 4.4, KitKat, another tasty treat.
     */
    public static final int KITKAT = 19;

    /**
     * Android 4.4W: KitKat for watches, snacks on the run.
     */
    public static final int KITKAT_WATCH = 20;

    /**
     * Temporary until we completely switch to {@link #LOLLIPOP}.
     */
    public static final int L = 21;

    /**
     * Lollipop. A flat one with beautiful shadows. But still tasty.
     */
    public static final int LOLLIPOP = 21;

    /**
     * Marshmallow.6.0
     */
    public static final int MARSHMALLOW = 23;

    public static boolean Support(int sdkVersionCode) {
        return Build.VERSION.SDK_INT >= sdkVersionCode;
    }

    /**
     * 2.2
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= 8;
    }

    /**
     * 2.3
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    /**
     * 3.0
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    /**
     * 3.1
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;
    }

    /**
     * 4.0
     */
    public static boolean hasIceCremaSandwich() {
        return Build.VERSION.SDK_INT >= 14;
    }

    /**
     * 4.1
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    /**
     * 4.4
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= KITKAT;
    }

    /**
     * 5.0
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= LOLLIPOP;
    }

    /**
     * 6.0
     */
    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= MARSHMALLOW;
    }

}
