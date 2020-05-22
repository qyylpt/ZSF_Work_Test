package com.zsf.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author: zsf
 * Date: 2020-05-22 11:35
 */
public class FileUtils {
    /**
     * 拷贝目录
     * @param fromFile
     * @param toFile
     * @return
     */
    public static boolean copy(String fromFile, String toFile) {
        File[] currentFiles;
        File fFile = new File(fromFile);
        if (!fFile.exists()) {
            return false;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = fFile.listFiles();
        //目标目录
        File tFile = new File(toFile);
        //创建目录
        if (!tFile.exists()) {
            tFile.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i= 0; i<currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                copy(currentFiles[i].getPath() + "/", toFile + "/" + currentFiles[i].getName());

            } else {
                CopySdcardFile(currentFiles[i].getPath(), toFile + "/" + currentFiles[i].getName());
            }
        }
        return true;
    }


    /**
     * 拷贝文件
     * @param fromFile
     * @param toFile
     * @return
     */
    public static boolean CopySdcardFile(String fromFile, String toFile) {
        try {
            InputStream from = new FileInputStream(fromFile);
            OutputStream to = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = from.read(bt)) > 0) {
                to.write(bt, 0, c);
            }
            from.close();
            to.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 删除目录及所有文件
     * @param file
     */
    private void removeFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                removeFile(f);
            }
            file.delete();
        }
    }

}
