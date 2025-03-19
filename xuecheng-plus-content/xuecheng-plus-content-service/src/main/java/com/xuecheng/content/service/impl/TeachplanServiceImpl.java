package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: Elon
 * @title: TeachplanServiceImpl
 * @projectName: xuecheng-plus-project
 * @description: TODO
 * @date: 2025/3/19 10:57
 */
@Service
public class TeachplanServiceImpl implements TeachplanService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;


    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }

    @Transactional
    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {

        // 课程计划
        Long id = teachplanDto.getId();
        if (id != null) {
            Teachplan teachplan = teachplanMapper.selectById(id);
            BeanUtils.copyProperties(teachplanDto, teachplan);
            teachplanMapper.updateById(teachplan);
        } else {
            int count = getTeachplandCount(teachplanDto.getCourseId(), teachplanDto.getParentid());
            Teachplan teachplanNew = new Teachplan();
            teachplanNew.setOrderby(count + 1);
            BeanUtils.copyProperties(teachplanDto, teachplanNew);
            teachplanMapper.insert(teachplanNew);
        }
    }

    @Override
    public void deleteTeachplan(Long teachplanId) {
        if (teachplanId == null) {
            XueChengPlusException.cast("课程计划为空");
        }
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        // 判断当前课程计划是章还是节
        Integer grade = teachplan.getGrade();
        // 当前课程计划为章
        if (grade == 1) {
            LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Teachplan::getParentid, teachplanId);
            // 获取查询条目数
            Integer count = teachplanMapper.selectCount(queryWrapper);

            if (count > 0) {
                XueChengPlusException.cast("课程计划信息存在子集,无法删除");
            }
            teachplanMapper.deleteById(teachplanId);
        } else {
            // 当前课程计划为节
            teachplanMapper.deleteById(teachplanId);
            LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(TeachplanMedia::getTeachplanId, teachplanId);
            teachplanMediaMapper.delete(queryWrapper);
        }
    }

    @Transactional
    @Override
    public void orderByTeachplan(String moveType, Long teachplanId) {
        Teachplan teachplan = teachplanMapper.selectById(teachplanId);
        // 获取当前层级和当前orderBy
        Integer grade = teachplan.getGrade();
        Integer orderBy = teachplan.getOrderby();
        // 章节移动是比较同一课程id下的orderby
        Long courseId = teachplan.getCourseId();
        // 小节移动是比较同一章节id下的orderby
        Long parentId = teachplan.getParentid();

        if ("moveup".equals(moveType)) {
            // 章节上移
            if (grade == 1) {
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Teachplan::getGrade, 1)
                        .eq(Teachplan::getCourseId, courseId)
                        .lt(Teachplan::getOrderby, orderBy)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderBy(teachplan, tmp);
            }
            // 小节上移
            else if (grade == 2) {
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Teachplan::getParentid, parentId)
                        .lt(Teachplan::getOrderby, orderBy)
                        .orderByDesc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderBy(teachplan, tmp);
            }
        } else if ("movedown".equals(moveType)) {
            if (grade == 1) {
                // 章节下移
                // SELECT * FROM teachplan WHERE courseId = 117 AND grade = 1 AND orderby > 1 ORDER BY orderby ASC LIMIT 1
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Teachplan::getCourseId, courseId)
                        .eq(Teachplan::getGrade, grade)
                        .gt(Teachplan::getOrderby, orderBy)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderBy(teachplan, tmp);
            } else if (grade == 2) {
                // 小节下移
                // SELECT * FROM teachplan WHERE parentId = 268 AND orderby > 1 ORDER BY orderby ASC LIMIT 1
                LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Teachplan::getParentid, parentId)
                        .gt(Teachplan::getOrderby, orderBy)
                        .orderByAsc(Teachplan::getOrderby)
                        .last("LIMIT 1");
                Teachplan tmp = teachplanMapper.selectOne(queryWrapper);
                exchangeOrderBy(teachplan, tmp);
            }
        }

    }


    /**
     * @param courseId
     * @param parentId
     * @return
     * @description 获取最新的排序号
     */
    private int getTeachplandCount(long courseId, long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Teachplan::getCourseId, courseId);
        queryWrapper.eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count;
    }

    /**
     * @param teachplan
     * @param tmp
     * @description 交换两个teachPlan的OrderBy
     */
    private void exchangeOrderBy(Teachplan teachplan, Teachplan tmp) {
        if (tmp == null) {
            XueChengPlusException.cast("已经到头");
        } else {
            // 交换orderBy
            Integer orderBy = teachplan.getOrderby();
            Integer tmpOrderBy = tmp.getOrderby();
            teachplan.setOrderby(tmpOrderBy);
            tmp.setOrderby(orderBy);
            teachplanMapper.updateById(tmp);
            teachplanMapper.updateById(teachplan);
        }
    }


}
