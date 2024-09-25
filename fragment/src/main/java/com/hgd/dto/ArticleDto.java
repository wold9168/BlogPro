package com.hgd.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ArticleDto {
    private Long categoryId;
    private String content;
    private String categoryName;
    private Date createTime;
    private Integer delFlag;
    private Long id;
    private String isComment;
    private String isTop;
    private String status;
    private String summary;
    private List<Long> tags;
    private String thumbnail;
    private String title;
    private Long updateBy;
    private Date updateTime;
    private Long viewCount;
}
