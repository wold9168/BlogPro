package com.hgd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    private String status;

    private Long categoryId;
    private String categoryName;
    private Date createTime;
    private Long id;
    private String summary;
    private String thumbnail;
    private String title;
    private Long viewCount;

    private String content;
    private String isComment;
    private String isTop;

    private List<Long> tags;
}
