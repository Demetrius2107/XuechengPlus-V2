package com.xuecheng.media.jobhandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author: Elon
 * @title: SampleJob
 * @projectName: xuecheng-plus-project
 * @description: 测试执行器
 * @date: 2025/3/20 15:50
 */
@Component
@Slf4j
public class SampleJob {

    @XxlJob("testJob")
    public void testJob() throws Exception {
        log.info("开始执行.....");
    }

}
