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
 * Represents Entity -> Entity : wasDerivedFrom || 	wasInfluencedBy || alternateOf || specializationOf || mentionOf
 *
 */
public class EntityEntity extends BasicProv implements ProvMatrix {

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
				originEntitiesId.add(id(et.getId()));
				destinationEntitiesId.add(id(et.getId()));
			}
		}
		matrix = new CRSMatrix(originEntitiesId.size(), destinationEntitiesId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (StatementOrBundle sb : sbs) {
			Kind k = sb.getKind();
			if (k == this.relation.getKind()) {
				switch (k) {
				case PROV_DERIVATION: {
					WasDerivedFrom wd = (WasDerivedFrom) sb;
					int i = originEntitiesId.indexOf(id(wd.getGeneratedEntity()));
					int j = destinationEntitiesId.indexOf(id(wd.getUsedEntity()));
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_INFLUENCE: {
					WasInfluencedBy wi = (WasInfluencedBy) sb;
					int i = originEntitiesId.indexOf(id(wi.getInfluencee()));
					int j = destinationEntitiesId.indexOf(id(wi.getInfluencer()));
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_ALTERNATE: {
					AlternateOf ao = (AlternateOf) sb;
					int i = originEntitiesId.indexOf(id(ao.getAlternate1()));
					int j = destinationEntitiesId.indexOf(id(ao.getAlternate2()));
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_SPECIALIZATION: {
					SpecializationOf eo = (SpecializationOf) sb;
					int i = originEntitiesId.indexOf(id(eo.getSpecificEntity()));
					int j = destinationEntitiesId.indexOf(id(eo.getGeneralEntity()));
					matrix.set(i, j, matrix.get(i, j) + 1);
					break;
				}
				case PROV_MENTION: {
					MentionOf mo = (MentionOf) sb;
					int i = originEntitiesId.indexOf(id(mo.getSpecificEntity()));
					int j = destinationEntitiesId.indexOf(id(mo.getGeneralEntity()));
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

	@Override
	public String getRowDimentionName() {
		return ProvMatrix.PROV_ENTITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ENTITY;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvMatrix.PROV_ENTITY;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ENTITY;
	}

}