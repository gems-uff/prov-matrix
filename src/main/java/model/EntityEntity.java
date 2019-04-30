package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasInfluencedBy;

/**
 * @author Victor
 * 
 * Represents Entity -> Entity : wasDerivedFrom || 	wasInfluencedBy || alternateOf || specializationOf || memberOf
 *
 */
public class EntityEntity implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originEntitiesId;
	private List<String> destinationEntitiesId;

	public EntityEntity() {
		super();
		this.originEntitiesId = new ArrayList<>();
		this.destinationEntitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityEntity(Relation relation) {
		this();
		this.relation = relation;
	}

	public EntityEntity(Document d) {
		this();
		this.document = d;
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == Kind.PROV_ENTITY) {
				Entity et = (Entity) sb;
				originEntitiesId.add(et.getId().getLocalPart());
				destinationEntitiesId.add(et.getId().getLocalPart());
			}
		}
		matrix = new CRSMatrix(originEntitiesId.size(), destinationEntitiesId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			Kind k = sb.getKind();
			if (k == this.relation.getKind()) {
				switch (k) {
				case PROV_DERIVATION: {
					WasDerivedFrom wd = (WasDerivedFrom) sb;
					int i = originEntitiesId.indexOf(wd.getGeneratedEntity().getLocalPart());
					int j = destinationEntitiesId.indexOf(wd.getUsedEntity().getLocalPart());
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_INFLUENCE: {
					WasInfluencedBy wi = (WasInfluencedBy) sb;
					int i = originEntitiesId.indexOf(wi.getInfluencee().getLocalPart());
					int j = destinationEntitiesId.indexOf(wi.getInfluencer().getLocalPart());
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_ALTERNATE: {
					AlternateOf ao = (AlternateOf) sb;
					int i = originEntitiesId.indexOf(ao.getAlternate1().getLocalPart());
					int j = destinationEntitiesId.indexOf(ao.getAlternate2().getLocalPart());
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_SPECIALIZATION: {
					SpecializationOf eo = (SpecializationOf) sb;
					int i = originEntitiesId.indexOf(eo.getSpecificEntity().getLocalPart());
					int j = destinationEntitiesId.indexOf(eo.getGeneralEntity().getLocalPart());
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_MENTION: {
					MentionOf mo = (MentionOf) sb;
					int i = originEntitiesId.indexOf(mo.getSpecificEntity().getLocalPart());
					int j = destinationEntitiesId.indexOf(mo.getGeneralEntity().getLocalPart());
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				default:
					break;
				}
			}
		}
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getOriginEntitiesId() {
		return originEntitiesId;
	}

	public void setOriginEntitiesId(List<String> activitiesId) {
		this.originEntitiesId = activitiesId;
	}

	public List<String> getDestinationEntitiesId() {
		return destinationEntitiesId;
	}

	public void setDestinationEntitiesId(List<String> agentsId) {
		this.destinationEntitiesId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationEntitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getOriginEntitiesId();
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

}
