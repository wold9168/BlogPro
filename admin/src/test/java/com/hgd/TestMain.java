package com.hgd;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;

import java.util.ArrayList;
import java.util.List;

public class TestMain {
    public static void main(String[] args){
        String fileName = "C:\\javaweb\\新建 Microsoft Excel 工作表.xlsx";
        List<DemoData> demoDataList=new ArrayList<>();
        EasyExcel.read(fileName,DemoData.class, new ReadListener<DemoData>(){

            @Override
            public void invoke(DemoData o, AnalysisContext analysisContext) {
                demoDataList.add(o);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {

            }
        }).sheet().doRead();
    }
}
