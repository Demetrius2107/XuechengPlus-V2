package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Elon
 * @title: CourseCategoryService
 * @projectName: xuecheng-plus-project
 * @description: TODO
 * @date: 2025/3/18 16:16
 */
public interface CourseCategoryService {

    /**
     * 课程分类树型结构查询
     *
     * @param id
     * @return
     */
     List<CourseCategoryTreeDto> queryTreeNodes(String id);
}
