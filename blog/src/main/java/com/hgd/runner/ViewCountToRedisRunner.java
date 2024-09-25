package com.hgd.runner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hgd.http.AppInfo;
import com.hgd.pojo.Article;
import com.hgd.service.ArticleService;
import com.hgd.util.RedisUtil;
import org.apache.xmlbeans.impl.xb.xsdschema.AppinfoDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountToRedisRunner implements CommandLineRunner {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ArticleService articleService;
    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<Article>();
        articleLambdaQueryWrapper.select(Article::getId,Article::getViewCount);
        List<Article> articleList = articleService.list(articleLambdaQueryWrapper);
        Map<String,Long> map =articleList.stream().collect(Collectors.toMap(
                article -> String.valueOf(article.getId()),
                article -> article.getViewCount()
        ));
        redisUtil.hSet(AppInfo.VIEWCOUNT_REDIS_KEY,map);
    }
}
