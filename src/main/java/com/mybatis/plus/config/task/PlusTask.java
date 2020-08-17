package com.mybatis.plus.config.task;

import com.mybatis.plus.utils.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * 描述: 定时任务
 *
 * @author 官萧何
 * @date 2020/8/17 11:20
 * @version V1.0
 */
@Slf4j
@Component
public class PlusTask {
    Logger logger = LoggerFactory.getLogger(PlusTask.class);


    @Value("${conf.auto-task}")
    Boolean autoTask;

    private final int delay = 60 * 60 * 1000;
 
    // 第一次延迟1秒执行，当执行完后7100秒再执行
    @Scheduled(initialDelay = delay, fixedDelay = 2*60*60*1000 )
    public void hourTask(){
        if (!autoTask) {
            return;
        }
        logger.info("===开始执行定时任务===");
        try {
            //任务执行体
        } catch (Exception e) {
            logger.error("====定时任务异常====");
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 2 * * ?") //每天凌晨两点执行
    public void dayTask(){
        if (!autoTask) {
            return;
        }
        logger.info("===开始执行定时任务===");
        try {
            //任务执行体
        } catch (Exception e) {
            logger.error("====定时任务异常====");
            e.printStackTrace();
        }
    }
}