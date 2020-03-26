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
import org.openprovenance.prov.model.Used;

/**
 * @author Victor
 * 
 *         Represents Activity -> Entity : Used
 *
 */
public class ActivityEntity extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> activitiesId;
	private List<String> entitiesId;

	public ActivityEntity() {
		super();
		this.relation = Relation.RELATION_USAGE;
		this.activitiesId = new ArrayList<>();
		this.entitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public ActivityEntity(Document d) {
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
		Collections.sort(this.activitiesId);
		Collections.sort(this.entitiesId);
		matrix = new CRSMatrix(activitiesId.size(), entitiesId.size());
	}

	public ActivityEntity(List<String> activitiesList, List<String> entitiesList) {
		this();
		this.activitiesId = activitiesList;
		this.entitiesId = entitiesList;
		matrix = new CRSMatrix(activitiesList.size(), entitiesList.size());
	}

	public void add(List<String> activitiesList, List<String> entitiesList) {

		if (activitiesList != null) {
			for (String ac : activitiesList) {
				if (!this.activitiesId.contains(ac)) {
					this.activitiesId.add(ac);
				}
			}
		}
		if (entitiesList != null) {
			for (String e : entitiesList) {
				if (!this.entitiesId.contains(e)) {
					this.entitiesId.add(e);
				}
			}
		}
		if (matrix.rows() != this.getRowDescriptors().size()
				|| matrix.columns() != this.getColumnDescriptors().size()) {
			matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
		}
	}

	private void buildIndex(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == Kind.PROV_ACTIVITY) {
			Activity ac = (Activity) sb;
			activitiesId.add(id(ac.getId()));
		} else if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
			Entity et = (Entity) sb;
			entitiesId.add(id(et.getId()));
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

	private void processStatements(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			processStatement(iterator.next());
		}
	}

	private void processStatement(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == this.relation.getKind()) {
			Used used = (Used) sb;
			int i = activitiesId.indexOf(id(used.getActivity()));
			int j = entitiesId.indexOf(id(used.getEntity()));
			matrix.set(i, j, matrix.get(i, j) + 1);
		}
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getActivitiesId() {
		return activitiesId;
	}

	public void setActivitiesId(List<String> activitiesId) {
		this.activitiesId = activitiesId;
	}

	public List<String> getEntitiesId() {
		return entitiesId;
	}

	public void setEntitiesId(List<String> entitiesId) {
		this.entitiesId = entitiesId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getEntitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getActivitiesId();
	}

	@Override
	public Relation getRelation() {
		return this.relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String getRowDimentionName() {
		return ProvType.PROV_ACTIVITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ACTIVITY;
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
		int i = this.activitiesId.indexOf(src);
		int j = this.entitiesId.indexOf(dest);
		if (i == -1) {
			this.activitiesId.add(src);
			i = this.activitiesId.indexOf(src);
		}
		if (j == -1) {
			this.entitiesId.add(dest);
			j = this.entitiesId.indexOf(dest);
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
