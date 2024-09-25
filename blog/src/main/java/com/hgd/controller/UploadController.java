package com.hgd.controller;

import com.hgd.util.QiNiuUtil;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class UploadController {
    @Autowired
    private QiNiuUtil qiNiuUtil;

    @PostMapping("/upload")
    public Result upload(MultipartFile img) throws IOException {
        InputStream inputStream = img.getInputStream();
        String url = qiNiuUtil.upload(inputStream);
        return Result.ok(url);
    }
}
