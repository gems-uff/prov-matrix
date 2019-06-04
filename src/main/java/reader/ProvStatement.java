package reader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProvStatement {

	private String id;
	private String name;
	private ProvElement source;
	private ProvElement destination;
	private Map<String, String> attributes;

	public ProvStatement(String name, ProvElement src, ProvElement dst) {
		this();
		this.name = name;
		this.source = src;
		this.destination = dst;
	}

	public ProvStatement() {
		super();
		this.attributes = new HashMap<>();
	}

	public ProvStatement(String name, ProvElement src, ProvElement dst, String[] attributes) {
		this(name, src, dst);
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				String[] keyValue = attributes[i].split("=");
				String key = keyValue[0];
				String value = "";
				if (attributes[i].split("=").length > 1) {
					value = keyValue[1];
				}
				this.attributes.put(key, value);
			}
		}
	}

	public ProvElement getSource() {
		return source;
	}

	public void setSource(ProvElement source) {
		this.source = source;
	}

	public ProvElement getDestination() {
		return destination;
	}

	public void setDestination(ProvElement destination) {
		this.destination = destination;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String toString = "";
		String src = this.getSource() != null ? this.getSource().getName() : "-";
		String dst = this.getDestination() != null ? this.getDestination().getName() : "-";
		if (this.id != null) {
			toString += this.name + "(" + this.id + "; " + src + ", " + dst;
		} else {
			toString += this.name + "(" + src + ", " + dst;
		}
		if (!this.attributes.isEmpty()) {
			toString += ", [";
			Set<String> keys = this.attributes.keySet();
			for (String key : keys) {
				if (key != null) {
					toString += key + "=" + this.attributes.get(key) + ",";
				}
			}
			toString = toString.substring(0, toString.length() - 1);
			toString += "]";
		}
		toString += ")";
		return toString;
	}

}
