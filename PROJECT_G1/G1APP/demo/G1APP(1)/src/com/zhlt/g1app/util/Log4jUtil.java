/**
 * 
 */
package com.zhlt.g1app.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * @email 313044509@qq.com
 * @author kenneth
 * 
 */
public class Log4jUtil {
	private static Logger gLogger;

	public static void init(String name) {
		final LogConfigurator logConfigurator = new LogConfigurator();

		logConfigurator.setFileName(FileUtil.getlog4jfile());
		// Set the root log level
		logConfigurator.setRootLevel(Level.ALL);
		// Set log level of a specific logger
		logConfigurator.setLevel("org.apache", Level.ALL);
		logConfigurator.configure();
		// gLogger = Logger.getLogger(this.getClass());
		gLogger = Logger.getLogger(name);
	}

	public static Logger getLogger(String name) {
		if (gLogger == null) {
			init(name);
		}
		return gLogger;
	}
}
