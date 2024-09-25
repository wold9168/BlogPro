package com.hgd;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestMain2 {
    public static void main(String[] args) {
        String fileName = "C:\\javaweb\\新建 Microsoft Excel 工作表.xlsx";
        List<DemoData> demoDataList= new ArrayList<>();
        demoDataList.add(new DemoData("王五","20"));
        EasyExcel.write(fileName,DemoData.class).sheet("模板").doWrite(demoDataList);
    }
}
