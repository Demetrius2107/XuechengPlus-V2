package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

/**
 * @author: Elon
 * @title: TeachplanService
 * @projectName: xuecheng-plus-project
 * @description: 课程基本信息管理业务接口
 * @date: 2025/3/19 10:55
 */
public interface TeachplanService {

    /**
     * @description 查询课程计划树型结构
     * @param courseId 课程id
     * @return
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);
}
