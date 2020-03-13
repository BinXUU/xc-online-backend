package com.xuecheng.manage_media;

import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * Created by BinXU on 2020/02/12.
 */
public class Test01 {

    //测试文件分块方法
    @Test
    public void test1() throws Exception {
        File sourceFile = new File("F:\\Develop\\video\\lucene.mp4");
        String chunkPath = "F:\\Develop\\video\\chunk\\";
        File chunkFolder = new File(chunkPath);
        if (!chunkFolder.exists()) {
            chunkFolder.mkdirs();
        }
        //定义分块大小
        long chunkSize = 1024 * 1024 * 1;
        //分块数量
        long chunkNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkSize);
        if (chunkNum <= 0) {
            chunkNum = 1;
        }
        //定义缓冲区大小
        byte[] b = new byte[1024];
        //读取文件
        FileInputStream inputStream = new FileInputStream(sourceFile);
        //分块
        for (long i = 0; i < chunkNum; i++) {
            //创建分块文件
            File file = new File(chunkPath + i);
            boolean newFile = file.createNewFile();
            if (newFile) {
                //向分块文件写入数据
                FileOutputStream outputStream = new FileOutputStream(file);
                int len = -1;
                while ((len = inputStream.read(b)) != -1) {
                    outputStream.write(b, 0, len);
                    if (file.length() > chunkSize) {
                        break;
                    }
                }
                outputStream.close();
            }
        }
        inputStream.close();
    }

    //测试文件合并的方法
    @Test
    public void test02() throws Exception {
        //定义块文件的目录
        File chunkFoler = new File("F:\\Develop\\video\\chunk");
        //合并文件
        File mergeFile = new File("F:\\Develop\\video\\merge.mp4");
        if (mergeFile.exists()) {
            mergeFile.delete();
        }
        //创建新的合并文件
        mergeFile.createNewFile();
        //创建文件输出流
        FileOutputStream outputStream = new FileOutputStream(mergeFile);
        //定义缓冲区大小
        byte[] b = new byte[1024];
        //分块列表
        File[] files = chunkFoler.listFiles();
        //将数组转换为集合，方便排序
        List<File> fileList = new ArrayList<>(Arrays.asList(files));
        //将文件从小到大排序
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                    return -1;
                }
                return 1;
            }
        });
        //合并文件
        for (File chunkFile : fileList) {
            FileInputStream inputStream = new FileInputStream(chunkFile);
            int len = -1;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
        }
        outputStream.close();
    }
}
