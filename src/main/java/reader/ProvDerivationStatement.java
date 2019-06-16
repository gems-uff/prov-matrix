package reader;

public class ProvDerivationStatement extends ProvStatement {

	private ProvActivity activity;
	private ProvStatement usage;
	private ProvTimedStatement generation;

	public ProvDerivationStatement() {
		super();
	}

	public ProvDerivationStatement(String name, ProvElement src, ProvElement dst, String[] attributes) {
		super(name, src, dst, attributes);
	}

	public ProvDerivationStatement(String name, ProvElement src, ProvElement dst) {
		super(name, src, dst);
	}

	public ProvActivity getActivity() {
		return activity;
	}

	public void setActivity(ProvActivity activity) {
		this.activity = activity;
	}

	public ProvStatement getUsage() {
		return usage;
	}

	public void setUsage(ProvStatement usage) {
		this.usage = usage;
	}

	public ProvTimedStatement getGeneration() {
		return generation;
	}

	public void setGeneration(ProvTimedStatement generation) {
		this.generation = generation;
	}

}
