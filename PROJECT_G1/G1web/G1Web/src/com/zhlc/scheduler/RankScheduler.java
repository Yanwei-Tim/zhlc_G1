package com.zhlc.scheduler;

import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * 创建人：anquan <br>
 * 创建时间：2015-5-4 下午05:13:15 <br>
 * 功能描述：定时任务执行 <br>
 * 版本： <br>
 * ====================================== <br>
 *               修改记录 <br>
 * ====================================== <br>
 *  序号    姓名      日期      版本           简单描述 <br>
 *
 */
@Component
public class RankScheduler {
	//每隔一份执行一次
	//@Scheduled(cron = "0 0/1 0-23 * * ?")
	public void recordUserChip(){
		System.out.println("当前时间："+new Date());
	}
}

