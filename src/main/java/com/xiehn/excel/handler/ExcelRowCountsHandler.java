package com.xiehn.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.xiehn.excel.listener.ExcelRowCountsListener;
import com.xiehn.excel.model.ExcelData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @description: 统计指定目录下所有excel文件的总行数
 * @author: 解昊楠
 * @create: 2024-08-20 08:50
 **/
public class ExcelRowCountsHandler {
    /**
     *在读取目录文件时，需要在ExcelRowCountsListener类的invoke方法中自定义需要过滤哪些表头，见第25行
     * 目前没有做过滤，全是从第一行开始读取
     */
    public static void main(String[] args) {
        // 源文件夹路径
        String sourceFolderPath = "D:\\test\\";
        AtomicInteger totalRowCounts = new AtomicInteger(0);
        // 判断当前目录是文件还是文件夹
        File sourceFolder = new File(sourceFolderPath);
        if(sourceFolder.isDirectory()){
            List<String> excelList = getExcelList(sourceFolderPath);
            if (excelList.isEmpty()){
                System.out.println("文件夹为空");
            }else{
                // 遍历文件夹下的所有文件
                for (String excelPath : excelList) {
                    try {
                        System.out.println("开始处理文件："+excelPath);
                        getExcelRowCounts(excelPath,totalRowCounts);
                    } catch (FileNotFoundException e) {
                        System.out.println("文件不存在");
                    }
                }

                // 打印所有文件的总行数
                System.out.println("===================================所有文件的总行数：" + totalRowCounts.get());
            }
            } else if (sourceFolder.isFile() && (sourceFolder.getName().endsWith(".xlsx") || sourceFolder.getName().endsWith(".xls"))) {
            try {
                getExcelRowCounts(sourceFolderPath,totalRowCounts);

                // 打印所有文件的总行数
                System.out.println("===================================所有文件的总行数：" + totalRowCounts.get());
            } catch (FileNotFoundException e) {
                System.out.println("文件不存在");
            }
        }else{
            System.out.println("请输入正确的文件格式");
        }
    }

    public static List<String> getExcelList(String filePath){
        try (Stream<Path> paths = Files.walk(Paths.get(filePath))) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".xlsx") || path.toString().endsWith(".xls"))
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("获取文件列表失败");
            return new ArrayList<>();
        }
    }

    public static void getExcelRowCounts(String filePath,AtomicInteger totalRowCounts) throws FileNotFoundException {
        // 判断源文件里面一共有几个sheet页
        int sheetCount = EasyExcel.read(new FileInputStream(filePath)).build().excelExecutor().sheetList().size();
        System.out.println("当前文件："+filePath+" 一共有" + sheetCount + "个sheet页");
        // 根据sheet页创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(sheetCount);
        AtomicInteger rowCounts = new AtomicInteger(0);
        try {
            // 根据sheet页的数量，依次读取sheet页
            for (int i = 0; i < sheetCount; i++) {
                int finalI = i;
                // 提交任务到线程池中
                executorService.submit(() -> {
                    try {
                        // 读取第i个sheet页
                        // 采用easyExcel的流式读取的方式读取文件
                        synchronized (ExcelRowCountsHandler.class){
                            EasyExcel.read(filePath, new ExcelRowCountsListener(finalI,filePath,rowCounts,totalRowCounts)).sheet(finalI).headRowNumber(0).doRead();
                            System.out.println("当前文件："+ filePath + "总行数："+ rowCounts.get());
                        }
                    } catch (Exception e) {
                        System.out.println("处理文件："+filePath+" 第 " + finalI + " 个sheet时出错：" + e.getMessage());
                    }

                });
            }
            // 等待所有任务完成
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        } catch (InterruptedException e) {
            System.out.println("============出错了==============");
            System.out.println("错误为：" + e);
        }

    }

}
