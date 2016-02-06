package mine.allen.util.lang;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * �ƻ���
 * @author Allen
 * @version 1.0 For All Projects 
 */
public class ScheduleUtils {

	public static void main(String[] args) throws Exception {
		ScheduleUtils t = new ScheduleUtils();
		t.work();
	}
	
	public void work() throws ParseException{    	
		System.out.println("���ڹ���");
		//�������������������ִ��    	
		Timer timer = new Timer();        
		MyTask myTask = new MyTask();        
		//��2012-05-01 01:01:01ִ�д�����,ÿ�μ��2���ٴ�ִ��        
		timer.schedule(myTask, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-01-22 14:57:00"), 2000);    
	}    
	class MyTask extends TimerTask{    	
		int i = 0;        
		@Override        
		public void run(){            
			System.out.println("���ڹ���");
			//���������������       
		}    
	}
}
