package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.SaveTeachplanDto;
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
     * @param courseId 课程id
     * @return
     * @description 查询课程计划树型结构
     */
    List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * @param teachplanDto
     * @description 保存课程计划
     */
    void saveTeachplan(SaveTeachplanDto teachplanDto);


    /**
     * @description 删除课程计划
     * @param teachplanId
     */
    void deleteTeachplan(Long teachplanId);


    /**
     * @description 课程计划排序
     * @param moveType
     * @param teachplanId
     */
    void orderByTeachplan(String moveType,Long teachplanId);
}
