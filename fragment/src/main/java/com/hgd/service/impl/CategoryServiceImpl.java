package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.mapper.ArticleMapper;
import com.hgd.pojo.Category;
import com.hgd.service.ArticleService;
import com.hgd.service.CategoryService;
import com.hgd.mapper.CategoryMapper;
import com.hgd.util.MyCopyBeanUtil;
import com.hgd.util.Result;
import com.hgd.vo.CategoryVo;
import com.hgd.vo.ListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
* @author Shinonome
* @description 针对表【category(分类表)】的数据库操作Service实现
* @createDate 2024-07-30 10:48:08
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{


    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getCategoryList() {
//        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<Article>();
//        lambdaQueryWrapper.orderByAsc(Article::getCategoryId);
        Set<Long> categoryIdSet=articleMapper.getCategoryIdList();
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.select(Category::getId,Category::getName);
        categoryLambdaQueryWrapper.in(Category::getId, categoryIdSet);
        List<Category> categoryList=list(categoryLambdaQueryWrapper);

        return Result.ok(categoryList);
    }

    @Override
    public Result listAllCategory() {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.select(Category::getId,Category::getDescription,Category::getName);
        List<Category> categoryList=list(categoryLambdaQueryWrapper);
        return Result.ok(categoryList);
    }

    @Override
    public Result categoryListAdmin(int pageNum,int pageSize,String name,String status) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
        if(StringUtils.hasText(name)){
            categoryLambdaQueryWrapper.like(Category::getName,name);
        }
        if(StringUtils.hasText(status)){
            categoryLambdaQueryWrapper.eq(Category::getStatus,status);
        }
        Page<Category> categoryPage=new Page<>(pageNum,pageSize);
        Page<Category> page = page(categoryPage,categoryLambdaQueryWrapper);
        List<Category> categoryList=page.getRecords();
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for(Category category:categoryList){
            CategoryVo categoryVo= MyCopyBeanUtil.copyBean(category,CategoryVo.class);
            categoryVoList.add(categoryVo);
        }
        ListVo<CategoryVo> categoryVoListVo=new ListVo<>(page.getTotal(),categoryVoList);
        return Result.ok(categoryVoListVo);
    }

    @Override
    public Result categoryAdd(Category category) {
        save(category);
        return Result.ok();
    }

    @Override
    public Result categoryDetail(Integer id) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper=new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getId,id);
        Category category=getOne(categoryLambdaQueryWrapper);
        return Result.ok(category);
    }

    @Override
    public Result categoryUpdate(Category category) {
        updateById(category);
        return Result.ok();
    }

    @Override
    public Result categoryDelete(Integer id) {
        categoryMapper.deleteById(id);
        return Result.ok();
    }
}




