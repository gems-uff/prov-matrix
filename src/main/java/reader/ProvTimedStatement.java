package reader;

public class ProvTimedStatement extends ProvStatement{
	
	public ProvTimedStatement() {
		super();
	}

	public ProvTimedStatement(String name, ProvElement src, ProvElement dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvTimedStatement(String name, ProvElement src, ProvElement dst) {
		super(name, src, dst);
	}

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
