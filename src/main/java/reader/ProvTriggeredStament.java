package reader;

public class ProvTriggeredStament extends ProvTimedStatement {
	
	private String trigger;
	private String StarterOrEnder;

	public ProvTriggeredStament() {
		super();
	}

	public ProvTriggeredStament(String name, ProvElement src, ProvElement dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvTriggeredStament(String name, ProvElement src, ProvElement dst) {
		super(name, src, dst);
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public String getStarterOrEnder() {
		return StarterOrEnder;
	}

	public void setStarterOrEnder(String starterOrEnder) {
		StarterOrEnder = starterOrEnder;
	}

}
