package reader;

import model.ProvActivity;
import model.ProvType;
import model.ProvRelation.Relation;

public class ProvDelegationStatement extends ProvStatement {

	private ProvActivity activity;

	public ProvDelegationStatement() {
		super();
	}

	public ProvDelegationStatement(Relation name, ProvType src, ProvType dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvDelegationStatement(Relation name, ProvType src, ProvType dst) {
		super(name, src, dst);
	}

	public ProvActivity getActivity() {
		return activity;
	}

	public void setActivity(ProvActivity activity) {
		this.activity = activity;
	}

}
