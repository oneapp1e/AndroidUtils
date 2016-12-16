/*
 * File Name: LogUtils.java 
 * History:
 * Created by Siyang.Miao on 2012-2-20
 */
package com.mlr.utils;


import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtils {
    //==========================================================================
    // Constants
    //==========================================================================
    // Max value is 3984
    private static final int MAX_LOG_LINE_LENGTH = 2048 << 1;

    // 将日志写入文件时是否加密
    private static final boolean ENCRYPT_LOG = false;

    //==========================================================================
    // Fields
    //==========================================================================
    private static String sTag = "mlr";
    private static boolean sDebuggable = true;
    private static long sTimestamp = 0;
    private static Object sLogLock = new Object();

    //==========================================================================
    // Constructors
    //==========================================================================

    //==========================================================================
    // Getters
    //==========================================================================

    //==========================================================================
    // Setters
    //==========================================================================

    //==========================================================================
    // Methods
    //==========================================================================
    public static void setTag(String tag) {
        sTag = tag;
    }

    public static void i(String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                int start = 0;
                int end = 0;
                int len = msg.length();
                while (true) {
                    start = end;
                    end = start + MAX_LOG_LINE_LENGTH;
                    if (end >= len) {
                        Log.i(sTag, msg.substring(start, len));
                        break;
                    } else {
                        Log.i(sTag, msg.substring(start, end));
                    }
                }
            } else {
                Log.i(sTag, msg);
            }
        }
    }

    public static void v(String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                int start = 0;
                int end = 0;
                int len = msg.length();
                while (true) {
                    start = end;
                    end = start + MAX_LOG_LINE_LENGTH;
                    if (end >= len) {
                        Log.v(sTag, msg.substring(start, len));
                        break;
                    } else {
                        Log.v(sTag, msg.substring(start, end));
                    }
                }
            } else {
                Log.v(sTag, msg);
            }
        }
    }

    public static void d(String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                int start = 0;
                int end = 0;
                int len = msg.length();
                while (true) {
                    start = end;
                    end = start + MAX_LOG_LINE_LENGTH;
                    if (end >= len) {
                        Log.d(sTag, msg.substring(start, len));
                        break;
                    } else {
                        Log.d(sTag, msg.substring(start, end));
                    }
                }
            } else {
                Log.d(sTag, msg);
            }
        }
    }

    public static void w(String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                int start = 0;
                int end = 0;
                int len = msg.length();
                while (true) {
                    start = end;
                    end = start + MAX_LOG_LINE_LENGTH;
                    if (end >= len) {
                        Log.w(sTag, msg.substring(start, len));
                        break;
                    } else {
                        Log.w(sTag, msg.substring(start, end));
                    }
                }
            } else {
                Log.w(sTag, msg);
            }
        }
    }

    public static void w(Throwable tr) {
        if (sDebuggable) {
            Log.w(sTag, "", tr);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (sDebuggable && null != msg) {
            Log.w(sTag, msg, tr);
        }
    }

    public static void e(String msg) {
        if (sDebuggable) {
            if (null != msg && msg.length() > 0) {
                int start = 0;
                int end = 0;
                int len = msg.length();
                while (true) {
                    start = end;
                    end = start + MAX_LOG_LINE_LENGTH;
                    if (end >= len) {
                        Log.e(sTag, msg.substring(start, len));
                        break;
                    } else {
                        Log.e(sTag, msg.substring(start, end));
                    }
                }
            } else {
                Log.e(sTag, msg);
            }
        }
    }

    public static void e(Throwable tr) {
        if (sDebuggable) {
            Log.e(sTag, "", tr);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (sDebuggable) {
            Log.e(sTag, msg, tr);
        }
    }

    public static void markStart(String msg) {
        sTimestamp = System.currentTimeMillis();
        if (!TextUtils.isEmpty(msg)) {
            e("[Started|" + sTimestamp + "]" + msg);
        }
    }

    public static void elapsed(String msg) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - sTimestamp;
        sTimestamp = currentTime;
        e("[Elapsed|" + elapsedTime + "]" + msg);
    }

    public static boolean isDebugable() {
        return sDebuggable;
    }

    public static void setDebugable(boolean debugable) {
        sDebuggable = debugable;
    }

    public static void log2File(String log, String path) {
        log2File(log, path, true);
    }

    public static void log2File(String log, String path, boolean append) {
        if (ENCRYPT_LOG) {
            // Base64简单加密
            log = Base64.encodeToString(log.getBytes(), Base64.NO_WRAP);
        }
        synchronized (sLogLock) {
            FileUtils.writeFile(log + "\r\n", path, append);
        }
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    //==========================================================================
    // Inner/Nested Classes
    //==========================================================================
}
