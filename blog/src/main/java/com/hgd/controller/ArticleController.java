package com.hgd.controller;

import com.hgd.service.ArticleService;
import com.hgd.util.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * aop：
 * 切面：增强的代码
 * 切入点：切面要放到源代码的什么位置
 * 织入：添加的过程
 * 通知：具体的增强方法
 */

@RestController
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/article/hotArticleList")
    public Result hotArticleList() {
        return articleService.hotArticleList();
    }

    @ApiOperation("文章列表")
    @GetMapping("/article/articleList")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value="分类id"),
            @ApiImplicitParam(name = "pageNum", value="页码"),
            @ApiImplicitParam(name = "pageSize", value="每页数据数量"),
    })
    public Result articleList(int categoryId, int pageNum, int pageSize) {
        return articleService.articleList(categoryId,pageNum,pageSize);
    }

    @GetMapping("/article/{id}")
    public Result articleDetail(@PathVariable int id) {
        return articleService.articleDetail(id);
    }

    @PutMapping("/article/updateViewCount/{id}")
    public Result updateViewCount(@PathVariable int id) {
        return articleService.updateViewCount(id);
    }
}
