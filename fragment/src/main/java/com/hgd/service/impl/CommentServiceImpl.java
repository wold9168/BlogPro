package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.hgd.pojo.Comment;
import com.hgd.pojo.SysUser;
import com.hgd.service.CommentService;
import com.hgd.mapper.CommentMapper;
import com.hgd.service.SysUserService;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.Result;
import com.hgd.vo.CommentVo;
import com.hgd.vo.ListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Shinonome
* @description 针对表【comment(评论表)】的数据库操作Service实现
* @createDate 2024-07-31 09:35:07
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result comment(Comment comment) {
        save(comment);
        return Result.ok();
    }

    @Override
    public Result commentList(int articleId, int pageNum, int pageSize) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<Comment>();
        commentLambdaQueryWrapper.eq(Comment::getRootId,-1);
        commentLambdaQueryWrapper.eq(Comment::getArticleId,articleId);
        Page<Comment> commentPage=new Page<>(pageNum,pageSize);
        Page<Comment> page =page(commentPage,commentLambdaQueryWrapper);
        List<Comment> commentList = page.getRecords();
        List<CommentVo> commentVos=MyCopyBeanUtil.copyList(commentList, CommentVo.class);
        //根据所有根评论，查询对应子评论
        for(CommentVo commentVo:commentVos) {
            //封装根评论用户信息
            SysUser createUser =sysUserService.getById(commentVo.getCreateBy());
            commentVo.setAvatar(createUser.getAvatar());
            commentVo.setUsername(createUser.getNickName());
            //查询对应所有子评论
            LambdaQueryWrapper<Comment> childCommentQueryWrapper = new LambdaQueryWrapper<>();
            childCommentQueryWrapper.eq(Comment::getRootId,commentVo.getId());
            List<Comment> childCommentList = list(childCommentQueryWrapper);
            List<CommentVo> childCommentVos=MyCopyBeanUtil.copyList(childCommentList, CommentVo.class);
            //封装子评论的用户信息
            for(CommentVo cv:childCommentVos)
            {
                SysUser cvCreateUser =sysUserService.getById(cv.getCreateBy());
                cv.setAvatar(cvCreateUser.getAvatar());
                cv.setUsername(cvCreateUser.getNickName());
                SysUser toCommentUser = sysUserService.getById(cv.getToCommentUserId());
                cv.setToCommentUserName(toCommentUser.getNickName());
            }
            commentVo.setChildren(childCommentVos);
//            LambdaQueryWrapper<SysUser> queryWrapperCreate = new LambdaQueryWrapper<>();

        }
        ListVo<CommentVo> listVo=new ListVo<>(page.getTotal(),commentVos);
        return Result.ok(listVo);
    }
}




