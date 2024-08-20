package com.xiehn.excel.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ExcelData {
    @ExcelProperty(value = "id", index = 0)
    private int id;
    @ExcelProperty(value = "platform_order_id", index = 1)
    private String platformOrderId;
    @ExcelProperty(value = "refund_status", index = 2)
    private int refundStatus;
    @ExcelProperty(value = "buyer_rate", index = 3)
    private int buyerRate;
    @ExcelProperty(value = "oid", index = 4)
    private String oid;
    @ExcelProperty(value = "type", index = 5)
    private String type;
    @ExcelProperty(value = "good_id", index = 6)
    private String goodId;
    @ExcelProperty(value = "outer_id", index = 7)
    private String outerId;
    @ExcelProperty(value = "sku_id", index = 8)
    private String skuId;
    @ExcelProperty(value = "outer_sku_id", index = 9)
    private String outerSkuId;
    @ExcelProperty(value = "status", index = 10)
    private int status;
    @ExcelProperty(value = "num", index = 11)
    private int num;
    @ExcelProperty(value = "total_fee", index = 12)
    private double totalFee;
    @ExcelProperty(value = "payment", index = 13)
    private double payment;
    @ExcelProperty(value = "discount_fee", index = 14)
    private double discountFee;
    @ExcelProperty(value = "adjust_fee", index = 15)
    private double adjustFee;
    @ExcelProperty(value = "consign_at", index = 16)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date consignAt;
    @ExcelProperty(value = "divide_order_fee", index = 17)
    private double divideOrderFee;
    @ExcelProperty(value = "end_at", index = 18)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date endAt;
    @ExcelProperty(value = "created_at", index = 19)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    @ExcelProperty(value = "updated_at", index = 20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    @ExcelProperty(value = "refund_id", index = 21)
    private String refundId;
    @ExcelProperty(value = "good_name", index = 22)
    private String goodName;
    @ExcelProperty(value = "part_mjz_discount", index = 23)
    private double partMjzDiscount;
    @ExcelProperty(value = "price", index = 24)
    private double price;
    @ExcelProperty(value = "cid", index = 25)
    private String cid;
}
