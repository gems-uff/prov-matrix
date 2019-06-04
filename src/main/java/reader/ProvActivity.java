package reader;

import java.util.Set;

public class ProvActivity extends ProvElement {

	private String startTime;
	private String endTime;

	public ProvActivity(String id, String[] optionalAttributes) {
		super(id, optionalAttributes);
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		String toString = "";
		toString += super.getName();
		if (!super.getAttributes().isEmpty()) {
			if (this.startTime != null) {
				toString += ", " + startTime;
			} else {
				toString += ", -";
			}
			if (this.endTime != null) {
				toString += ", " + endTime;
			} else {
				toString += ", -";
			}
			toString += ", [";
			Set<String> keys = super.getAttributes().keySet();
			for (String key : keys) {
				if (key != null) {
					toString += key + "=" + super.getAttributes().get(key) + ", ";
				}
			}
			toString = toString.substring(0, toString.length() - 2);
			toString += "]";
		}
		toString += ")";
		return toString;
	}

}
