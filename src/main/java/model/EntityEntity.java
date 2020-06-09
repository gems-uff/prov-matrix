package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasInfluencedBy;

/**
 * @author Victor
 * 
 *         Represents Entity -> Entity : wasDerivedFrom || wasInfluencedBy ||
 *         alternateOf || specializationOf || mentionOf || hadMember
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
			if (sb != null) {
				if (sb instanceof Statement) {
					buildIndex(sb);
				} else {
					Bundle bundle = (Bundle) sb;
					buildBundleIndex(bundle.getStatement());
				}
			}
		}
		Collections.sort(this.originEntitiesId);
		Collections.sort(this.destinationEntitiesId);
		matrix = new CRSMatrix(originEntitiesId.size(), destinationEntitiesId.size());
	}

	public EntityEntity(List<String> entitiesList) {
		this.originEntitiesId = entitiesList;
		this.destinationEntitiesId = entitiesList;
		this.matrix = new CRSMatrix(originEntitiesId.size(), destinationEntitiesId.size());
	}

	public void add(List<String> entitiesList) {
		if (entitiesList != null) {
			for (String e : entitiesList) {
				if (!this.originEntitiesId.contains(e)) {
					this.originEntitiesId.add(e);
				}
				if (!this.destinationEntitiesId.contains(e)) {
					this.destinationEntitiesId.add(e);
				}
			}
			if (matrix.rows() != this.getRowDescriptors().size()
					|| matrix.columns() != this.getColumnDescriptors().size()) {
				matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
			}
		}
	}

	private void buildIndex(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
			Entity et = (Entity) sb;
			originEntitiesId.add(id(et.getId()));
			destinationEntitiesId.add(id(et.getId()));
		}
	}

	private void buildBundleIndex(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			buildIndex(iterator.next());
		}
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (StatementOrBundle sb : sbs) {
			if (sb != null) {
				if (sb instanceof Statement) {
					processStatement(sb);
				} else {
					Bundle bundle = (Bundle) sb;
					processStatements(bundle.getStatement());
				}
			}
		}
	}

	private void processStatement(StatementOrBundle sb) {
		if (sb != null && sb != null && sb.getKind() == this.relation.getKind()) {
			switch (sb.getKind()) {
			case PROV_DERIVATION: {
				WasDerivedFrom wd = (WasDerivedFrom) sb;
				int i = originEntitiesId.indexOf(id(wd.getGeneratedEntity()));
				int j = destinationEntitiesId.indexOf(id(wd.getUsedEntity()));
				matrix.set(i, j, matrix.get(i, j) + 1);
				break;
			}
			case PROV_INFLUENCE: {
				WasInfluencedBy wi = (WasInfluencedBy) sb;
				System.out.println(id(wi.getInfluencee()));
				System.out.println(id(wi.getInfluencer()));
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
			case PROV_MEMBERSHIP: {
				HadMember hm = (HadMember) sb;
				List<QualifiedName> members = hm.getEntity();
				for (QualifiedName member : members) {
					int i = originEntitiesId.indexOf(id(member));
					int j = destinationEntitiesId.indexOf(id(hm.getCollection()));
					matrix.set(i, j, matrix.get(i, j) + 1);
				}
				break;
			}
			default:
				break;
			}
		}
	}

	private void processStatements(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			processStatement(iterator.next());
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
		return ProvType.PROV_ENTITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ENTITY;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvType.PROV_ENTITY;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ENTITY;
	}

	public void add(String src, String dest) {
		int i = this.originEntitiesId.indexOf(src);
		int j = this.destinationEntitiesId.indexOf(dest);
		if (i == -1) {
			this.originEntitiesId.add(src);
			i = this.originEntitiesId.indexOf(src);
		}
		if (j == -1) {
			this.destinationEntitiesId.add(dest);
			j = this.destinationEntitiesId.indexOf(dest);
		}
		if (matrix.rows() != this.getRowDescriptors().size()
				|| matrix.columns() != this.getColumnDescriptors().size()) {
			matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
		}
		this.matrix.set(i, j, this.matrix.get(i, j) + 1);
	}

	@Override
	public boolean isEmpty() {
		return this.matrix.density() == 0.0;
	}

	@Override
	public String getIdentifier() {
		return this.relation != null ? this.relation.getAbbreviate().replace(" ", "") : null;
	}
}