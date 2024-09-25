package com.hgd.controller;

import com.hgd.service.CategoryService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/getCategoryList")
    public Result getCategoryList() {
        return categoryService.getCategoryList();
    }
}
