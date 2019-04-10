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
 * Represents Activity -> Agent | WasAssociatedWith
 *
 */
public class ActivityAgent implements ProvMatrix {

	private CRSMatrix matrix;
	private Document document;
	private List<String> activitiesId;
	private List<String> agentsId;

	public ActivityAgent() {
		super();
		this.activitiesId = new ArrayList<>();
		this.agentsId = new ArrayList<>();
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
		matrix = new CRSMatrix(activitiesId.size(), agentsId.size());
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == Kind.PROV_ASSOCIATION) {
				WasAssociatedWith wa = (WasAssociatedWith) sb;
				int i = activitiesId.indexOf(wa.getActivity().getLocalPart());
				int j = agentsId.indexOf(wa.getAgent().getLocalPart());
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

	public List<String> getAgentsId() {
		return agentsId;
	}

	public void setAgentsId(List<String> agentsId) {
		this.agentsId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getAgentsId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getActivitiesId();
	}

}
