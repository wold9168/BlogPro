package com.hgd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.pojo.ArticleTag;
import com.hgd.service.ArticleTagService;
import com.hgd.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
* @author Shinonome
* @description 针对表【article_tag(文章标签关联表)】的数据库操作Service实现
* @createDate 2024-08-01 15:31:32
*/
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
    implements ArticleTagService{

}




