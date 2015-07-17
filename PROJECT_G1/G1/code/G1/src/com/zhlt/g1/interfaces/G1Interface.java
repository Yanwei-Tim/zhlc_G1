package com.zhlt.g1.interfaces;

import com.zhlt.g1.db.DBOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;


/**
 * 
 * @author kenneth
 * @version V1.0   
 *  Go baby mobile
 *  深圳中和联创智能科技有限公司
 */
public interface G1Interface {
	/**
	 * 初始化
	 * @throws Exception
	 */
	public void init(Context ct,DBOpenHelper  dbh) throws Exception;
    /**
     * 开始运行
     * @throws Exception
     */
	public void start(Handler mhandler) throws Exception;
    /**
     * 关闭，清空资源
     * @throws Exception
     */
	public void close() throws Exception;
	/**
	 * log 模块的状态信息
	 * @throws Exception
	 */
    public void log(boolean debug) throws Exception;
}
