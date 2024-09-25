package com.hgd.ex;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CategoryEX {
    @ExcelProperty("分类名")
    private String name;
    @ExcelProperty("分类描述")
    private String description;
}
