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
@WebListener("���м�����")
public class TimerTaskListener implements ServletContextListener {

	private Timer timer = null; // ��ʱ��
	private long delay = 0; // ��ʱʱ��ֵ����ǰϵͳʱ�䵽����ʱ��֮���ʱ����������
	private long between = 0; // ���ʱ��ֵ

	@Override
	public void contextInitialized(ServletContextEvent paramServletContextEvent) {
		init(paramServletContextEvent);// ��ʼ������
		timer = new Timer(true);// ����Timer����
		// ��ʱִ�г���
		// ����Handle()�ࣨ��ÿ��Ҫִ�е����񣩣�delay��ʾ�ӳٺ�������0,24*60*60*1000��ʾÿ��һ��ִ��һ������60*60*1000��ʾһ��Сʱ��
		timer.schedule(new Handle(paramServletContextEvent.getServletContext()),
				delay, between);
	}

	@Override
	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		timer.cancel();
	}

	/**
	 * ��ʼ������
	 * @param paramServletContextEvent 
	 */
	public void init(ServletContextEvent paramServletContextEvent) {
		//�����ò�������TimerTask
		String timerTask = paramServletContextEvent.getServletContext().getInitParameter("TimerTask");
		String[] timerTasks = timerTask.split(",");
		// ����һ��Calendar����
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		// ������ǰʱ��
		Date now = new Date();
		// ����ʱ��
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
			LogUtils.errorPrint("TimerTask���ô���(�����÷�Χ������),Сʱ��"
					+timerTasks[0]+",���ӣ�"+timerTasks[1]+",�룺"+timerTasks[2]+",���ͣ�"+timerTasks[3]+",�����"+timerTasks[4]+"",e);
		}
		// ��ȡ�趨��ʱ��
		Date date = cal.getTime();
		// ��ȡ�趨��ʱ��͵�ǰ��ʱ����������
		long interval = date.getTime() - now.getTime();
		// �����ǰʱ���������ʱ�䣬������ʱ������Ϊ��һ������ʱ��
		if (interval < 0) {
			cal.add(Calendar.DAY_OF_MONTH, 1);// ��������1
			date = cal.getTime();
			// ��ȡ�趨��ʱ��͵�ǰ��ʱ����������
			interval = date.getTime() - now.getTime();
		}
		this.delay = interval;// ��ʼ����ǰʱ�䵽����ʱ��֮���ʱ���
		LogUtils.infoPrint("=====��һ������ʱ�䣺"+date+" =====");
		LogUtils.infoPrint("TimerTask���ã�Сʱ��"
					+timerTasks[0]+",���ӣ�"+timerTasks[1]+",�룺"+timerTasks[2]+",���ͣ�"+timerTasks[3]+",�����"+timerTasks[4]);
	}

	public class Handle extends TimerTask {

		private ServletContext context = null;
		Timer timer = null; // ��ѯ����ʱ��

		public Handle(ServletContext context) {
			this.context = context;
		}

		/**
		 * ��ʼִ��
		 */
		public void run() {
			// ��ʼִ��
			context.log("��ʼִ�У�");
		}
	}
}
