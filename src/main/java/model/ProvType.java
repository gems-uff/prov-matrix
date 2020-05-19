package model;

import java.util.Set;

public class ProvType extends ProvConcept {

	public static final String PROV_TYPE = "TYPE";
	public static final String PROV_ABBREVIATE_TYPE = "T";
	public static final String PROV_ENTITY = "ENTITY";
	public static final String PROV_ABBREVIATE_ENTITY = "E";
	public static final String PROV_AGENT = "AGENT";
	public static final String PROV_ABBREVIATE_AGENT = "Ag";
	public static final String PROV_ACTIVITY = "ACTIVITY";
	public static final String PROV_ABBREVIATE_ACTIVITY = "Ac";

	private String name;
	private String kind;

	public ProvType() {
		super();
		this.kind = PROV_ENTITY;
	}

	public ProvType(String name) {
		this();
		this.name = name;
	}

	public ProvType(String name, String kind) {
		this(name);
		this.kind = kind;
	}

	public ProvType(String name, String kind, String[] attributes) {
		this(name, kind);
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				String key = attributes[i].split("=")[0];
				String value = "";
				if (attributes[i].split("=").length > 1) {
					value = attributes[i].split("=")[1];
				}
				super.getAttributes().put(key, value);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String toString = this.kind.equals(PROV_ENTITY)?"entity(":"agent(";
		toString += this.name;
		if (!super.getAttributes().isEmpty()) {
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

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

}
