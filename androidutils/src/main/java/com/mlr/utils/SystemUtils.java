/*
 * File Name: SystemUtils.java 
 * History:
 * Created by Siyang.Miao on 2012-2-20
 */
package com.mlr.utils;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SystemUtils {
    // ==========================================================================
    // Constants
    // ==========================================================================
    private static final String KEY_PROP_ABI = "ro.product.cpu.abi";
    private static final String KEY_PROP_ABI2 = "ro.product.cpu.abi2";
    private static final String KEY_PROP_ABI_LIST = "ro.product.cpu.abilist";
    private static final String KEY_PROP_SERIALNO = "ro.serialno";
    private static final String KEY_PROP_BOOT_SERIALNO = "ro.boot.serialno";
    private static final String KEY_WIFI_INTERFACE = "wifi.interface";

    private static final String STR_ABI_ARMEABI = "armeabi";
    private static final String STR_ABI_ARMEABI_V7A = "armeabi-v7a";
    private static final String STR_ABI_ARM64_V8A = "arm64-v8a";
    private static final String STR_ABI_X86 = "x86";
    private static final String STR_ABI_X86_64 = "x86_64";
    private static final String STR_ABI_MIPS = "mips";
    private static final String STR_ABI_MIPS64 = "mips64";

    private static final String KERNEL_PATH = "/proc/version";
    private static final String CPU_INFO_PATH = "/proc/cpuinfo";
    private static final String CPU_MAX_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq";
    private static final String CPU_MIN_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq";
    private static final String CPU_CUR_FREQ_PATH = "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq";

    public static final int ABI_ARMEABI = 0x1;
    public static final int ABI_ARMEABI_V7A = 0x2;
    public static final int ABI_X86 = 0x4;
    public static final int ABI_MIPS = 0x8;
    public static final int ABI_ARM64_V8A = 0x10;
    public static final int ABI_X86_64 = 0x20;
    public static final int ABI_MIPS64 = 0x40;

    // ==========================================================================
    // Fields
    // ==========================================================================
    private static StatFs sStatFs = null;

    // ==========================================================================
    // Constructors
    // ==========================================================================

    // ==========================================================================
    // Getters
    // ==========================================================================

    // ==========================================================================
    // Setters
    // ==========================================================================

    // ==========================================================================
    // Methods
    // ==========================================================================

    /**
     * 取得当前设备CPU的ABI
     *
     * @return ABI | ABI2 or {@link Build#SUPPORTED_ABIS}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int getABI() {
        int abi = 0;
        if (VersionUtils.hasLollipop()) {
            String[] abis = Build.SUPPORTED_ABIS;
            if (abis != null) {
                for (String strAbi : abis) {
                    abi |= matchABI(strAbi);
                }
            }
            if (abi > 0) {
                return abi;
            }
        }
        return matchABI(getSystemProperty(KEY_PROP_ABI)) | matchABI(getSystemProperty(KEY_PROP_ABI2));
    }

    private static int matchABI(String abiString) {
        if (TextUtils.isEmpty(abiString)) {
            return 0;
        }
        if (STR_ABI_ARMEABI.equals(abiString)) {
            return ABI_ARMEABI;
        } else if (STR_ABI_ARMEABI_V7A.equals(abiString)) {
            return ABI_ARMEABI_V7A;
        } else if (STR_ABI_ARM64_V8A.equals(abiString)) {
            return ABI_ARM64_V8A;
        } else if (STR_ABI_X86.equals(abiString)) {
            return ABI_X86;
        } else if (STR_ABI_X86_64.equals(abiString)) {
            return ABI_X86_64;
        } else if (STR_ABI_MIPS.equals(abiString)) {
            return ABI_MIPS;
        } else if (STR_ABI_MIPS64.equals(abiString)) {
            return ABI_MIPS64;
        }
        return 0;
    }

    public static String getABIStr() {
        return getSystemProperty(KEY_PROP_ABI_LIST);
    }

    // public static String getRomId() {
    // String rom = "";
    // try {
    // String modversion = GOAPKGF.getSystemProperty("ro.modversion");
    // String buildId = GOAPKGF.getSystemProperty("ro.build.display.id");
    // if (modversion != null && !modversion.equals("")) {
    // rom = modversion;
    // }
    // if (buildId != null && !buildId.equals("")) {
    // rom = buildId;
    // }
    // } catch (Exception e) {
    // LogUtils.e(e);
    // }
    // return rom;
    // }

    /**
     * 获取手机序列号
     *
     * @return
     */
    public static String getSerialno() {
        String serialno = getSystemProperty(KEY_PROP_SERIALNO);
        if (StringUtils.isEmpty(serialno)) {
            serialno = getSystemProperty(KEY_PROP_BOOT_SERIALNO);
        }
        if (StringUtils.isEmpty(serialno)) {
            serialno = "";
        }
        return serialno;
    }

    /**
     * 获取系统配置参数
     *
     * @param key 参数key
     * @return 参数值
     */
    public static final String getSystemProperty(String key) {
        String pValue = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method m = c.getMethod("get", String.class);
            pValue = m.invoke(null, key).toString();
        } catch (ClassNotFoundException e) {
            LogUtils.e(e);
        } catch (SecurityException e) {
            LogUtils.e(e);
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return pValue;
    }

    public static String getIMEI(Context context) {
        if (null == context) {
            return null;
        }
        String imei = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = tm.getDeviceId();
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return imei;
    }


    /**
     * 获取设备的ANDROID_ID。<br/>
     * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来，
     * 这个16进制的字符串就是ANDROID_ID，当设备被wipe后该值会被重置。
     *
     * @param context 上下文
     * @return 设备的ANDROID_ID。若无法正常读取会返回 0000000000000000。
     */
    public static String getAndroidId(Context context) {
        String androidId = null;// 指定一个默认值
        if (null == context) {
            return androidId;
        }

        try {
            androidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return androidId;
    }

    /**
     * 取得当前sim卡的imsi
     *
     * @param context
     * @return Return null if it is unavailable.
     */
    public static String getIMSI(Context context) {
        if (null == context) {
            return null;
        }
        String imsi = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return imsi;
    }

    public static String getSimSN(Context context) {
        if (null == context) {
            return null;
        }
        String simSN = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            simSN = tm.getSimSerialNumber();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return simSN;
    }

    public static String getTel(Context context) {
        if (null == context) {
            return null;
        }
        String tel = null;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            tel = tm.getLine1Number();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return tel;
    }

    /**
     * 获取Wifi’Mac地址，可以使用getWifiMacAddress和getEth0MacAddress来替代
     *
     * @param context
     * @return wifi'Mac
     */
    @Deprecated
    public static String getMacAddress(Context context) {
        if (null == context) {
            return null;
        }
        String mac = null;
        try {
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return mac;
    }

    /**
     * 获取wifi'Mac
     *
     * @param context
     * @return wifi'Mac
     */
    public static String getWifiMacAddress(Context context) {
        if (null == context) {
            return null;
        }
        String mac = null;
        try {
            // 6.0系统 由于系统限制，换一种方式读取mac
            if (VersionUtils.hasMarshmallow()) {
                String wifiInterface = getSystemProperty(KEY_WIFI_INTERFACE);
                if (!StringUtils.isEmpty(wifiInterface)) {
                    NetworkInterface nInterface = NetworkInterface.getByName(wifiInterface);
                    if (nInterface != null) {
                        byte[] addr = nInterface.getHardwareAddress();
                        if (addr != null && addr.length > 0) {
                            StringBuilder buf = new StringBuilder();
                            for (byte b : addr) {
                                buf.append(String.format("%02X:", b));
                            }
                            if (buf.length() > 0) {
                                buf.deleteCharAt(buf.length() - 1);
                            }
                            mac = buf.toString();
                        }
                    }
                }
                if (!StringUtils.isEmpty(mac)) {
                    return mac;
                }
            }
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wm.getConnectionInfo();
            mac = info.getMacAddress();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return mac;
    }

    /**
     * 获取以太网卡mac
     *
     * @return eth0'Mac 以太网卡Mac
     */
    public static String getEthMacAddress() {
        String ethMac = null;
        File f = new File("/sys/class/net/eth0/address");
        if (f.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                ethMac = br.readLine();
                ethMac.trim();
                br.close();
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        return ethMac;
    }

    public static String getBluetoothAddr() {
        String addr = "00:00:00:00:00:00";
        try {
            BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
            if (bluetooth != null) {
                addr = bluetooth.getAddress();
                if (StringUtils.isEmpty(addr)) {
                    addr = "00:00:00:00:00:00";
                }
            }
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return addr;
    }

    /*
     * 获取系统中的Library
     */
    public static List<String> getSystemLibs(Context context) {
        if (null == context) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        String[] libNames = pm.getSystemSharedLibraryNames();
        List<String> listLibNames = Arrays.asList(libNames);
        LogUtils.d("SystemLibs: " + listLibNames);
        return listLibNames;
    }

    /**
     * 根据packageName获取packageInfo
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        return getPackageInfo(context, packageName, false);
    }

    /**
     * 根据 包名获取 软件信息
     *
     * @param context
     * @param packageName
     * @param checkVC     是否检测versioncode,尝试从已安装目录下解析apk 信息
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String packageName, boolean checkVC) {
        if (null == context) {
            return null;
        }
        PackageInfo info = null;
        PackageManager manager = context.getPackageManager();
        // 根据packageName获取packageInfo
        try {
            info = manager.getPackageInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            // 如果版本号取值异常，尝试从已安装目录下解析apk 信息
            if (checkVC && info != null && (info.versionCode <= 0 || info.versionCode == Integer.MAX_VALUE)) {
                ApplicationInfo aInfo = info.applicationInfo;
                if (aInfo != null && !StringUtils.isEmpty(aInfo.publicSourceDir)) {
                    PackageInfo apkInfo = manager.getPackageArchiveInfo(aInfo.publicSourceDir, 0);
                    if (apkInfo != null) {
                        info.versionCode = apkInfo.versionCode;
                    }
                }
            }
        } catch (NameNotFoundException e) {
            LogUtils.e(e);
        }
        return info;
    }

    /**
     * 判断是否是第三方软件
     *
     * @param context     Context
     * @param packageName 软件的包名
     * @return 是第三方软件返回true，否则返回false
     */
    public static boolean isThirdPartyApp(Context context, String packageName) {
        if (null == context) {
            return false;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(packageName, 0);
            return isThirdPartyApp(packageInfo);
        } catch (NameNotFoundException e) {
            LogUtils.e(e);
            return false;
        }
    }

    /**
     * 判断是否是第三方软件
     *
     * @param packageInfo 软件的packageInfo
     * @return 是第三方软件返回true，否则返回false
     */
    public static boolean isThirdPartyApp(PackageInfo packageInfo) {
        if (null == packageInfo || null == packageInfo.applicationInfo) {
            return false;
        }

        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
                || ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    /**
     * 读取指定路径下APK文件签名
     */
    @SuppressWarnings("unchecked")
    public static String getJarSignature(String filePath) throws Exception {
        if (null == filePath) {
            return null;
        }
        String resultSign = "";
        String resultKey = "";
        List<ZipEntry> names = new ArrayList<ZipEntry>();
        ZipFile zf = new ZipFile(filePath);
        Enumeration<ZipEntry> zi = (Enumeration<ZipEntry>) zf.entries();
        while (zi.hasMoreElements()) {
            ZipEntry ze = zi.nextElement();
            String name = ze.getName();
            if (name.startsWith("META-INF/") && (name.endsWith(".RSA") || name.endsWith(".DSA"))) {
                names.add(ze);
            }
        }
        Collections.sort(names, new Comparator<ZipEntry>() {
            @Override
            public int compare(ZipEntry obj1, ZipEntry obj2) {
                if (obj1 != null && obj2 != null) {
                    return obj1.getName().toUpperCase().compareTo(obj2.getName().toUpperCase());
                }
                return 0;
            }
        });
        for (ZipEntry ze : names) {
            // System.out.println("RSA file name " + ze.getName());
            InputStream is = zf.getInputStream(ze);
            try {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                CertPath cp = cf.generateCertPath(is, "PKCS7");
                List<?> list = cp.getCertificates();
                for (Object obj : list) {
                    if (!(obj instanceof X509Certificate))
                        continue;
                    X509Certificate cert = (X509Certificate) obj;
                    StringBuffer sb = new StringBuffer();
                    sb.setLength(0);
                    byte[] key = getPKBytes(cert.getPublicKey());
                    for (int i = 0; i < key.length; i++)
                        sb.append(String.format("%02X", key[i]));
                    resultKey += sb.toString();
                    sb.setLength(0);
                    byte[] signature = cert.getSignature();

                    for (int i = 0; i < signature.length; i++)
                        sb.append(String.format("%02X", signature[i]));
                    resultSign += sb.toString();
                }
            } catch (CertificateException e) {
                LogUtils.e(e);
            }
            is.close();
        }
        if (!TextUtils.isEmpty(resultKey) && !TextUtils.isEmpty(resultSign)) {
            return hashCode(resultKey) + "," + hashCode(resultSign);
        }
        return null;
    }

    private static byte[] getPKBytes(PublicKey pk) {
        if (pk instanceof RSAPublicKey) {
            RSAPublicKey k = (RSAPublicKey) pk;
            return k.getModulus().toByteArray();
        } else if (pk instanceof DSAPublicKey) {
            DSAPublicKey k = (DSAPublicKey) pk;
            return k.getY().toByteArray();
        }
        return null;
    }

    /**
     * 字符串对应hash code
     *
     * @param str
     * @return
     */
    public static int hashCode(String str) {
        int hash = 0;
        if (str != null) {
            int multiplier = 1;
            int _offset = 0;
            int _count = str.length();
            char[] _value = new char[_count];
            str.getChars(_offset, _count, _value, 0);
            for (int i = _offset + _count - 1; i >= _offset; i--) {
                hash += _value[i] * multiplier;
                int shifted = multiplier << 5;
                multiplier = shifted - multiplier;
            }
        }
        return hash;
    }

    /**
     * 通过包名读取已安装APP数字签名
     */
    public static String getInstalledPackageSignature(Context context, String packageName) {
        if (null == context) {
            return null;
        }
        String signature = null;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(packageName, PackageManager.GET_SIGNATURES);
            String apkPath = appInfo.sourceDir;
            signature = getJarSignature(apkPath);
        } catch (NameNotFoundException e) {
            LogUtils.e(e);
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return signature;
    }

    public static boolean isSDCardAvailable() {
        /**
         * mk_u950-userdebug 4.4.4 KTU84P 6e8311657c test-keys 机型 java.lang.NullPointerException at
         * android.os.Environment.getStorageState(Environment.java:719) at
         * android.os.Environment.getExternalStorageState(Environment.java:694) at
         * de.robv.android.xposed.XposedBridge.invokeOriginalMethodNative(Native Method) at
         * de.robv.android.xposed.XposedBridge.handleHookedMethod(XposedBridge.java:631) at
         * android.os.Environment.getExternalStorageState(Native Method) at rn.b(SystemUtils.java:483) 在某些机型上出现该空指针异常
         * 此处给进行catch处理 2015-11-03
         */
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getExternalSpace() {
        long availableSpace = -1l;
        try {
            String externalStoragePath = Environment.getExternalStorageDirectory().getPath();
            if (sStatFs == null)
                sStatFs = new StatFs(externalStoragePath);
            else
                sStatFs.restat(externalStoragePath);
            availableSpace = sStatFs.getAvailableBlocks() * (long) sStatFs.getBlockSize();
        } catch (Throwable e) {
            LogUtils.e(e);
        }

        return availableSpace;
    }

    public static long getAvailableSpace(String path) {
        long availableSpace = -1l;
        if (path == null) {
            return availableSpace;
        }
        try {
            if (sStatFs == null)
                sStatFs = new StatFs(path);
            else
                sStatFs.restat(path);
            availableSpace = sStatFs.getAvailableBlocks() * (long) sStatFs.getBlockSize();
        } catch (Exception e) {
            LogUtils.e(e);
        }

        return availableSpace;
    }

    public static long getTotalSpace(String path) {
        long availableSpace = -1l;
        if (path == null) {
            return availableSpace;
        }
        try {
            if (sStatFs == null)
                sStatFs = new StatFs(path);
            else
                sStatFs.restat(path);
            availableSpace = sStatFs.getBlockCount() * (long) sStatFs.getBlockSize();
        } catch (Exception e) {
            LogUtils.e(e);
        }

        return availableSpace;
    }

    public static int[] getResolution(Context context) {
        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int[] res = new int[2];
        try {
            res[0] = windowMgr.getDefaultDisplay().getWidth();
            res[1] = windowMgr.getDefaultDisplay().getHeight();
        } catch (Exception e) {
        }
        return res;
    }

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================


    /**
     * 获取rom版本 xiaoqi 2011-8-14 15:04;
     */
    public static String getRomversion() {
        String rom = "";
        try {
            String modversion = getSystemProperty("ro.modversion");
            String displayId = getSystemProperty("ro.build.display.id");
            if (modversion != null && !modversion.equals("")) {
                rom = modversion;
            }
            if (displayId != null && !displayId.equals("")) {
                rom = displayId;
            }
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return rom;
    }

    /**
     * 获取格式化日期和时间
     *
     * @param formatStr 格式化字符串，例如"yyyy-MM-dd HH:mm:ss"
     * @return 格式化的日期时间
     */
    public static String getFormattedDateTime(String formatStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(formatStr);
            Date date = new Date();
            return format.format(date.getTime());
        } catch (Throwable e) {
            LogUtils.e(e);
        }
        return "";
    }

    public static Resources getAPKResources(Context context, String apkPath) throws Exception {
        String PATH_AssetManager = "android.content.res.AssetManager";
        Class assetMagCls = Class.forName(PATH_AssetManager);
        Constructor assetMagCt = assetMagCls.getConstructor((Class[]) null);
        Object assetMag = assetMagCt.newInstance((Object[]) null);
        Class[] typeArgs = new Class[1];
        typeArgs[0] = String.class;
        Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
        Object[] valueArgs = new Object[1];
        valueArgs[0] = apkPath;
        assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
        Resources res = context.getResources();
        typeArgs = new Class[3];
        typeArgs[0] = assetMag.getClass();
        typeArgs[1] = res.getDisplayMetrics().getClass();
        typeArgs[2] = res.getConfiguration().getClass();
        Constructor resCt = Resources.class.getConstructor(typeArgs);
        valueArgs = new Object[3];
        valueArgs[0] = assetMag;
        valueArgs[1] = res.getDisplayMetrics();
        valueArgs[2] = res.getConfiguration();
        res = (Resources) resCt.newInstance(valueArgs);
        return res;
    }

    /**
     * Gets the number of cores available in this device, across all processors. Requires: Ability to peruse the
     * filesystem at "/sys/devices/system/cpu"
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        // Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                // Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Throwable e) {
            // Default to return 1 core
            return 1;
        }
    }

    /*
     * 获取CPU频率
     */
    public static String getCpuFreq() {
        return readKernelInfo(CPU_MAX_FREQ_PATH);
    }

    /**
     * 获取内核信息
     */
    private static String readKernelInfo(String path) {
        FileReader fr = null;
        BufferedReader br = null;
        String kernelInfo = null;
        try {
            fr = new FileReader(path);
            br = new BufferedReader(fr, 1024);
            kernelInfo = br.readLine();
            if (path.equals(KERNEL_PATH)) {
                String[] temp = kernelInfo.split("\\s+");
                if (temp.length >= 3) {
                    kernelInfo = temp[2];
                }
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (Throwable e) {
            LogUtils.e(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return kernelInfo;
    }

    /**
     * 获取内存信息
     *
     * @return xxxMB
     */
    public static int getRAM() {
        int total_memory = 0;
        String temp = null;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("/proc/meminfo");
            br = new BufferedReader(fr, 1024);
            temp = br.readLine();

            String[] str = temp.split("\\s+");
            total_memory = Math.round(Integer.valueOf(str[1]) / 1024f);
        } catch (Throwable e) {
            LogUtils.e(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }

        return total_memory;
    }

    /**
     * 获取 本机默认 User Agent
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static String getDefaultUA(Context context) {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                return WebSettings.getDefaultUserAgent(context);
            } else {
                return new WebView(context).getSettings().getUserAgentString();
            }
        } catch (Throwable tr) {
            LogUtils.e(tr);
            return null;
        }
    }

    /**
     * 是否为魅族4.4设备
     * 魅族4.4及以上设备都会返回true
     *
     * @return
     */
    public static boolean isMezuKitkat() {
        return VersionUtils.hasKitKat()
                && !StringUtils.isEmpty(SystemUtils.getSystemProperty("ro.flyme.published"));
    }

    /**
     * 是否是小米4.4及以上设备
     *
     * @return
     */
    public static boolean isMiui() {
        return VersionUtils.hasKitKat()
                && !StringUtils.isEmpty(SystemUtils.getSystemProperty("ro.miui.ui.version.code"));
    }

}
