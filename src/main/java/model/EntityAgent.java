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
		return ProvMatrix.PROV_ENTITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ENTITY;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvMatrix.PROV_AGENT;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_AGENT;
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
		this.matrix.set(i, j, this.matrix.get(i, j) + 1);
	}

}
