package com.hgd.service;

import com.hgd.dto.ArticleDto;
import com.hgd.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【article(文章表)】的数据库操作Service
* @createDate 2024-07-30 10:29:23
*/

public interface ArticleService extends IService<Article> {

    Result hotArticleList();

    Result articleList(int categoryId, int pageNum, int pageSize);

    Result articleDetail(int id);

    Result updateViewCount(int id);

    Result article(com.hgd.dto.ArticleDto articleDto);

    Result articleList(int pageNum, int pageSize, String title, String summary);

    Result articleDetailAdmin(int id);

    Result articleUpdate(ArticleDto articleDto);

    Result deleteArticleContent(int id);
}
