package model;

import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

public interface ProvMatrix {

	public static final String PROV_ENTITY = "ENTITY";
	public static final String PROV_ABBREVIATE_ENTITY = "E";
	public static final String PROV_AGENT = "AGENT";
	public static final String PROV_ABBREVIATE_AGENT = "Ag";
	public static final String PROV_ACTIVITY = "ACTIVITY";
	public static final String PROV_ABBREVIATE_ACTIVITY = "Ac";

	public static final String PROV_USAGE = "used";
	public static final String PROV_ABBREVIATE_USAGE = "USD";

	public static final String PROV_GENERATION = "wasGeneratedBy";
	public static final String PROV_ABBREVIATE_GENERATION = "WGB";

	public static final String PROV_INVALIDATION = "wasInvalidatedBy";
	public static final String PROV_ABBREVIATE_INVALIDATION = "WVB";

	public static final String PROV_START = "started";
	public static final String PROV_ABBREVIATE_START = "STD";

	public static final String PROV_END = "ended";
	public static final String PROV_ABBREVIATE_END = "END";

	public static final String PROV_COMMUNICATION = "wasInformedBy";
	public static final String PROV_ABBREVIATE_COMMUNICATION = "WFB";

	public static final String PROV_DERIVATION = "wasDerivedBy";
	public static final String PROV_ABBREVIATE_DERIVATION = "WDB";

	public static final String PROV_ASSOCIATION = "wasAssociatedWith";
	public static final String PROV_ABBREVIATE_ASSOCIATION = "WAW";

	public static final String PROV_ATTRIBUTION = "wasAttributedTo";
	public static final String PROV_ABBREVIATE_ATTRIBUTION = "WAT";

	public static final String PROV_DELEGATION = "actedOnBehalfOf";
	public static final String PROV_ABBREVIATE_DELEGATION = "AOA";

	public static final String PROV_INFLUENCE = "wasInfluencedBy";
	public static final String PROV_ABBREVIATE_INFLUENCE = "WIB";

	public static final String PROV_ALTERNATE = "alternateOf";
	public static final String PROV_ABBREVIATE_ALTERNATE = "AOF";

	public static final String PROV_SPECIALIZATION = "specializationOf";
	public static final String PROV_ABBREVIATE_SPECIALIZATION = "SOF";

	public static final String PROV_MENTION = "mentionOf";
	public static final String PROV_ABBREVIATE_MENTION = "MOF";

	public static final String PROV_MEMBERSHIP = "hadMember";
	public static final String PROV_ABBREVIATE_MEMBERSHIP = "HMB";

	public CRSMatrix getMatrix();

	public Relation getRelation();

	public String getRowDimentionName();

	public String getRowDimentionAbbreviate();

	public String getColumnDimentionName();

	public String getColumnDimentionAbbreviate();

	public List<String> getColumnDescriptors();

	public List<String> getRowDescriptors();

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
		RELATION_MEMBERSHIP(Kind.PROV_MEMBERSHIP, PROV_MEMBERSHIP, PROV_ABBREVIATE_MEMBERSHIP);

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
