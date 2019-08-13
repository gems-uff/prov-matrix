package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasAssociatedWith;

/**
 * @author Victor
 * 
 *         Represents Activity -> Agent : wasAssociatedWith
 *
 */
public class ActivityAgent extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> activitiesId;
	private List<String> agentsId;

	public ActivityAgent() {
		this.relation = Relation.RELATION_ASSOCIATION;
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
		Collections.sort(this.agentsId);
		matrix = new CRSMatrix(activitiesId.size(), agentsId.size());
	}

	public ActivityAgent(List<String> activitiesList, List<String> agentsList) {
		this();
		this.activitiesId = activitiesList;
		this.agentsId = agentsList;
		this.matrix = new CRSMatrix(activitiesList.size(), agentsList.size());
	}

	public void add(List<String> activitiesList, List<String> agentsList) {

		if (activitiesList != null) {
			for (String ac : activitiesList) {
				if (!this.activitiesId.contains(ac)) {
					this.activitiesId.add(ac);
				}
			}
		}
		if (agentsList != null) {
			for (String ag : agentsList) {
				if (!this.agentsId.contains(ag)) {
					this.agentsId.add(ag);
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
		} else if (sb != null && sb.getKind() == Kind.PROV_AGENT) {
			Agent ag = (Agent) sb;
			agentsId.add(id(ag.getId()));
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
			WasAssociatedWith wa = (WasAssociatedWith) sb;
			int i = activitiesId.indexOf(id(wa.getActivity()));
			int j = agentsId.indexOf(id(wa.getAgent()));
			matrix.set(i, j, matrix.get(i, j) + 1);
		}
	}

	private void processStatements(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			processStatement(iterator.next());
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
		return ProvType.PROV_AGENT;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_AGENT;
	}

	public void add(String src, String dest) {
		int i = this.activitiesId.indexOf(src);
		int j = this.agentsId.indexOf(dest);
		if (i == -1) {
			this.activitiesId.add(src);
			i = this.activitiesId.indexOf(src);
		}
		if (j == -1) {
			this.agentsId.add(dest);
			j = this.agentsId.indexOf(dest);
		}
		this.matrix.set(i, j, this.matrix.get(i, j) + 1);
	}

	@Override
	public boolean isEmpty() {
		return this.matrix.density() == 0.0;
	}

}
