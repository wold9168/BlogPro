package com.hgd.util;

import com.alibaba.fastjson.JSON;
import com.hgd.http.HttpCode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WriteUtil {
    public static void write(HttpServletResponse resp, HttpCode httpCode) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        resp.getWriter().write(JSON.toJSONString(Result.fail(httpCode)));

    }
}
