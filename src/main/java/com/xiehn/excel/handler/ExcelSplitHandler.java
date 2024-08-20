package com.xiehn.excel.handler;

import com.alibaba.excel.EasyExcel;
import com.xiehn.excel.listener.*;
import com.xiehn.excel.model.ExcelData;
import org.apache.commons.lang3.time.StopWatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 将大Excel文件分片
 */
public class ExcelSplitHandler {
    public static void main(String[] args) {
        // 定义源文件路径
        String filePath = "D:\\子单数据.xlsx";
        // 定义新文件存放的目标目录
        String targetPath = "D:\\test\\";
        StopWatch stopWatch = new StopWatch();

        // 调用方法执行具体逻辑
        try {
            // 使用stopwatch计算程序运行时间
            System.out.println("开始执行");
            stopWatch.start();
            ExcelSplitHandler.splitExcel(filePath, targetPath);
            stopWatch.stop();
        } catch (IOException e) {
            System.out.println("=======出错了==============");
            System.out.println("错误为：" + e);
        }
        // 输出程序运行时间为：分钟
        System.out.println("程序运行时间为：" + stopWatch.getTime(TimeUnit.SECONDS) + "秒");

    }

    public static void splitExcel(String filePath, String targetPath) throws IOException {
        // 判断源文件里面一共有几个sheet页
        int sheetCount = EasyExcel.read(new FileInputStream(filePath)).build().excelExecutor().sheetList().size();
        System.out.println("一共有" + sheetCount + "个sheet页");
        // 根据sheet页创建固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(sheetCount);
        try {
            // 根据sheet页的数量，依次读取这三个sheet页
            for (int i = 0; i < sheetCount; i++) {
                int finalI = i;
                // 提交任务到线程池中
                executorService.submit(() -> {
                    try {
                        // 读取第i个sheet页
                        // 采用easyExcel的流式读取的方式读取文件
                        EasyExcel.read(filePath, ExcelData.class, new ExcelListener(finalI, targetPath)).sheet(finalI).headRowNumber(0).doRead();
                    } catch (Exception e) {
                        System.out.println("处理第 " + finalI + " 个sheet时出错：" + e.getMessage());
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
