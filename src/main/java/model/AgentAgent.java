package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;

/**
 * @author Victor
 * 
 *         Represents Agent -> Agent : actedOnBehalfOf
 *
 */
public class AgentAgent extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originAgentsId;
	private List<String> destinationAgentsId;

	public AgentAgent() {
		super();
		this.relation = Relation.RELATION_DELEGATION;
		this.originAgentsId = new ArrayList<>();
		this.destinationAgentsId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public AgentAgent(Document d) {
		this();
		this.document = d;
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb instanceof Statement) {
				buildIndex(sb);
			} else {
				Bundle bundle = (Bundle) sb;
				buildBundleIndex(bundle.getStatement());
			}
		}
		Collections.sort(this.originAgentsId);
		Collections.sort(this.destinationAgentsId);
		matrix = new CRSMatrix(originAgentsId.size(), destinationAgentsId.size());
	}
	
	private void buildIndex(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == Kind.PROV_AGENT) {
			Agent ag = (Agent) sb;
			originAgentsId.add(id(ag.getId()));
			destinationAgentsId.add(id(ag.getId()));
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
			if (sb instanceof Statement) {
				processStatement(sb);
			} else {
				Bundle bundle = (Bundle) sb;
				processStatements(bundle.getStatement());
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
			ActedOnBehalfOf wd = (ActedOnBehalfOf) sb;
			int i = originAgentsId.indexOf(id(wd.getDelegate()));
			int j = destinationAgentsId.indexOf(id(wd.getResponsible()));
			matrix.set(i, j, matrix.get(i, j) + 1);
		}
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getOriginAgentsId() {
		return originAgentsId;
	}

	public void setOriginAgentsId(List<String> activitiesId) {
		this.originAgentsId = activitiesId;
	}

	public List<String> getDestinationAgentsId() {
		return destinationAgentsId;
	}

	public void setDestinationAgentsId(List<String> agentsId) {
		this.destinationAgentsId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationAgentsId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getOriginAgentsId();
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String getRowDimentionName() {
		return ProvMatrix.PROV_AGENT;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_AGENT;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvMatrix.PROV_AGENT;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_AGENT;
	}

}
