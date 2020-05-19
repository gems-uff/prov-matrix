package model;

import org.openprovenance.prov.model.StatementOrBundle.Kind;

public interface ProvRelation {

	public static final String PROV_USAGE = "used";
	public static final String PROV_ABBREVIATE_USAGE = "U S D";
	public static final String PROV_GENERATION = "wasGeneratedBy";
	public static final String PROV_ABBREVIATE_GENERATION = "W G B";
	public static final String PROV_INVALIDATION = "wasInvalidatedBy";
	public static final String PROV_ABBREVIATE_INVALIDATION = "W V B";
	public static final String PROV_START = "wasStartedBy";
	public static final String PROV_ABBREVIATE_START = "S T D";
	public static final String PROV_END = "wasEndedBy";
	public static final String PROV_ABBREVIATE_END = "E N D ";
	public static final String PROV_COMMUNICATION = "wasInformedBy";
	public static final String PROV_ABBREVIATE_COMMUNICATION = "W F B";
	public static final String PROV_DERIVATION = "wasDerivedFrom";
	public static final String PROV_ABBREVIATE_DERIVATION = "W D F";
	public static final String PROV_ASSOCIATION = "wasAssociatedWith";
	public static final String PROV_ABBREVIATE_ASSOCIATION = "W A W";
	public static final String PROV_ATTRIBUTION = "wasAttributedTo";
	public static final String PROV_ABBREVIATE_ATTRIBUTION = "W A T";
	public static final String PROV_DELEGATION = "actedOnBehalfOf";
	public static final String PROV_ABBREVIATE_DELEGATION = "A OB";
	public static final String PROV_INFLUENCE = "wasInfluencedBy";
	public static final String PROV_ABBREVIATE_INFLUENCE = "W I B";
	public static final String PROV_ALTERNATE = "alternateOf";
	public static final String PROV_ABBREVIATE_ALTERNATE = "A OF";
	public static final String PROV_SPECIALIZATION = "specializationOf";
	public static final String PROV_ABBREVIATE_SPECIALIZATION = "S OF";
	public static final String PROV_MENTION = "mentionOf";
	public static final String PROV_ABBREVIATE_MENTION = "M OF";
	public static final String PROV_MEMBERSHIP = "hadMember";
	public static final String PROV_ABBREVIATE_MEMBERSHIP = "H M B";
	public static final String PROV_AGENT = "agent";
	public static final String PROV_ACTIVITY = "activity";
	public static final String PROV_ENTITY = "entity";
	public static final String PROV_ABBREVIATE_ACT_INSTANCE = "Ac T";
	public static final String PROV_ABBREVIATE_AGT_INSTANCE = "Ag T";
	public static final String PROV_ABBREVIATE_ENT_INSTANCE = "En T";
	
	public enum Relation {

		RELATION_USAGE(Kind.PROV_USAGE, PROV_USAGE, PROV_ABBREVIATE_USAGE),
		RELATION_GENERATION(Kind.PROV_GENERATION, PROV_GENERATION, PROV_ABBREVIATE_GENERATION),
		RELATION_INVALIDATION(Kind.PROV_INVALIDATION, PROV_INVALIDATION, PROV_ABBREVIATE_INVALIDATION),
		RELATION_START(Kind.PROV_START, PROV_START, PROV_ABBREVIATE_START),
		RELATION_END(Kind.PROV_END, PROV_END, PROV_ABBREVIATE_END),
		RELATION_COMMUNICATION(Kind.PROV_COMMUNICATION, PROV_COMMUNICATION, PROV_ABBREVIATE_COMMUNICATION),
		RELATION_DERIVATION(Kind.PROV_DERIVATION, PROV_DERIVATION, PROV_ABBREVIATE_DERIVATION),
		RELATION_ASSOCIATION(Kind.PROV_ASSOCIATION, PROV_ASSOCIATION, PROV_ABBREVIATE_ASSOCIATION),
		RELATION_ATTRIBUTION(Kind.PROV_ATTRIBUTION, PROV_ATTRIBUTION, PROV_ABBREVIATE_ATTRIBUTION),
		RELATION_DELEGATION(Kind.PROV_DELEGATION, PROV_DELEGATION, PROV_ABBREVIATE_DELEGATION),
		RELATION_INFLUENCE(Kind.PROV_INFLUENCE, PROV_INFLUENCE, PROV_ABBREVIATE_INFLUENCE),
		RELATION_ALTERNATE(Kind.PROV_ALTERNATE, PROV_ALTERNATE, PROV_ABBREVIATE_ALTERNATE),
		RELATION_SPECIALIZATION(Kind.PROV_SPECIALIZATION, PROV_SPECIALIZATION, PROV_ABBREVIATE_SPECIALIZATION),
		RELATION_MENTION(Kind.PROV_MENTION, PROV_MENTION, PROV_ABBREVIATE_MENTION),
		RELATION_MEMBERSHIP(Kind.PROV_MEMBERSHIP, PROV_MEMBERSHIP, PROV_ABBREVIATE_MEMBERSHIP),
		RELATION_AGENT_INSTANCE(Kind.PROV_AGENT, PROV_AGENT, PROV_ABBREVIATE_AGT_INSTANCE),
		RELATION_ACTIVITY_INSTANCE(Kind.PROV_ACTIVITY, PROV_ACTIVITY, PROV_ABBREVIATE_ACT_INSTANCE),
		RELATION_ENTITY_INSTANCE(Kind.PROV_ENTITY, PROV_ENTITY, PROV_ABBREVIATE_ENT_INSTANCE);

		private final Kind kind;
		private final String description;
		private final String abbreviate;

		Relation(Kind kind, String desc, String abbr) {
			this.kind = kind;
			this.description = desc;
			this.abbreviate = abbr;
		}

		public Kind getKind() {
			return kind;
		}

		public String getDescription() {
			return description;
		}

		public String getAbbreviate() {
			return abbreviate;
		}

	}

}