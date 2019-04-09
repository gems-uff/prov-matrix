package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasAttributedTo;

/**
 * @author Victor
 * 
 * Represents Entity -> Agent | WasAttributedTo
 *
 */
public class EntityAgent {

	private CRSMatrix matrix;
	private List<String> entitiesId;
	private List<String> agentsId;

	public EntityAgent() {
		super();
		this.entitiesId = new ArrayList<>();
		this.agentsId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityAgent(Document d) {
		this();
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == Kind.PROV_ENTITY) {
				Entity et = (Entity) sb;
				entitiesId.add(et.getId().getLocalPart());
			} else if (sb.getKind() == Kind.PROV_AGENT) {
				Agent ag = (Agent) sb;
				entitiesId.add(ag.getId().getLocalPart());
			}
		}
		matrix = new CRSMatrix(entitiesId.size(), agentsId.size());
	}

	public void buildMatrix(Document d) {
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb.getKind() == Kind.PROV_ATTRIBUTION) {
				WasAttributedTo wa = (WasAttributedTo) sb;
				int i = entitiesId.indexOf(wa.getEntity().getLocalPart());
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

}
