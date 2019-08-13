package reader;

import model.ProvType;
import model.ProvRelation.Relation;

public class ProvTriggeredStament extends ProvTimedStatement {
	
	private String trigger;
	private String StarterOrEnder;

	public ProvTriggeredStament() {
		super();
	}

	public ProvTriggeredStament(Relation relation, ProvType src, ProvType dst, String[] attributes) {
		super(relation, src, dst, attributes);
	}

	public ProvTriggeredStament(Relation relation, ProvType src, ProvType dst) {
		super(relation, src, dst);
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
