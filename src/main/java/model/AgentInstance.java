package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Document;

/**
 * @author Victor
 * 
 *         Represents Agent -> Instance of Agent Type (prov:type) : AgT
 *
 */
public class AgentInstance extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originAgentsId;
	private List<String> destinationTypeAgentsId;

	public AgentInstance() {
		super();
		this.relation = Relation.RELATION_AGENT_INSTANCE;
		this.originAgentsId = new ArrayList<>();
		this.destinationTypeAgentsId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public AgentInstance(List<String> agentsList, Set<String> agentTypes) {
		this();
		this.originAgentsId = agentsList;
		this.destinationTypeAgentsId = new ArrayList<String>();
		for (String agg : agentTypes) {
			if (agg.contains("::") && !agg.endsWith("::")) {
				String[] aggs = agg.split("::");
				for (int i = 0; i < aggs.length; i++) {
					if (!destinationTypeAgentsId.contains(aggs[i])) {
						destinationTypeAgentsId.add(aggs[i]);
					}
				}
			} else {
				if (!destinationTypeAgentsId.contains(agg)) {
					destinationTypeAgentsId.add(agg);
				}
			}
		}
		matrix = new CRSMatrix(originAgentsId.size(), destinationTypeAgentsId.size());
	}

	public void add(String src, String dest) {
		int i = this.originAgentsId.indexOf(src);
		int j = this.destinationTypeAgentsId.indexOf(dest);
		if (i == -1) {
			this.originAgentsId.add(src);
			i = this.originAgentsId.indexOf(src);
		}
		if (j == -1) {
			this.destinationTypeAgentsId.add(dest);
			j = this.destinationTypeAgentsId.indexOf(dest);
		}
		if (matrix.rows() != this.getRowDescriptors().size()
				|| matrix.columns() != this.getColumnDescriptors().size()) {
			matrix = super.growMatrix(matrix, this.getRowDescriptors().size(), this.getColumnDescriptors().size());
		}
		this.matrix.set(i, j, this.matrix.get(i, j) + 1);
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

	public List<String> getDestinationTypeAgentsId() {
		return destinationTypeAgentsId;
	}

	public void setDestinationTypeAgentsId(List<String> agentsId) {
		this.destinationTypeAgentsId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationTypeAgentsId();
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
		return ProvType.PROV_AGENT;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_AGENT;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvType.PROV_TYPE;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_TYPE;
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