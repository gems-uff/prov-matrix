package reader;

public class ProvDelegationStatement extends ProvStatement {

	private ProvActivity activity;

	public ProvDelegationStatement() {
		super();
	}

	public ProvDelegationStatement(String name, ProvElement src, ProvElement dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvDelegationStatement(String name, ProvElement src, ProvElement dst) {
		super(name, src, dst);
	}

	public ProvActivity getActivity() {
		return activity;
	}

	public void setActivity(ProvActivity activity) {
		this.activity = activity;
	}

}
