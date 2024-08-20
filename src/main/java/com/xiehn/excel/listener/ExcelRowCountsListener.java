package com.xiehn.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: 解昊楠
 * @create: 2024-08-20 09:38
 **/
public class ExcelRowCountsListener extends AnalysisEventListener<Map<String, Object>> {
    private final int sheetIndex;
    private final String filePath;
    private AtomicInteger rowCounts;
    private final AtomicInteger totalRowCounts;
    private int rowIndex;
    private final AtomicInteger count = new AtomicInteger(0);
    public ExcelRowCountsListener(int sheetIndex, String filePath, AtomicInteger rowCounts, AtomicInteger totalRowCounts) {
        this.sheetIndex = sheetIndex;
        this.filePath = filePath;
        this.rowCounts = rowCounts;
        this.totalRowCounts = totalRowCounts;
    }
    @Override
    public void invoke(Map<String, Object> o, AnalysisContext analysisContext) {
//        读取的时候先判断第一行是不是表头，是表头就跳过
//        if (analysisContext.readSheetHolder().getSheetNo() == 0 && analysisContext.readRowHolder().getRowIndex() == 0) {
//            return;
//        }
        // 如果当前行数据为空，则直接跳过
        if (o == null) {
            return;
        }
        // 读取当前文件除过表头外的所有行
        rowIndex++;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("文件："+filePath+" 第"+sheetIndex+"个sheet总行数："+rowIndex);
        // 将当前文件所有行累加到原子整数中
        int tmpCount = count.addAndGet(rowIndex);
        rowCounts.addAndGet(tmpCount);
        totalRowCounts.addAndGet(tmpCount);
        rowIndex=0;
    }
}
