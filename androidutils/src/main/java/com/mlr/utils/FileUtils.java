package com.mlr.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
    // ==========================================================================
    // Constants
    // ==========================================================================

    public static final int TRUNCATE_TYPE_NONE = 0xf1;// 不对写入文件分隔
    public static final int TRUNCATE_TYPE_DAY = 0xf2;// 按天对文件分隔
    public static final int TRUNCATE_TYPE_HOUR = 0xf3;// 按小时对文件分隔

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    // ==========================================================================
    // Fields
    // ==========================================================================

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
     * 判断文件是否存在，有返回TRUE，否则FALSE
     *
     * @return
     */
    public static boolean exists(String fullName) {
        try {
            if (TextUtils.isEmpty(fullName)) {
                return false;
            }
            return new File(fullName).exists();
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }

    }

    public static boolean isReadable(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            File f = new File(path);
            return f.exists() && f.canRead();
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }

    }

    public static boolean isWriteable(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return false;
            }
            File f = new File(path);
            return f.exists() && f.canWrite();
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }
    }

    public static boolean createDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdir();
        }
        return true;
    }

    public static boolean createDirs(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    public static long getSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1l;
        }
        File f = new File(path);
        if (f.isDirectory()) {
            return -1l;
        } else {
            return f.length();
        }
    }

    public static boolean writeFile(InputStream is, String path, boolean recreate) {
        boolean res = false;
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (recreate && f.exists()) {
                f.delete();
            }

            if (!f.exists() && null != is) {
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } catch (Throwable e) {
            LogUtils.e(e);
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                    fos = null;
                }
                if (null != is) {
                    is.close();
                    is = null;
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }

        return res;
    }

    public static boolean writeFile(String content, String path, boolean append) {
        return writeFile(content, path, append, TRUNCATE_TYPE_NONE);
    }

    /**
     * 向指定路径的文件写入内容
     *
     * @param content      要写入的内容
     * @param path         要写入的文件路径
     * @param append       是否要追加
     * @param truncateType 文件截断类型
     * @return 是否写入成功
     */
    public static boolean writeFile(String content, String path, boolean append, int truncateType) {
        boolean res = false;
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                } else {
                    // 目标文件已经存在了，判断是否需要按日期分隔
                    if (truncateType == TRUNCATE_TYPE_DAY) {
                        tryTruncateFile(f, "yyyy-MM-dd");
                    } else if (truncateType == TRUNCATE_TYPE_HOUR) {
                        tryTruncateFile(f, "yyyy-MM-dd HH");
                    }
                }
            } else {
                f.createNewFile();
            }

            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content.getBytes());
                res = true;
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (null != raf) {
                    raf.close();
                    raf = null;
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return res;
    }

    // 根据文件最后修改时间，判断是否需要对文件分隔
    private static String getTruncateName(String fileName, String timestamp) {
        int index = fileName.lastIndexOf(".");
        if (index < 0) {
            index = fileName.length();
        }
        return fileName.substring(0, index) + "_" + timestamp + fileName.substring(index);
    }

    // 检测是否需要截断文件
    private static boolean tryTruncateFile(File f, String pattern) throws IOException {
        String path = f.getAbsolutePath();

        String time1 = StringUtils.getFormattedDateTime(f.lastModified(), pattern);
        String time2 = StringUtils.getFormattedDateTime(System.currentTimeMillis(), pattern);
        if (!time1.equals(time2)) {
            // 时间不一致，需要截断
            File newF = new File(f.getParentFile(), getTruncateName(f.getName(), time1));
            boolean flag = f.renameTo(newF);
            LogUtils.w("renameTo:" + newF.getAbsolutePath() + "  result is " + flag);
            f = new File(path);
            f.createNewFile();
            return true;
        }
        return false;
    }

    public static boolean writeFile(int content, String path, boolean append) {
        boolean res = false;
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }

            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.writeInt(content);
                res = true;
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (null != raf) {
                    raf.close();
                    raf = null;
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }

        return res;
    }

    public static void writeProperties(String filePath, String key, String value, String comment) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        writeProperties(new File(filePath), key, value, comment);
    }

    public static void writeProperties(File f, String key, String value, String comment) {
        if (key == null || value == null || f == null) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            p.setProperty(key, value);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (IllegalArgumentException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } finally {
            try {
                if (null != fis) {
                    fis.close();
                    fis = null;
                }
                if (null != fos) {
                    fos.close();
                    fos = null;
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
    }

    public static Integer readInt(String path) {
        Integer res = null;
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (!f.exists()) {
                return null;
            }

            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "r");
                res = raf.readInt();
            }
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (IOException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (null != raf) {
                    raf.close();
                    raf = null;
                }
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return res;
    }

    public static boolean copyFile(String srcPath, String destPath) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(destPath)) {
            return false;
        }
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile);
    }

    /**
     * 文件拷贝
     */
    public static boolean copyFile(File srcFile, File destFile) {
        return copyFile(srcFile, destFile, true);
    }

    /**
     * 文件拷贝
     */
    public static boolean copyFile(File srcFile, File destFile, boolean isCut) {
        if (null == srcFile || !srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
            }
            out.flush();
            // 是否为剪切操作
            if (isCut) {
                srcFile.delete();
            }
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogUtils.e(e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogUtils.e(e);
                }
            }

        }

        return true;
    }

    public static boolean deleteFile(String path) {
        boolean result = true;
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            try {
                result = !file.exists() || file.delete();
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    public static boolean deleteDirs(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return true;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return true;
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    f.delete();
                }
            }
            return dir.delete();
        }
        return false;
    }

    public static void chmod(String path, String mode) {
        try {
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (Exception e) {
            LogUtils.e(e);
        } catch (Throwable e) {
            LogUtils.e(e);
        }
    }

    public static boolean zip(String srcPath, String destPath, String zipEntry) {
        if (null == srcPath || null == destPath || null == zipEntry) {
            return false;
        }
        return zip(new File(srcPath), new File(destPath), zipEntry);
    }

    public static boolean zip(File srcFile, File destFile, String zipEntry) {
        boolean res = false;
        if (null == srcFile || !srcFile.exists() || !srcFile.canRead() || null == zipEntry) {
            return false;
        }
        if (destFile.exists()) {
            destFile.delete();
        }
        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream(destFile, false);
            zos = new ZipOutputStream(fos);

            ZipEntry entry = new ZipEntry(zipEntry);
            zos.putNextEntry(entry);
            fis = new FileInputStream(srcFile);
            byte[] buffer = new byte[32];
            int cnt = 0;
            while ((cnt = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, cnt);
            }
            zos.flush();
            res = true;
        } catch (FileNotFoundException e) {
            LogUtils.e(e);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (zos != null) {
                    zos.closeEntry();
                    zos.close();
                }
            } catch (IOException e) {
                // ignored
            }

        }
        return res;
    }

    /**
     * 根据文件路径获取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return "";
        }
        int start = filePath.lastIndexOf('/');
        return filePath.substring(start + 1);
    }

    /**
     * 读取文件内容
     */
    public static String readFile(String fileName) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = bytes2HexString(buffer);
            fin.close();
        } catch (Exception e) {
            LogUtils.e(e);
        }
        return res;
    }

    /**
     * byteArr转hexString
     * bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns 00A8
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    // ==========================================================================
    // Inner/Nested Classes
    // ==========================================================================
}
