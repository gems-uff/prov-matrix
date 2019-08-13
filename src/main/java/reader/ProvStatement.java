package reader;

import java.util.Set;

import model.ProvConcept;
import model.ProvType;
import model.ProvRelation.Relation;

public class ProvStatement extends ProvConcept {

	private String id;
	private Relation relation;
	private ProvType source;
	private ProvType destination;

	public ProvStatement(Relation relation, ProvType src, ProvType dst) {
		this();
		this.relation = relation;
		this.source = src;
		this.destination = dst;
	}

	public ProvStatement() {
		super();
	}

	public ProvStatement(Relation relation, ProvType src, ProvType dst, String[] attributes) {
		this(relation, src, dst);
		if (attributes != null) {
			for (int i = 0; i < attributes.length; i++) {
				String[] keyValue = attributes[i].split("=");
				String key = keyValue[0];
				String value = "";
				if (attributes[i].split("=").length > 1) {
					value = keyValue[1];
				}
				super.getAttributes().put(key, value);
			}
		}
	}

	public ProvType getSource() {
		return source;
	}

	public void setSource(ProvType source) {
		this.source = source;
	}

	public ProvType getDestination() {
		return destination;
	}

	public void setDestination(ProvType destination) {
		this.destination = destination;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
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
			toString += this.relation + "(" + this.id + "; " + src + ", " + dst;
		} else {
			toString += this.relation + "(" + src + ", " + dst;
		}
		if (!super.getAttributes().isEmpty()) {
			toString += ", [";
			Set<String> keys = super.getAttributes().keySet();
			for (String key : keys) {
				if (key != null) {
					toString += key + "=" + super.getAttributes().get(key) + ",";
				}
			}
			toString = toString.substring(0, toString.length() - 1);
			toString += "]";
		}
		toString += ")";
		return toString;
	}

}
