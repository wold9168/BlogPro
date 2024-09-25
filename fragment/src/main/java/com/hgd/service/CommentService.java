package com.hgd.service;

import com.hgd.pojo.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2024-07-31 09:35:07
*/
public interface CommentService extends IService<Comment> {

    Result comment(Comment comment);

    Result commentList(int articleId, int pageNum, int pageSize);
}
