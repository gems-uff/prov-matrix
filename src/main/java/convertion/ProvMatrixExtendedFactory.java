package convertion;
/*
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.uff.ic.utility.IO.PROVNReader;
import br.uff.ic.utility.graph.ActivityVertex;
import br.uff.ic.utility.graph.AgentVertex;
import br.uff.ic.utility.graph.Edge;
import br.uff.ic.utility.graph.EntityVertex;
import br.uff.ic.utility.graph.Vertex;
import model.ActivityActivity;
import model.ActivityAgent;
import model.ActivityEntity;
import model.AgentAgent;
import model.EntityActivity;
import model.EntityAgent;
import model.EntityEntity;
import model.ProvMatrix;
import model.ProvMatrix.Relation;*/

public class ProvMatrixGameFactory {
/*
	private PROVNReader pReader;
	private ActivityActivity wasInformedBy;
	private ActivityActivity wasStartedBy;
	private ActivityActivity wasEndedBy;
	private ActivityActivity wasInfluencedByAcAc;

	private ActivityAgent wasAssociatedWith;
	private ActivityAgent wasInfluencedByAcAg;

	private ActivityEntity used;
	private ActivityEntity wasInfluencedByAcE;

	private EntityActivity wasInvalidatedBy;
	private EntityActivity wasGeneratedBy;
	private EntityActivity wasInfluencedByEAc;

	private EntityAgent wasAttributedTo;
	private EntityAgent wasInfluencedByEAg;

	private EntityEntity wasDerivedFrom;
	private EntityEntity specializationOf;
	private EntityEntity alternateOf;
	private EntityEntity mentionOf;
	private EntityEntity hadMember;
	private EntityEntity wasInfluencedByEE;

	private AgentAgent actedOnBehalfOf;
	private AgentAgent wasInfluencedByAgAg;

	private Set<String> agents;
	private Set<String> entities;
	private Set<String> activities;

	public ProvMatrixGameFactory() {
		super();
	}

	public ProvMatrixGameFactory(String fileName) throws URISyntaxException, IOException {
		this.pReader = new PROVNReader(new File(fileName));
		this.pReader.readFile();
		this.agents = new HashSet<>();
		this.entities = new HashSet<>();
		this.activities = new HashSet<>();
		this.wasInformedBy = new ActivityActivity();
		this.wasStartedBy = new ActivityActivity();
		this.wasEndedBy = new ActivityActivity();
		this.wasInfluencedByAcAc = new ActivityActivity();
		this.wasAssociatedWith = new ActivityAgent();
		this.wasAttributedTo = new EntityAgent();
		this.wasInfluencedByEAg = new EntityAgent();
		this.wasInfluencedByAcAg = new ActivityAgent();
		this.used = new ActivityEntity();
		this.wasInfluencedByAcE = new ActivityEntity();
		this.wasInvalidatedBy = new EntityActivity();
		this.wasGeneratedBy = new EntityActivity();
		this.wasInfluencedByEAc = new EntityActivity();
		this.wasDerivedFrom = new EntityEntity();
		this.specializationOf = new EntityEntity();
		this.alternateOf = new EntityEntity();
		this.mentionOf = new EntityEntity();
		this.hadMember = new EntityEntity();
		this.wasInfluencedByEE = new EntityEntity();
		this.actedOnBehalfOf = new AgentAgent();
		this.wasInfluencedByAgAg = new AgentAgent();
	}

	public ActivityActivity getWasInfluencedByAA() {
		return wasInfluencedByAcAc;
	}

	public void setWasInfluencedByAA(ActivityActivity wasInfluencedByAA) {
		this.wasInfluencedByAcAc = wasInfluencedByAA;
	}

	public EntityEntity getWasInfluencedByEE() {
		return wasInfluencedByEE;
	}

	public void setWasInfluencedByEE(EntityEntity wasInfluencedByEE) {
		this.wasInfluencedByEE = wasInfluencedByEE;
	}

	public List<ProvMatrix> buildMatrices() {
		List<ProvMatrix> matrices = new ArrayList<>();
		Collection<Vertex> vertices = this.pReader.getNodes();
		for (Vertex v : vertices) {
			if (v != null) {
				if (v instanceof AgentVertex) {
					if (!id(v).equals("")) {
						agents.add(id(v));
					}
				} else if (v instanceof ActivityVertex) {
					if (!id(v).equals("")) {
						activities.add(id(v));
					}
				} else if (v instanceof EntityVertex) {
					if (!id(v).equals("")) {
						this.entities.add(id(v));
					}
				}
			}
		}
		List<String> agentsList = new ArrayList<>(agents);
		Collections.sort(agentsList);
		List<String> activitiesList = new ArrayList<>(activities);
		Collections.sort(activitiesList);
		List<String> entitiesList = new ArrayList<>(entities);
		Collections.sort(entitiesList);
		this.wasInformedBy = new ActivityActivity(activitiesList);
		this.wasStartedBy = new ActivityActivity(activitiesList);
		this.wasEndedBy = new ActivityActivity(activitiesList);
		this.wasInfluencedByAcAc = new ActivityActivity(activitiesList);

		this.wasAssociatedWith = new ActivityAgent(activitiesList, agentsList);
		this.wasInfluencedByAcAg = new ActivityAgent(activitiesList, agentsList);

		this.used = new ActivityEntity(activitiesList, entitiesList);
		this.wasInfluencedByAcE = new ActivityEntity(activitiesList, entitiesList);

		this.wasInvalidatedBy = new EntityActivity(entitiesList, activitiesList);
		this.wasGeneratedBy = new EntityActivity(entitiesList, activitiesList);
		this.wasInfluencedByEAc = new EntityActivity(entitiesList, activitiesList);

		this.wasAttributedTo = new EntityAgent(entitiesList, agentsList);
		this.wasInfluencedByEAg = new EntityAgent(entitiesList, agentsList);

		this.wasDerivedFrom = new EntityEntity(entitiesList);
		this.specializationOf = new EntityEntity(entitiesList);
		this.alternateOf = new EntityEntity(entitiesList);
		this.mentionOf = new EntityEntity(entitiesList);
		this.hadMember = new EntityEntity(entitiesList);
		this.wasInfluencedByEE = new EntityEntity(entitiesList);

		this.actedOnBehalfOf = new AgentAgent(agentsList);
		this.wasInfluencedByAgAg = new AgentAgent(agentsList);

		Collection<Edge> edges = this.pReader.getEdges();
		for (Edge e : edges) {
			if (e != null) {
				Vertex src = (Vertex) e.getSource();
				Vertex dst = (Vertex) e.getTarget();
				switch (e.getType()) {
				case ProvMatrix.PROV_COMMUNICATION: {
					this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAcAc.add(id(src), id(dst));
					this.wasInformedBy.setRelation(Relation.RELATION_COMMUNICATION);
					this.wasInformedBy.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_START: {
					this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAcAc.add(id(src), id(dst));
					this.wasStartedBy.setRelation(Relation.RELATION_START);
					this.wasStartedBy.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_END: {
					this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAcAc.add(id(src), id(dst));
					this.wasEndedBy.setRelation(Relation.RELATION_END);
					this.wasEndedBy.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_ASSOCIATION: {
					this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAcAg.add(id(src), id(dst));
					this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
					if (this.activities.contains(id(src)) && this.agents.contains(id(dst))) {
						this.wasAssociatedWith.add(id(src), id(dst));
					} else {
						System.out.println("Não encontrou act:"+id(src)+", ag: "+id(dst));
						throw new RuntimeException("Não encontrou act:"+id(src)+", ag: "+id(dst));
					}
					break;
				}
				case ProvMatrix.PROV_ATTRIBUTION: {
					this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEAg.add(id(src), id(dst));
					this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
					this.wasAttributedTo.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_USAGE: {
					this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAcE.add(id(src), id(dst));
					this.used.setRelation(Relation.RELATION_USAGE);
					this.used.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_INVALIDATION: {
					this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEAc.add(id(src), e.getLabel());
					this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
					this.wasInvalidatedBy.add(id(src), e.getLabel());
					break;
				}
				case ProvMatrix.PROV_GENERATION: {
					this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEAc.add(id(src), e.getLabel());
					this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
					this.wasGeneratedBy.add(id(src), e.getLabel());
					break;
				}
				case ProvMatrix.PROV_DERIVATION: {
					this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEE.add(id(src), id(dst));
					this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
					this.wasDerivedFrom.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_SPECIALIZATION: {
					this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEE.add(id(src), id(dst));
					this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
					this.specializationOf.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_ALTERNATE: {
					this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
					this.alternateOf.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_MENTION: {
					this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByEE.add(id(dst), id(src));
					this.mentionOf.setRelation(Relation.RELATION_MENTION);
					this.mentionOf.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_MEMBERSHIP: {
					this.hadMember.setRelation(Relation.RELATION_MEMBERSHIP);
					this.hadMember.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_DELEGATION: {
					this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
					this.wasInfluencedByAgAg.add(id(src), id(dst));
					this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
					this.actedOnBehalfOf.add(id(src), id(dst));
					break;
				}
				case ProvMatrix.PROV_INFLUENCE: {
					if (entities.contains(id(src)) && entities.contains(id(dst))) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(src), id(dst));
					} else if (agents.contains(id(src)) && agents.contains(id(dst))) {
						this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAgAg.add(id(src), id(dst));
					} else if (activities.contains(id(src)) && activities.contains(id(dst))) {
						this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAc.add(id(src), id(dst));
					} else if (activities.contains(id(src)) && entities.contains(id(dst))) {
						this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcE.add(id(src), id(dst));
					} else if (activities.contains(id(src)) && agents.contains(id(dst))) {
						this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAg.add(id(src), id(dst));
					} else if (entities.contains(id(src)) && activities.contains(id(dst))) {
						this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAc.add(id(src), id(dst));
					} else if (entities.contains(id(src)) && agents.contains(id(dst))) {
						this.wasAttributedTo.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAg.add(id(src), id(dst));
					}
					break;
				}
				default:
					break;
				}
			}
		}
		matrices.add(this.wasInformedBy);
		matrices.add(this.wasStartedBy);
		matrices.add(this.wasEndedBy);
		matrices.add(this.wasInfluencedByAcAc);
		matrices.add(this.wasAssociatedWith);
		matrices.add(this.wasInfluencedByAcAg);
		matrices.add(this.wasAttributedTo);
		matrices.add(this.wasInfluencedByEAg);
		matrices.add(this.used);
		matrices.add(this.wasInfluencedByAcE);
		matrices.add(this.wasInvalidatedBy);
		matrices.add(this.wasGeneratedBy);
		matrices.add(this.wasInfluencedByEAc);
		matrices.add(this.wasDerivedFrom);
		matrices.add(this.specializationOf);
		matrices.add(this.alternateOf);
		matrices.add(this.mentionOf);
		matrices.add(this.hadMember);
		matrices.add(this.wasInfluencedByEE);
		matrices.add(this.actedOnBehalfOf);
		matrices.add(this.wasInfluencedByAgAg);
		return matrices;
	}

	public String id(Vertex vertex) {
		String id = "";
		if (vertex instanceof ActivityVertex) {
			id = vertex.getAttributeValue("Label").replace(" ", "");
		} else {
			id = vertex.getID(); 
		}
		return id.contains("Unknown") ? "" : id;
	}

	public void setWasInfluencedBy(EntityEntity wasInfluencedBy) {
		this.wasInfluencedByEE = wasInfluencedBy;
	}

	public EntityEntity getWasInfluencedBy() {
		return wasInfluencedByEE;
	}

	public ActivityActivity getWasInformedBy() {
		return wasInformedBy;
	}

	public void setWasInformedBy(ActivityActivity wasInformedBy) {
		this.wasInformedBy = wasInformedBy;
	}

	public ActivityActivity getStarted() {
		return wasStartedBy;
	}

	public void setStarted(ActivityActivity started) {
		this.wasStartedBy = started;
	}

	public ActivityActivity getEnded() {
		return wasEndedBy;
	}

	public void setEnded(ActivityActivity ended) {
		this.wasEndedBy = ended;
	}

	public ActivityAgent getWasAssociatedWith() {
		return wasAssociatedWith;
	}

	public void setWasAssociatedWith(ActivityAgent wasAssociatedWith) {
		this.wasAssociatedWith = wasAssociatedWith;
	}

	public ActivityEntity getUsed() {
		return used;
	}

	public void setUsed(ActivityEntity used) {
		this.used = used;
	}

	public EntityActivity getWasGeneratedBy() {
		return wasGeneratedBy;
	}

	public void setWasGeneratedBy(EntityActivity wasGeneratedBy) {
		this.wasGeneratedBy = wasGeneratedBy;
	}

	public EntityAgent getWasAttributedTo() {
		return wasAttributedTo;
	}

	public void setWasAttributedTo(EntityAgent wasAttributedTo) {
		this.wasAttributedTo = wasAttributedTo;
	}

	public AgentAgent getActedOnBehalfOf() {
		return actedOnBehalfOf;
	}

	public void setActedOnBehalfOf(AgentAgent actedOnBehalfOf) {
		this.actedOnBehalfOf = actedOnBehalfOf;
	}

	public ActivityActivity getWasInfluencedByAcAc() {
		return wasInfluencedByAcAc;
	}

	public void setWasInfluencedByAcAc(ActivityActivity wasInfluencedByAcAc) {
		this.wasInfluencedByAcAc = wasInfluencedByAcAc;
	}

	public AgentAgent getWasInfluencedByAgAg() {
		return wasInfluencedByAgAg;
	}

	public void setWasInfluencedByAgAg(AgentAgent wasInfluencedByAgAg) {
		this.wasInfluencedByAgAg = wasInfluencedByAgAg;
	}

	public ActivityActivity getWasStartedBy() {
		return wasStartedBy;
	}

	public void setWasStartedBy(ActivityActivity wasStartedBy) {
		this.wasStartedBy = wasStartedBy;
	}

	public ActivityActivity getWasEndedBy() {
		return wasEndedBy;
	}

	public void setWasEndedBy(ActivityActivity wasEndedBy) {
		this.wasEndedBy = wasEndedBy;
	}

	public ActivityAgent getWasInfluencedByAcAg() {
		return wasInfluencedByAcAg;
	}

	public void setWasInfluencedByAcAg(ActivityAgent wasInfluencedByAcAg) {
		this.wasInfluencedByAcAg = wasInfluencedByAcAg;
	}

	public ActivityEntity getWasInfluencedByAcE() {
		return wasInfluencedByAcE;
	}

	public void setWasInfluencedByAcE(ActivityEntity wasInfluencedByAcE) {
		this.wasInfluencedByAcE = wasInfluencedByAcE;
	}

	public EntityActivity getWasInfluencedByEAc() {
		return wasInfluencedByEAc;
	}

	public void setWasInfluencedByEAc(EntityActivity wasInfluencedByEAc) {
		this.wasInfluencedByEAc = wasInfluencedByEAc;
	}

	public EntityEntity getWasDerivedFrom() {
		return wasDerivedFrom;
	}

	public void setWasDerivedFrom(EntityEntity wasDerivedFrom) {
		this.wasDerivedFrom = wasDerivedFrom;
	}

	public EntityEntity getAlternateOf() {
		return alternateOf;
	}

	public void setAlternateOf(EntityEntity alternateOf) {
		this.alternateOf = alternateOf;
	}

	public EntityEntity getSpecializationOf() {
		return specializationOf;
	}

	public void setSpecializationOf(EntityEntity specializationOf) {
		this.specializationOf = specializationOf;
	}

	public EntityEntity getMentionOf() {
		return mentionOf;
	}

	public void setMentionOf(EntityEntity mentionOf) {
		this.mentionOf = mentionOf;
	}

	public EntityEntity getHadMember() {
		return hadMember;
	}

	public void setHadMember(EntityEntity hadMember) {
		this.hadMember = hadMember;
	}

	public EntityAgent getWasInfluencedByEAg() {
		return wasInfluencedByEAg;
	}

	public void setWasInfluencedByEAg(EntityAgent wasInfluencedByEAg) {
		this.wasInfluencedByEAg = wasInfluencedByEAg;
	}

	public EntityActivity getWasInvalidatedBy() {
		return wasInvalidatedBy;
	}

	public void setWasInvalidatedBy(EntityActivity wasInvalidatedBy) {
		this.wasInvalidatedBy = wasInvalidatedBy;
	}
*/
}
