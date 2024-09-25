package com.hgd.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data

public class CommentVo {
    private Long articleId;
    private String avatar;

    private List<CommentVo> children;

    private String content;
    private Long createBy;
    private Date createTime;
    private Long id;
    private Long rootId;
    private Long toCommentId;
    private Long toCommentUserId;
    private String toCommentUserName;
    private String username;
}
