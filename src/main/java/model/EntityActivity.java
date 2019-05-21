package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;

/**
 * @author Victor
 * 
 * Represents Entity -> Activity : WasGeneratedBy || WasInvalidatedBy
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
			if (sb!=null && sb.getKind() == Kind.PROV_ENTITY) {
				Entity et = (Entity) sb;
				entitiesId.add(id(et.getId()));
			} else if (sb!=null && sb.getKind() == Kind.PROV_ACTIVITY) {
				Activity ac = (Activity) sb;
				activitiesId.add(id(ac.getId()));
			}
		}
		Collections.sort(this.entitiesId);
		Collections.sort(this.activitiesId);
		matrix = new CRSMatrix(entitiesId.size(), activitiesId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb!=null && sb.getKind() == this.relation.getKind()) {
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
		return ProvMatrix.PROV_ENTITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {		
		return ProvMatrix.PROV_ABBREVIATE_ENTITY;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvMatrix.PROV_ACTIVITY;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ACTIVITY;
	}

}
