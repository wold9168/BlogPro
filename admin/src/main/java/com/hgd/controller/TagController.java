package com.hgd.controller;

import com.hgd.pojo.Tag;
import com.hgd.service.TagService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/content/tag/list")
    @PreAuthorize("@ps.prePost('content:tag:index')")
    public Result list(int pageNum, int pageSize, String name, String remark) {
        return tagService.list(pageNum, pageSize, name, remark);
    }

    @PostMapping("/content/tag")
    @PreAuthorize("@ps.prePost('content:tag:index')")
    public Result tag(@RequestBody Tag tag) {
        tagService.save(tag);
        return Result.ok();
    }

    @DeleteMapping("/content/tag/{id}")
    @PreAuthorize("@ps.prePost('content:tag:index')")
    public Result tag(@PathVariable("id") List<Integer> ids) {
        tagService.removeByIds(ids);
        return Result.ok();
    }

    @GetMapping("/content/tag/{id}")
    @PreAuthorize("@ps.prePost('content:tag:index')")
    public Result tagById(@PathVariable("id")int id) {
        return tagService.tagById(id);
    }

    @PutMapping("/content/tag")
    @PreAuthorize("@ps.prePost('content:tag:index')")
    public Result tagUpdate(@RequestBody Tag tag){
        tagService.updateById(tag);
        return Result.ok();
    }

    @GetMapping("/content/tag/listAllTag")
    public Result listAllTag() {
        return tagService.listAllTag();
    }
}
