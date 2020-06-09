package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasAttributedTo;

/**
 * @author Victor
 * 
 *         Represents Entity -> Agent | WasAttributedTo
 *
 */
public class EntityAgent extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> entitiesId;
	private List<String> agentsId;

	public EntityAgent() {
		super();
		this.relation = Relation.RELATION_ATTRIBUTION;
		this.entitiesId = new ArrayList<>();
		this.agentsId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityAgent(Document d) {
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
		Collections.sort(this.agentsId);
		matrix = new CRSMatrix(entitiesId.size(), agentsId.size());
	}

	public EntityAgent(List<String> entitiesList, List<String> agentsList) {
		this.entitiesId = entitiesList;
		this.agentsId = agentsList;
		this.matrix = new CRSMatrix(entitiesId.size(), agentsId.size());
	}

	public void add(List<String> entitiesList, List<String> agentsList) {
		if (entitiesList != null) {
			for (String e : entitiesList) {
				if (!this.entitiesId.contains(e)) {
					this.entitiesId.add(e);
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
		if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
			Entity et = (Entity) sb;
			entitiesId.add(id(et.getId()));
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

	private void processStatements(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			processStatement(iterator.next());
		}
	}

	private void processStatement(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == this.relation.getKind()) {
			WasAttributedTo wa = (WasAttributedTo) sb;
			int i = entitiesId.indexOf(id(wa.getEntity()));
			int j = agentsId.indexOf(id(wa.getAgent()));
			matrix.set(i, j, matrix.get(i, j) + 1);
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
		return ProvType.PROV_AGENT;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_AGENT;
	}

	public void add(String src, String dest) {
		int i = this.entitiesId.indexOf(src);
		int j = this.agentsId.indexOf(dest);
		if (i == -1) {
			this.entitiesId.add(src);
			i = this.entitiesId.indexOf(src);
		}
		if (j == -1) {
			this.agentsId.add(dest);
			j = this.agentsId.indexOf(dest);
		}
		if (matrix.rows() != this.getRowDescriptors().size()
				|| matrix.columns() != this.getColumnDescriptors().size()) {
			matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
		}
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