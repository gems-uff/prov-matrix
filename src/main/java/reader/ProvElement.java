package reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvElement {

	private String name;
	private Map<String, String> attributes;

	public ProvElement(String name) {
		this();
		this.name = name;
	}

	public ProvElement() {
		super();
		this.attributes = new HashMap<>();
	}

	public ProvElement(String name, String[] attributes) {
		this(name);
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				String key = attributes[i].split("=")[0];
				String value = "";
				if (attributes[i].split("=").length > 1) {
					value = attributes[i].split("=")[1];
				}
				this.attributes.put(key, value);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		String toString = "";
		toString += this.name;
		if (!this.attributes.isEmpty()) {
			toString += ", [";
			Set<String> keys = this.attributes.keySet();
			for (String key : keys) {
				if (key != null) {
					toString += key + "=" + this.attributes.get(key) + ", ";
				}
			}
			toString = toString.substring(0, toString.length() - 2);
			toString += "]";
		}
		toString += ")";
		return toString;
	}

}
