package com.hgd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgd.pojo.Tag;
import com.hgd.service.TagService;
import com.hgd.mapper.TagMapper;
import com.hgd.util.Result;
import com.hgd.vo.ListVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Shinonome
* @description 针对表【tag(标签)】的数据库操作Service实现
* @createDate 2024-08-01 14:16:37
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

    @Override
    public Result list(int pageNum, int pageSize, String name, String remark) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.select(Tag::getId,Tag::getName,Tag::getRemark);
        if(StringUtils.hasText(name)){
            tagLambdaQueryWrapper.like(Tag::getName,name);
        }
        if(StringUtils.hasText(remark)){
            tagLambdaQueryWrapper.like(Tag::getRemark,remark);
        }
        Page<Tag> tagPage = new Page<>(pageNum,pageSize);
        Page<Tag> page = page(tagPage,tagLambdaQueryWrapper);
        List<Tag> tagList = page.getRecords();
        ListVo<Tag> tagListVo = new ListVo<>(page.getTotal(),tagList);
        return Result.ok(tagListVo);
    }

    @Override
    public Result tagById(int id) {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.select(Tag::getId,Tag::getName,Tag::getRemark);
        tagLambdaQueryWrapper.eq(Tag::getId,id);
        Tag tag = getOne(tagLambdaQueryWrapper);
        return Result.ok(tag);
    }

    @Override
    public Result listAllTag() {
        LambdaQueryWrapper<Tag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        tagLambdaQueryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(tagLambdaQueryWrapper);
        return Result.ok(list);
    }
}




