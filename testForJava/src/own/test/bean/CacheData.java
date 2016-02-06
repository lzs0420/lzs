package own.test.bean;

public class CacheData {
	private Object data;
	private long time;
	private int count;
	
	public CacheData(Object data) {
		this.setData(data);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void addCount() {
		this.count++;
		
	}

}
