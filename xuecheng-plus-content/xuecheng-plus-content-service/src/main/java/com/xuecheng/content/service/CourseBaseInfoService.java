package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author: Elon
 * @title: CourseBaseInfoService
 * @projectName: xuecheng-plus-project
 * @description: 课程基本信息管理接口
 * @date: 2025/3/18 13:57
 */
public interface CourseBaseInfoService {

    /**
     * @description 课程查询接口
     * @param pageParams 分页参数
     * @param queryCourseParamsDto 查询条件
     * @return
     */
    PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto queryCourseParamsDto);


    /**
     * @description 新增课程基本信息
     * @param companyId 教学机构Id
     * @param addCourseDto 课程基本信息
     * @return
     */
    CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);


    /**
     * @description 根据课程id查询课程基本信息
     * @param courseId 课程id
     * @return
     */
    CourseBaseInfoDto getCourseBaseInfo(long courseId);

    /**
     * @description 修改课程信息
     * @param companyId  机构id
     * @param dto  课程信息
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     * @author Mr.M
     * @date 2022/9/8 21:04
     */
    CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto dto);


    /**
     * @description 删除课程信息
     * @param companyId
     * @param courseId
     */
    void deleteCourse(Long companyId, Long courseId);
}
