package com.xuecheng.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * <p>
 * 课程计划 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface TeachplanMapper extends BaseMapper<Teachplan> {


    /**
     *
     * @description 查询某课程的教学计划,组成树状结构
     * @param courseId
     * @return
     */
    public List<TeachplanDto> selectTreeNodes(Long courseId);

}
