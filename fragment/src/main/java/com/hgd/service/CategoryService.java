package com.hgd.service;

import com.hgd.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【category(分类表)】的数据库操作Service
* @createDate 2024-07-30 10:48:08
*/
public interface CategoryService extends IService<Category> {


    Result getCategoryList();

    Result listAllCategory();

    Result categoryListAdmin(int pageNum,int pageSize,String name,String status);

    Result categoryAdd(Category category);

    Result categoryDetail(Integer id);

    Result categoryUpdate(Category category);

    Result categoryDelete(Integer id);
}
