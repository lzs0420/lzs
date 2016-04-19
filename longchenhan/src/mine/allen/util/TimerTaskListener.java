package mine.allen.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import mine.allen.util.common.LogUtils;
@WebListener("日切监听器")
public class TimerTaskListener implements ServletContextListener {

	private Timer timer = null; // 定时器
	private long delay = 0; // 延时时间值（当前系统时间到下载时间之间的时间差毫秒数）
	private long between = 0; // 间隔时间值

	@Override
	public void contextInitialized(ServletContextEvent paramServletContextEvent) {
		init(paramServletContextEvent);// 初始化参数
		timer = new Timer(true);// 创建Timer对象
		// 定时执行程序
		// 调用Handle()类（即每天要执行的任务），delay表示延迟毫秒数，0,24*60*60*1000表示每隔一天执行一次任务，60*60*1000表示一个小时；
		timer.schedule(new Handle(paramServletContextEvent.getServletContext()),
				delay, between);
	}

	@Override
	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		timer.cancel();
	}

	/**
	 * 初始化参数
	 * @param paramServletContextEvent 
	 */
	public void init(ServletContextEvent paramServletContextEvent) {
		//从配置参数配置TimerTask
		String timerTask = paramServletContextEvent.getServletContext().getInitParameter("TimerTask");
		String[] timerTasks = timerTask.split(",");
		// 声明一个Calendar对象
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		// 声明当前时间
		Date now = new Date();
		// 设置时间
		try{
			cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timerTasks[0]));
			cal.set(Calendar.MINUTE, Integer.parseInt(timerTasks[1]));
			cal.set(Calendar.SECOND, Integer.parseInt(timerTasks[2]));
			switch (Integer.parseInt(timerTasks[3])){
			case 0: cal2.add(Calendar.YEAR, Integer.parseInt(timerTasks[4]));
					between = cal2.getTimeInMillis() - now.getTime();
					break;
			case 1: cal2.add(Calendar.MONTH, Integer.parseInt(timerTasks[4]));
					between = cal2.getTimeInMillis() - now.getTime();
					break;
			case 2: between = 24 * 60 * 60 * 1000 * Integer.parseInt(timerTasks[4]);break;
			case 3: between = 60 * 60 * 1000 * Integer.parseInt(timerTasks[4]);break;
			case 4: between = 60 * 1000 * Integer.parseInt(timerTasks[4]);break;
			case 5: between = 1000 * Integer.parseInt(timerTasks[4]);break;
			default:break;
			}
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			LogUtils.errorPrint("TimerTask配置错误(请配置范围内整数),小时："
					+timerTasks[0]+",分钟："+timerTasks[1]+",秒："+timerTasks[2]+",类型："+timerTasks[3]+",间隔："+timerTasks[4]+"",e);
		}
		// 获取设定的时间
		Date date = cal.getTime();
		// 获取设定的时间和当前的时间差（毫秒数）
		long interval = date.getTime() - now.getTime();
		// 如果当前时间大于下载时间，则将下载时间设置为下一个下载时间
		if (interval < 0) {
			cal.add(Calendar.DAY_OF_MONTH, 1);// 将天数加1
			date = cal.getTime();
			// 获取设定的时间和当前的时间差（毫秒数）
			interval = date.getTime() - now.getTime();
		}
		this.delay = interval;// 初始化当前时间到下载时间之间的时间差
		LogUtils.infoPrint("=====第一次日切时间："+date+" =====");
		LogUtils.infoPrint("TimerTask配置：小时："
					+timerTasks[0]+",分钟："+timerTasks[1]+",秒："+timerTasks[2]+",类型："+timerTasks[3]+",间隔："+timerTasks[4]);
	}

	public class Handle extends TimerTask {

		private ServletContext context = null;
		Timer timer = null; // 查询任务定时器

		public Handle(ServletContext context) {
			this.context = context;
		}

		/**
		 * 开始执行
		 */
		public void run() {
			// 开始执行
			context.log("开始执行！");
		}
	}
}
