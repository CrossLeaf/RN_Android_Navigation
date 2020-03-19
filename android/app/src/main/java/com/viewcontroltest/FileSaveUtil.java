package com.viewcontroltest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.ResponseBody;

public class FileSaveUtil {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值

    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值

    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值

    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值


    /**
     * SD卡是否存在
     **/
    private boolean hasSD = false;
    /**
     * 当前程序包的路径
     **/
    private String FILESPATH;

    public static boolean isFileExists(File file) {
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    // 创建目录
    public static void mkDir(String dir) {
        File file = new File(dir);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        boolean f = file.mkdirs();
    }

    /**
     * 获取文件夹下的所有文件名
     */
    public static List<String> getFileName(String fileName) {
        List<String> fileList = new ArrayList<String>();
        String path = fileName; // 路径
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return fileList;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (!fs.isDirectory()) {
                fileList.add(fs.getName());
            }
        }
        return fileList;
    }

    /**
     * 獲取文件夾下的所有文件名
     *
     * @param parentPath 父目錄
     * @return 目錄底下的所有檔案
     */
    public static List<File> getFiles(String parentPath) {
        List<File> fileList = new ArrayList<File>();
        File f = new File(parentPath);
        if (!f.exists()) {
            System.out.println(parentPath + " not exists");
            return fileList;
        }

        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (!fs.isDirectory()) {
                fileList.add(fs);
            }
        }
        return fileList;
    }

    // 获取目录下所有文件(按时间排序)
    public static List<String> getFileSort(final String parentPath, List<String> list) {
        if (list != null && list.size() > 0) {
            Collections.sort(list, new Comparator<String>() {
                @Override
                public int compare(String filePath, String newFilePath) {
                    String path = parentPath + "/" + filePath;
                    File file = new File(parentPath, filePath);
                    File newFile = new File(parentPath, newFilePath);
                    long filetime = file.lastModified();
                    long newFiletime = newFile.lastModified();
                    if (file.lastModified() < newFile.lastModified()) {// 降序<; 升序>
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }
        return list;
    }

    /**
     * 獲取目錄下所有文件(案時間近到遠排序)
     *
     * @param list 檔案列表
     * @return 排序後的檔案列表
     */
    public static List<File> getFileSort(List<File> list) {
        if (list != null && list.size() > 0) {
            Collections.sort(
                    list,
                    (file, newFile) -> Long.compare(newFile.lastModified(), file.lastModified())
            );
        }
        return list;
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File createSDFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!isFileExists(file))
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                file.createNewFile();
            }
        return file;
    }

    /**
     * 在SD卡上创建文件夹
     *
     * @throws IOException
     */
    public static File createSDDirectory(String fileName) throws IOException {
        File file = new File(fileName);
        if (!isFileExists(file))
            file.mkdirs();
        return file;
    }

//    /**
//     * @content 存储内容
//     * @file 文件目录
//     * @isAppend 是否追加
//     */
//    public synchronized static void writeString(String content, String file, boolean isAppend) {
//        try {
//            createSDDirectory(saveFn);
//            createSDDirectory(savelistFn);
//            createSDDirectory(savechannelFn);
//            byte[] data = content.getBytes("utf-8");
//            writeBytes(file, data, isAppend);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//
//    }

    public synchronized static boolean writeBytes(String filePath, byte[] data,
                                                  boolean isAppend) {
        try {
            FileOutputStream fos;
            if (isAppend)
                fos = new FileOutputStream(filePath, true);
            else
                fos = new FileOutputStream(filePath);
            fos.write(data);
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * 读取SD卡中文本文件
     *
     * @param fileName
     * @return
     */
    public synchronized static String readSDFile(String fileName) {
        StringBuffer sb = new StringBuffer();
        File f1 = new File(fileName);
        String str = null;
        try {
            InputStream is = new FileInputStream(f1);
            InputStreamReader input = new InputStreamReader(is, "UTF-8");
            @SuppressWarnings("resource")
            BufferedReader reader = new BufferedReader(input);
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        // 如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    public static boolean saveBitmap(Bitmap bm, String picName) {
        try {
            File f = new File(picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 把文件转换成base64
     *
     * @param path
     * @return
     */
    public static String encodeBase64File(String path) throws Exception {
        byte[] videoBytes;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @SuppressWarnings("resource")
        FileInputStream fis = new FileInputStream(new File(path));
        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);
        videoBytes = baos.toByteArray();
        return Base64.encodeToString(videoBytes, Base64.NO_WRAP);
    }

    /**
     * 根据相册媒体库路径转换成sd卡路径
     *
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isOverKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isOverKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @SuppressLint("NewApi")
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        } else {
            file.createNewFile();
            Log.e("获取文件大小", "文件不存在!");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;

    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;

    }

    public static String writeResponseBodyToDisk(ResponseBody body, String fileName) {
        String filePath = null;
        try {
            File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(file, fileName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                filePath = futureStudioIconFile.getAbsolutePath();
            } catch (IOException e) {
                filePath = null;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            filePath = null;
        }

        return filePath;
    }

    public String getFILESPATH() {
        return FILESPATH;
    }

    public boolean hasSD() {
        return hasSD;
    }
}