package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;

/**
 * @author Victor
 * 
 *         Represents Entity -> Activity : WasGeneratedBy || WasInvalidatedBy
 *
 */
public class EntityActivity extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> entitiesId;
	private List<String> activitiesId;

	public EntityActivity() {
		super();
		this.entitiesId = new ArrayList<>();
		this.activitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityActivity(Relation relation) {
		super();
		this.relation = relation;
	}

	public EntityActivity(Document d) {
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
		Collections.sort(this.entitiesId);
		Collections.sort(this.activitiesId);
		this.matrix = new CRSMatrix(entitiesId.size(), activitiesId.size());
	}

	public EntityActivity(List<String> entitiesList, List<String> activitiesList) {
		this.entitiesId = entitiesList;
		this.activitiesId = activitiesList;
		this.matrix = new CRSMatrix(entitiesId.size(), activitiesId.size());
	}

	public void add(List<String> entitiesList, List<String> activitiesList) {
		if (entitiesList != null) {
			for (String e : entitiesList) {
				if (!this.entitiesId.contains(e)) {
					this.entitiesId.add(e);
				}
			}
		}
		if (activitiesList != null) {
			for (String ac : activitiesList) {
				if (!this.activitiesId.contains(ac)) {
					this.activitiesId.add(ac);
				}
			}
		}
		if (matrix.rows() != this.getRowDescriptors().size()
				|| matrix.columns() != this.getColumnDescriptors().size()) {
			matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
		}
	}

	private void buildIndex(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
			Entity et = (Entity) sb;
			entitiesId.add(id(et.getId()));
		} else if (sb != null && sb.getKind() == Kind.PROV_ACTIVITY) {
			Activity ac = (Activity) sb;
			activitiesId.add(id(ac.getId()));
		}
	}

	private void buildBundleIndex(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			buildIndex(iterator.next());
		}
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
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
		if (sb != null && sb.getKind() == this.relation.getKind()) {
			switch (sb.getKind()) {
			case PROV_GENERATION: {
				WasGeneratedBy wg = (WasGeneratedBy) sb;
				int i = entitiesId.indexOf(id(wg.getEntity()));
				int j = activitiesId.indexOf(id(wg.getActivity()));
				matrix.set(i, j, matrix.get(i, j) + 1);
				break;
			}
			case PROV_INVALIDATION: {
				WasInvalidatedBy wi = (WasInvalidatedBy) sb;
				int i = entitiesId.indexOf(id(wi.getEntity()));
				int j = activitiesId.indexOf(id(wi.getActivity()));
				matrix.set(i, j, matrix.get(i, j) + 1);
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

	public List<String> getEntitiesId() {
		return entitiesId;
	}

	public void setEntitiesId(List<String> activitiesId) {
		this.entitiesId = activitiesId;
	}

	public List<String> getActivitiesId() {
		return activitiesId;
	}

	public void setActivitiesId(List<String> agentsId) {
		this.activitiesId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getActivitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getEntitiesId();
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
		return ProvType.PROV_ACTIVITY;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ACTIVITY;
	}

	public void add(String src, String dest) {
		int i = this.entitiesId.indexOf(src);
		int j = this.activitiesId.indexOf(dest);
		if (i != -1 && j != -1) {
			this.matrix.set(i, j, this.matrix.get(i, j) + 1);
		}
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