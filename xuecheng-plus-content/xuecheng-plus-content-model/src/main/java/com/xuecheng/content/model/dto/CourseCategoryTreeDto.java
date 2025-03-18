package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Elon
 * @title: CourseCategoryTreeDto
 * @projectName: xuecheng-plus-project
 * @description: 课程分类树型节点dto
 * @date: 2025/3/18 16:04
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    List<CourseCategoryTreeDto> childrenTreeNodes;

}
