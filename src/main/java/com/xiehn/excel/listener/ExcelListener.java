package com.xiehn.excel.listener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.xiehn.excel.model.ExcelData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelListener extends AnalysisEventListener<ExcelData> {
    // 定义每个小excel的行数大小
    private static final int BATCH_COUNT = 3000;
    // 定义存放缓冲数据的集合
    private final List<ExcelData> excelData = new ArrayList<>(BATCH_COUNT);
    // 生成文件的目标存放路径
    private final String targetPath;
    private final int sheetIndex;
    // 定义计时器，用来指定生成的文件名
    private final AtomicInteger count = new AtomicInteger(1);

    public ExcelListener(int sheetIndex, String targetPath) {
        this.targetPath = targetPath;
        this.sheetIndex = sheetIndex;
    }

    @Override
    public void invoke(ExcelData data, AnalysisContext analysisContext) {
        // 读取的时候先判断每个sheet页的第一行是不是表头，是表头就跳过
        if (analysisContext.readSheetHolder().getSheetNo() == 0 && analysisContext.readRowHolder().getRowIndex() == 0) {
            return;
        }
        // 如果不是第一个sheet，那就打印出来每个sheet第一行数据的id
        if (analysisContext.readSheetHolder().getSheetNo() != 0 && analysisContext.readRowHolder().getRowIndex() == 0) {
            System.out.println("第" + sheetIndex + "个sheet第一行数据的id如下：");
            System.out.println(data.getId());
        }
        // 逐行读取数据
        excelData.add(data);
        if (excelData.size() == BATCH_COUNT) {
            // 批量写入到新的excel文件
            writeExcel(excelData);
            excelData.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 最后写入剩余的数据
        if (!excelData.isEmpty()) {
            System.out.println("第" + sheetIndex + "个sheet剩余最后一行数据的id如下：");
            System.out.println(excelData.get(excelData.size() - 1).getId());
            writeExcel(excelData);
        }
        excelData.clear();
    }

    private void writeExcel(List<ExcelData> excelData) {
        // 指定新文件的名称和路径
        String fileName = targetPath + "子单数据_sheet" + sheetIndex + "_" + count.get() + ".xlsx";
        System.out.println("生成文件：" + fileName + "，属于第" + sheetIndex + "个sheet" + "，第" + count.get() + "个文件");
        // 创建新的excel文件
        EasyExcel.write(fileName, ExcelData.class).sheet().doWrite(excelData);
        // 计时器加1
        count.getAndIncrement();
    }

}
