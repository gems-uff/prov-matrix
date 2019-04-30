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
public class ActivityEntity implements ProvMatrix {

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
				activitiesId.add(ac.getId().getLocalPart());
			} else if (sb.getKind() == Kind.PROV_ENTITY) {
				Entity et = (Entity) sb;
				entitiesId.add(et.getId().getLocalPart());
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
				int i = activitiesId.indexOf(used.getActivity().getLocalPart());
				int j = entitiesId.indexOf(used.getEntity().getLocalPart());
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

}
