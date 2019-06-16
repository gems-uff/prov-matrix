package reader;

public class ProvTimedStatement extends ProvStatement{
	
	private String time;
	
	public ProvTimedStatement() {
		super();
	}

	public ProvTimedStatement(String name, ProvElement src, ProvElement dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvTimedStatement(String name, ProvElement src, ProvElement dst) {
		super(name, src, dst);
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
