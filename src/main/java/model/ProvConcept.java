package model;

import java.util.HashMap;
import java.util.Map;

public class ProvConcept {
	
	private Map<String, String> attributes;

	public ProvConcept() {
		this.attributes = new HashMap<>();
	}

	public ProvConcept(Map<String, String> attributes) {
		this();
		this.attributes = attributes;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
