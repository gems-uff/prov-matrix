package reader;

import model.ProvType;
import model.ProvRelation.Relation;

public class ProvTimedStatement extends ProvStatement{
	
	private String time;
	
	public ProvTimedStatement() {
		super();
	}

	public ProvTimedStatement(Relation relation, ProvType src, ProvType dst, String[] attributes) {
		super(relation, src, dst, attributes);
	}

	public ProvTimedStatement(Relation relation, ProvType src, ProvType dst) {
		super(relation, src, dst);
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
