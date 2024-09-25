package com.hgd.controller;

import com.hgd.pojo.Comment;
import com.hgd.service.CommentService;
import com.hgd.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    @Autowired
    public CommentService commentService;

    @PostMapping("/comment")
    public Result comment(@RequestBody Comment comment){

        return commentService.comment(comment);
    }
    @GetMapping("/comment/commentList")
    public Result commentList(int articleId,int pageNum,int pageSize){
        return commentService.commentList(articleId,pageNum,pageSize);
    }
}
