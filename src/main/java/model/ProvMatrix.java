package model;

import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

public interface ProvMatrix {

	public CRSMatrix getMatrix();

	public Relation getRelation();

	public List<String> getColumnDescriptors();

	public List<String> getRowDescriptors();

	public enum Relation {

		RELATION_USAGE(Kind.PROV_USAGE), 
		RELATION_GENERATION(Kind.PROV_GENERATION),
		RELATION_INVALIDATION(Kind.PROV_INVALIDATION), 
		RELATION_START(Kind.PROV_START), 
		RELATION_END(Kind.PROV_END),
		RELATION_COMMUNICATION(Kind.PROV_COMMUNICATION), 
		RELATION_DERIVATION(Kind.PROV_DERIVATION),
		RELATION_ASSOCIATION(Kind.PROV_ASSOCIATION), 
		RELATION_ATTRIBUTION(Kind.PROV_ATTRIBUTION),
		RELATION_DELEGATION(Kind.PROV_DELEGATION), 
		RELATION_INFLUENCE(Kind.PROV_INFLUENCE),
		RELATION_ALTERNATE(Kind.PROV_ALTERNATE), 
		RELATION_SPECIALIZATION(Kind.PROV_SPECIALIZATION),
		RELATION_MENTION(Kind.PROV_MENTION), 
		RELATION_MEMBERSHIP(Kind.PROV_MEMBERSHIP);

		private final Kind kind;

		Relation(Kind kind) {
			this.kind = kind;
		}

		public Kind getKind() {
			return kind;
		}

	}

}
