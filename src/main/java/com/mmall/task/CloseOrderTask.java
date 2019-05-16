package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.common.RedisShardedPool;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author: whua
 * @create: 2019/05/15 13:30
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @PreDestroy
    public void delLock() {
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
    }

    //    @Scheduled(cron = "0 0/1 * * * ?") //每1分钟执行一次
    public void closeOrderTaskV1() {
        log.info("close order，定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hour);
        log.info("close order，定时任务结束");
    }

    //    @Scheduled(cron = "0 0/1 * * * ?") //每1分钟执行一次
    public void closeOrderTaskV2() {
        log.info("close order，定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("close order，定时任务结束");
    }

    @Scheduled(cron = "0 0/1 * * * ?") //每1分钟执行一次
    public void closeOrderTaskV3() {
        log.info("close order，定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if (setnxResult != null && setnxResult.intValue() == 1) {
            //如果返回值是1，代表设置成功，获取锁
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            //未获取到锁，继续判断时间戳，看是否可以重置并获取到锁
            String locValueStr = RedisShardedPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            if (locValueStr != null && System.currentTimeMillis() > Long.parseLong(locValueStr)) {
                // getSet设置新值，返回旧值
                String getSetResult = RedisShardedPoolUtil.getset(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
                //再次用当前时间戳getset
                //返回给定key的旧值，并判断旧值是否被修改，若发生了改变说明已经有进程获取了锁，则本进程获取锁失败
                //若key不存在，返回nil，则可以获取锁
                if (getSetResult == null || (getSetResult != null && StringUtils.equals(getSetResult, locValueStr))) {
                    // 成功获取到锁
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                } else {
                    log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
                }
            } else {
                log.info("没有获得分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
            }
        }
        log.info("close order，定时任务结束");
    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(lockName, Integer.parseInt(PropertiesUtil.getProperty("lock.ttl", "5")));//有效期5秒，防止死锁
        log.info("获取{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{},ThreadName:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());
        log.info("======================================");
    }


}
