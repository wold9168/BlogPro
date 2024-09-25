package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.dto.ArticleDto;
import com.hgd.http.AppInfo;
import com.hgd.mapper.ArticleTagMapper;
import com.hgd.pojo.Article;
import com.hgd.pojo.ArticleTag;
import com.hgd.pojo.Category;
import com.hgd.pojo.Tag;
import com.hgd.service.ArticleService;
import com.hgd.mapper.ArticleMapper;
import com.hgd.service.ArticleTagService;
import com.hgd.service.CategoryService;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.RedisUtil;
import com.hgd.util.Result;
import com.hgd.vo.ArticleVo;
import com.hgd.vo.ListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Shinonome
* @description 针对表【article(文章表)】的数据库操作Service实现
* @createDate 2024-07-30 10:29:23
*/
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public Result hotArticleList() {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getId,Article::getTitle,Article::getViewCount);
        articleLambdaQueryWrapper.orderByDesc(Article::getViewCount);
        articleLambdaQueryWrapper.last("limit 10");
        List<Article> articleList = list(articleLambdaQueryWrapper);
        return Result.ok(articleList);
    }

    @Override
    public Result articleList(int categoryId, int pageNum, int pageSize) {
        log.info("访问了文件列表   categoryId:"+categoryId);
        if(categoryId==0){
            LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            articleLambdaQueryWrapper.select(Article::getCategoryId,Article::getCreateTime,Article::getId,Article::getSummary,
                    Article::getThumbnail,Article::getTitle,Article::getViewCount);
            articleLambdaQueryWrapper.eq(Article::getStatus,0);
            articleLambdaQueryWrapper.orderByDesc(Article::getIsTop);
//            articleLambdaQueryWrapper.last("limit 10");
            //设置分页信息
            Page<Article> articlePage = new Page<>(pageNum,pageSize);
//            List<Article> articleList = list(articleLambdaQueryWrapper);
            Page<Article> page =page(articlePage,articleLambdaQueryWrapper);
            //获取结果
            List<Article> articleList = page.getRecords();
//            articleList.stream().map(new Function<Article, ArticleVo>(){
//                @Override
//                public ArticleVo apply(Article article){
//                    ArticleVo articleVo=new ArticleVo();
//                    BeanUtils.copyProperties(article,articleVo);
//                    return articleVo;
//                }
//            })
            List<ArticleVo> list = MyCopyBeanUtil.copyList(articleList, ArticleVo.class);
            for(ArticleVo articleVo:list){
                LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
                categoryLambdaQueryWrapper.select(Category::getName);
                categoryLambdaQueryWrapper.eq(Category::getId,articleVo.getCategoryId());
                Category one = categoryService.getOne(categoryLambdaQueryWrapper);
                articleVo.setCategoryName(one.getName());
            }
            Map<String, Object> map=new HashMap<>();
            map.put("total",page.getTotal());
            map.put("rows",list);
            return Result.ok(map);
        }
        List<ArticleVo> articleListByCategoryId=
                getBaseMapper().getArticleListByCategoryId(categoryId, pageNum-1, pageSize);
        int total = getBaseMapper().getArticleListByCategoryIdToCount(categoryId);
        Map<String, Object> map=new HashMap<>();
        map.put("total",total);
        map.put("rows",articleListByCategoryId);
        return Result.ok(map);
    }

    @Override
    public Result articleDetail(int id) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getCategoryId,
                Article::getContent,Article::getCreateTime,Article::getId,
                Article::getIsComment,Article::getIsTop,Article::getTitle);
        articleLambdaQueryWrapper.eq(Article::getId,id);
        Article article = getOne(articleLambdaQueryWrapper);
        Category category = categoryService.getById(article.getCategoryId());
        ArticleVo articleVo = MyCopyBeanUtil.copyBean(article, ArticleVo.class);
        articleVo.setViewCount(Long.parseLong(redisUtil.hGet(AppInfo.VIEWCOUNT_REDIS_KEY,String.valueOf(articleVo.getId())).toString()));
        articleVo.setCategoryName(category.getName());
        return Result.ok(articleVo);
    }

    @Override
    public Result updateViewCount(int id) {
        redisUtil.hIncr(AppInfo.VIEWCOUNT_REDIS_KEY,String.valueOf(id),1);
        return Result.ok();
    }

    @Transactional
    @Override
    public Result article(ArticleDto articleDto) {
        Article article = MyCopyBeanUtil.copyBean(articleDto,Article.class);
        save(article);
        List<Long> tags = articleDto.getTags();
        List<ArticleTag> articleTagList = tags.stream().map(aLong -> new ArticleTag(article.getId(),aLong)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);
        return Result.ok();
    }

    @Override
    public Result articleList(int pageNum, int pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getCategoryId,
                Article::getCreateTime,Article::getId,
                Article::getIsComment,Article::getIsTop,Article::getStatus,Article::getSummary,
                Article::getThumbnail,Article::getTitle,Article::getViewCount);
        articleLambdaQueryWrapper.eq(Article::getDelFlag,0);
        if(StringUtils.hasText(title)){
            articleLambdaQueryWrapper.like(Article::getTitle,title);
        }
        if(StringUtils.hasText(summary)){
            articleLambdaQueryWrapper.like(Article::getSummary,summary);
        }
        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        Page<Article> page = page(articlePage,articleLambdaQueryWrapper);
        List<Article> articleList = page.getRecords();
//        ArticleListVo<Tag> tagListVo = new ListVo<>(,articleList);
        List<ArticleVo> list = MyCopyBeanUtil.copyList(articleList, ArticleVo.class);
        for(ArticleVo articleVo :list){
            LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<Category>();
            categoryLambdaQueryWrapper.select(Category::getName);
            categoryLambdaQueryWrapper.eq(Category::getId, articleVo.getCategoryId());
            Category one = categoryService.getOne(categoryLambdaQueryWrapper);
            articleVo.setCategoryName(one.getName());

            articleVo.setViewCount(Long.parseLong(redisUtil.hGet(AppInfo.VIEWCOUNT_REDIS_KEY,String.valueOf(articleVo.getId())).toString()));
        }
        ListVo<ArticleVo> resultList= new ListVo<>(page.getTotal(),list);
        return Result.ok(resultList);
    }

    @Override
    public Result articleDetailAdmin(int id) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.select(Article::getCategoryId,Article::getContent,Article::getCreateBy,
                Article::getCreateTime,Article::getDelFlag,Article::getId,
                Article::getIsComment,Article::getIsTop,Article::getStatus,Article::getSummary,
                Article::getThumbnail,Article::getTitle,Article::getUpdateBy,Article::getUpdateTime);
        articleLambdaQueryWrapper.eq(Article::getId,id);
        Article article = getOne(articleLambdaQueryWrapper);
        Category category = categoryService.getById(article.getCategoryId());
        ArticleVo articleVo = MyCopyBeanUtil.copyBean(article, ArticleVo.class);
        articleVo.setViewCount(Long.parseLong(redisUtil.hGet(AppInfo.VIEWCOUNT_REDIS_KEY,String.valueOf(articleVo.getId())).toString()));

        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.select(ArticleTag::getArticleId,ArticleTag::getTagId);
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,id);

        List<ArticleTag> list = articleTagService.list(articleTagLambdaQueryWrapper);
        List<Long> tagsList = list.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        articleVo.setTags(tagsList);

        return Result.ok(articleVo);
    }

    @Override
    @Transactional
    public Result articleUpdate(ArticleDto articleDto) {
        Article article = MyCopyBeanUtil.copyBean(articleDto,Article.class);
        updateById(article);

        Long articleId = article.getId();
        List<Long> tagList = articleDto.getTags();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("article_id",articleId);
        articleTagMapper.deleteByMap(columnMap);
        List<ArticleTag> articleTagList = tagList.stream().map(aLong -> new ArticleTag(articleId,aLong)).collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);
        return Result.ok();
    }

    @Override
    public Result deleteArticleContent(int id) {
        articleMapper.deleteById(id);
        return Result.ok();
    }
}




