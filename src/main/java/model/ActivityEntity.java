package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.Used;

/**
 * @author Victor
 * 
 * Represents Activity -> Entity : Used
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
			if (sb.getKind() == Kind.PROV_ACTIVITY) {
				Activity ac = (Activity) sb;
				activitiesId.add(id(ac.getId()));
			} else if (sb.getKind() == Kind.PROV_ENTITY) {
				Entity et = (Entity) sb;
				entitiesId.add(id(et.getId()));
			}
		}
		matrix = new CRSMatrix(activitiesId.size(), entitiesId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == this.relation.getKind()) {
				Used used = (Used) sb;
				int i = activitiesId.indexOf(id(used.getActivity()));
				int j = entitiesId.indexOf(id(used.getEntity()));
				matrix.set(i, j, matrix.get(i, j) + 1);
			}
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
		return ProvMatrix.PROV_ACTIVITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {		
		return ProvMatrix.PROV_ABBREVIATE_ACTIVITY;
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
