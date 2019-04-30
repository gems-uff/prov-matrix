package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasAssociatedWith;

/**
 * @author Victor
 * 
 * Represents Activity -> Agent : wasAssociatedWith
 *
 */
public class ActivityAgent implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> activitiesId;
	private List<String> agentsId;

	public ActivityAgent() {
		this.relation = Relation.RELATION_ASSOCIATION;
		this.agentsId = new ArrayList<>();
		this.activitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public ActivityAgent(Document d) {
		this();
		this.document = d;
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == Kind.PROV_ACTIVITY) {
				Activity ac = (Activity) sb;
				activitiesId.add(ac.getId().getLocalPart());
			} else if (sb.getKind() == Kind.PROV_AGENT) {
				Agent ag = (Agent) sb;
				agentsId.add(ag.getId().getLocalPart());
			}
		}
		matrix = new CRSMatrix(agentsId.size(), activitiesId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == this.relation.getKind()) {
				WasAssociatedWith wa = (WasAssociatedWith) sb;
				int i = activitiesId.indexOf(wa.getActivity().getLocalPart());
				int j = agentsId.indexOf(wa.getAgent().getLocalPart());
				matrix.set(i, j, matrix.get(i, j) + 1);
			}
		}
	}

	@Override
	public CRSMatrix getMatrix() {
		return this.matrix;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.agentsId;
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.activitiesId;
	}

	public List<String> getAgentsId() {
		return agentsId;
	}

	public void setAgentsId(List<String> entitiesId) {
		this.agentsId = entitiesId;
	}

	public List<String> getActivitiesId() {
		return activitiesId;
	}

	public void setActivitiesId(List<String> activitiesId) {
		this.activitiesId = activitiesId;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

}
