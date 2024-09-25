package com.hgd.service;

import com.hgd.pojo.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hgd.util.Result;

/**
* @author Shinonome
* @description 针对表【tag(标签)】的数据库操作Service
* @createDate 2024-08-01 14:16:37
*/
public interface TagService extends IService<Tag> {

    Result list(int pageNum, int pageSize, String name, String remark);

    Result tagById(int id);

    Result listAllTag();
}
