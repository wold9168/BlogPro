package com.hgd.mapper;

import com.hgd.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hgd.vo.ArticleVo;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
* @author Shinonome
* @description 针对表【article(文章表)】的数据库操作Mapper
* @createDate 2024-07-30 10:29:23
* @Entity com.hgd.pojo.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("select category_id from article")
    public Set<Long> getCategoryIdList();

    public List<ArticleVo> getArticleListByCategoryId(int categoryId, int pageNum, int pageSize);

    public int getArticleListByCategoryIdToCount(int categoryId);
}




