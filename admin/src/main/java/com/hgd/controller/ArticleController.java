package com.hgd.controller;

import com.hgd.dto.ArticleDto;
import com.hgd.service.ArticleService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping("/content/article")
    @PreAuthorize("@ps.prePost('content:article:writer')")
    public Result article(@RequestBody ArticleDto articleDto){
        return articleService.article(articleDto);
    }
    @GetMapping("/content/article/list")
    @PreAuthorize("@ps.prePost('content:article:list')")
    public Result articleList(int pageNum, int pageSize, String title, String summary){
        return articleService.articleList(pageNum,pageSize,title,summary);
    }
    @GetMapping("/content/article/{id}")
    @PreAuthorize("@ps.prePost('content:article:writer')")
    public Result articleDetail(@PathVariable("id") int id){
        return articleService.articleDetailAdmin(id);
    }
    @PutMapping("/content/article")
    @PreAuthorize("@ps.prePost('content:article:writer')")
    public Result articleUpdate(@RequestBody ArticleDto articleDto){
        return articleService.articleUpdate(articleDto);
    }
    @DeleteMapping("/content/article/{id}")
    @PreAuthorize("@ps.prePost('content:article:writer')")
    public Result deleteArticleContent(@PathVariable int id) {
        return articleService.deleteArticleContent(id);
    }
}
