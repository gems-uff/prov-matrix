package convertion;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.ActivityActivity;
import model.ActivityAgent;
import model.ActivityEntity;
import model.AgentAgent;
import model.EntityActivity;
import model.EntityAgent;
import model.EntityEntity;
import model.ProvMatrix;
import model.ProvRelation.Relation;
import model.ProvType;
import reader.ProvReader;
import reader.ProvStatement;

public class ProvMatrixExtendedFactory implements ProvMatrixFactory {

	private ProvReader[] pReader;
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
	private Map<String, String> labels;
	private HashMap<String, String> cellParams;

	public ProvMatrixExtendedFactory() {
		this.agents = new HashSet<>();
		this.entities = new HashSet<>();
		this.activities = new HashSet<>();
		this.labels = new HashMap<String, String>();
	}

	public ProvMatrixExtendedFactory(String[] fileName, String dir) throws URISyntaxException, IOException {
		this();
		this.pReader = new ProvReader[fileName.length];
		for (int i = 0; i < fileName.length; i++) {
			String path = fileName[i];
			if (path != null && !path.contains(":\\")) {
				path = dir + path;
			}
			this.pReader[i] = new ProvReader(path);
			this.pReader[i].readFile();
		}
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

	public List<ProvMatrix> buildMatrices(boolean deriveInfluence) {
		List<ProvMatrix> matrices = new ArrayList<>();
		for (int i = 0; i < pReader.length; i++) {
			List<String> agentsList = new ArrayList<>();
			for (ProvType provType : pReader[i].getAgents()) {
				agentsList.add(provType.getName());
			}
			List<String> activitiesList = new ArrayList<>();
			for (ProvType provType : pReader[i].getActivities()) {
				activitiesList.add(provType.getName());
			}
			List<String> entitiesList = new ArrayList<>();
			for (ProvType provType : pReader[i].getEntities()) {
				entitiesList.add(provType.getName());
			}
			if (this.wasInformedBy == null) {
				this.wasInformedBy = new ActivityActivity(activitiesList);
				this.getWasInformedBy().setRelation(Relation.RELATION_COMMUNICATION);
			} else {
				this.getWasInformedBy().setRelation(Relation.RELATION_COMMUNICATION);
				this.wasInformedBy.add(activitiesList);
			}
			if (this.wasStartedBy == null) {
				this.wasStartedBy = new ActivityActivity(activitiesList);
				this.wasStartedBy.setRelation(Relation.RELATION_START);
			} else {
				this.wasStartedBy.setRelation(Relation.RELATION_START);
				this.wasStartedBy.add(activitiesList);
			}
			if (this.wasEndedBy == null) {
				this.wasEndedBy = new ActivityActivity(activitiesList);
				this.wasEndedBy.setRelation(Relation.RELATION_END);
			} else {
				this.wasEndedBy.add(activitiesList);
				this.wasEndedBy.setRelation(Relation.RELATION_END);
			}
			if (this.wasInfluencedByAcAc == null) {
				this.wasInfluencedByAcAc = new ActivityActivity(activitiesList);
				this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByAcAc.add(activitiesList);
				this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
			}

			if (this.wasAssociatedWith == null) {
				this.wasAssociatedWith = new ActivityAgent(activitiesList, agentsList);
				this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
			} else {
				this.wasAssociatedWith.add(activitiesList, agentsList);
				this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
			}
			if (this.wasInfluencedByAcAg == null) {
				this.wasInfluencedByAcAg = new ActivityAgent(activitiesList, agentsList);
				this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByAcAg.add(activitiesList, agentsList);
				this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
			}

			if (this.used == null) {
				this.used = new ActivityEntity(activitiesList, entitiesList);
				this.used.setRelation(Relation.RELATION_USAGE);
			} else {
				this.used.add(activitiesList, entitiesList);
				this.used.setRelation(Relation.RELATION_USAGE);
			}

			if (this.wasInfluencedByAcE == null) {
				this.wasInfluencedByAcE = new ActivityEntity(activitiesList, entitiesList);
				this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByAcE.add(activitiesList, entitiesList);
				this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
			}

			if (this.wasGeneratedBy == null) {
				this.wasGeneratedBy = new EntityActivity(entitiesList, activitiesList);
				this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
			} else {
				this.wasGeneratedBy.add(entitiesList, activitiesList);
				this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
			}
			if (this.wasInfluencedByEAc == null) {
				this.wasInfluencedByEAc = new EntityActivity(entitiesList, activitiesList);
				this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
				this.wasInfluencedByEAc.add(entitiesList, activitiesList);
			}
			if (this.wasInvalidatedBy == null) {
				this.wasInvalidatedBy = new EntityActivity(entitiesList, activitiesList);
				this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
			} else {
				this.wasInvalidatedBy.add(entitiesList, activitiesList);
				this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
			}

			if (this.wasAttributedTo == null) {
				this.wasAttributedTo = new EntityAgent(entitiesList, agentsList);
				this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
			} else {
				this.wasAttributedTo.add(entitiesList, agentsList);
				this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
			}
			if (this.wasInfluencedByEAg == null) {
				this.wasInfluencedByEAg = new EntityAgent(entitiesList, agentsList);
				this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByEAg.add(entitiesList, agentsList);
				this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
			}

			if (this.wasDerivedFrom == null) {
				this.wasDerivedFrom = new EntityEntity(entitiesList);
				this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
			} else {
				this.wasDerivedFrom.add(entitiesList);
				this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
			}
			if (this.wasInfluencedByEE == null) {
				this.wasInfluencedByEE = new EntityEntity(entitiesList);
				this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByEE.add(entitiesList);
				this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
			}
			if (this.specializationOf == null) {
				this.specializationOf = new EntityEntity(entitiesList);
				this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
			} else {
				this.specializationOf.add(entitiesList);
				this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
			}
			if (this.alternateOf == null) {
				this.alternateOf = new EntityEntity(entitiesList);
				this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
			} else {
				this.alternateOf.add(entitiesList);
				this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
			}
			if (this.mentionOf == null) {
				this.mentionOf = new EntityEntity(entitiesList);
			} else {
				this.mentionOf.add(entitiesList);
			}
			if (this.hadMember == null) {
				this.hadMember = new EntityEntity(entitiesList);
			} else {
				this.hadMember.add(entitiesList);
			}

			if (this.actedOnBehalfOf == null) {
				this.actedOnBehalfOf = new AgentAgent(agentsList);
				this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
			} else {
				this.actedOnBehalfOf.add(agentsList);
				this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
			}
			if (this.wasInfluencedByAgAg == null) {
				this.wasInfluencedByAgAg = new AgentAgent(agentsList);
				this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
			} else {
				this.wasInfluencedByAgAg.add(agentsList);
				this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
			}

			Collection<ProvStatement> edges = this.pReader[i].getStatements();
			for (ProvStatement e : edges) {
				if (e != null) {
					ProvType src = e.getSource();
					ProvType dst = e.getDestination();
					if (src != null && dst != null) {
						switch (e.getRelation()) {
						case RELATION_COMMUNICATION: {
							if (deriveInfluence) {
								this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcAc.add(src.getName(), dst.getName());
							}
							this.wasInformedBy.setRelation(Relation.RELATION_COMMUNICATION);
							this.wasInformedBy.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_START: {
							if (src != null && dst != null) {
								if (deriveInfluence) {
									this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
									this.wasInfluencedByAcAc.add(src.getName(), dst.getName());
								}
								this.wasStartedBy.setRelation(Relation.RELATION_START);
								this.wasStartedBy.add(src.getName(), dst.getName());
							}
							break;
						}
						case RELATION_END: {
							if (deriveInfluence) {
								this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcAc.add(src.getName(), dst.getName());
							}
							this.wasEndedBy.setRelation(Relation.RELATION_END);
							this.wasEndedBy.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_ASSOCIATION: {
							if (deriveInfluence) {
								this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcAg.add(src.getName(), dst.getName());
							}
							this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
							if (activitiesList.contains(src.getName()) && agentsList.contains(dst.getName())) {
								this.wasAssociatedWith.add(src.getName(), dst.getName());
							} else {
								System.out.println("Não encontrou act:" + src.getName() + ", ag: " + dst.getName());
								throw new RuntimeException(
										"Não encontrou act:" + src.getName() + ", ag: " + dst.getName());
							}
							break;
						}
						case RELATION_ATTRIBUTION: {
							if (deriveInfluence) {
								this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEAg.add(src.getName(), dst.getName());
							}
							this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
							this.wasAttributedTo.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_USAGE: {
							if (deriveInfluence) {
								this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcE.add(src.getName(), dst.getName());
							}
							this.used.setRelation(Relation.RELATION_USAGE);
							this.used.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_INVALIDATION: {
							if (deriveInfluence) {
								this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEAc.add(src.getName(), dst.getName());
							}
							this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
							this.wasInvalidatedBy.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_GENERATION: {
							if (deriveInfluence) {
								this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEAc.add(src.getName(), dst.getName());
							}
							this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
							this.wasGeneratedBy.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_DERIVATION: {
							if (deriveInfluence) {
								this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEE.add(src.getName(), dst.getName());
							}
							this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
							this.wasDerivedFrom.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_SPECIALIZATION: {
							if (deriveInfluence) {
								this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEE.add(src.getName(), dst.getName());
							}
							this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
							this.specializationOf.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_ALTERNATE: {
							if (deriveInfluence) {
								this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEE.add(src.getName(), dst.getName());
							}
							this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
							this.alternateOf.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_MENTION: {
							if (deriveInfluence) {
								this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEE.add(dst.getName(), src.getName());
							}
							this.mentionOf.setRelation(Relation.RELATION_MENTION);
							this.mentionOf.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_MEMBERSHIP: {
							this.hadMember.setRelation(Relation.RELATION_MEMBERSHIP);
							this.hadMember.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_DELEGATION: {
							if (deriveInfluence) {
								this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAgAg.add(src.getName(), dst.getName());
							}
							this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
							this.actedOnBehalfOf.add(src.getName(), dst.getName());
							break;
						}
						case RELATION_INFLUENCE: {
							if (entities.contains(src.getName()) && entities.contains(dst.getName())) {
								this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEE.add(src.getName(), dst.getName());
							} else if (agents.contains(src.getName()) && agents.contains(dst.getName())) {
								this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAgAg.add(src.getName(), dst.getName());
							} else if (activities.contains(src.getName()) && activities.contains(dst.getName())) {
								this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcAc.add(src.getName(), dst.getName());
							} else if (activities.contains(src.getName()) && entities.contains(dst.getName())) {
								this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcE.add(src.getName(), dst.getName());
							} else if (activities.contains(src.getName()) && agents.contains(dst.getName())) {
								this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByAcAg.add(src.getName(), dst.getName());
							} else if (entities.contains(src.getName()) && activities.contains(dst.getName())) {
								this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEAc.add(src.getName(), dst.getName());
							} else if (entities.contains(src.getName()) && agents.contains(dst.getName())) {
								this.wasAttributedTo.setRelation(Relation.RELATION_INFLUENCE);
								this.wasInfluencedByEAg.add(src.getName(), dst.getName());
							}
							break;
						}
						default:
							break;
						}
					}
				}
			}
		}
		add(matrices, this.wasInformedBy);
		add(matrices, this.wasStartedBy);
		add(matrices, this.wasEndedBy);
		add(matrices, this.wasInfluencedByAcAc);
		add(matrices, this.wasAssociatedWith);
		add(matrices, this.wasInfluencedByAcAg);
		add(matrices, this.wasAttributedTo);
		add(matrices, this.wasInfluencedByEAg);
		add(matrices, this.used);
		add(matrices, this.wasInfluencedByAcE);
		add(matrices, this.wasInvalidatedBy);
		add(matrices, this.wasGeneratedBy);
		add(matrices, this.wasInfluencedByEAc);
		add(matrices, this.wasDerivedFrom);
		add(matrices, this.specializationOf);
		add(matrices, this.alternateOf);
		add(matrices, this.mentionOf);
		add(matrices, this.hadMember);
		add(matrices, this.wasInfluencedByEE);
		add(matrices, this.actedOnBehalfOf);
		add(matrices, this.wasInfluencedByAgAg);
		return matrices;

	}

	private void add(List<ProvMatrix> matrices, ProvMatrix provMatrix) {
		if (!provMatrix.isEmpty()) {
			matrices.add(provMatrix);
		}
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

	public Map<String, String> getDimensionLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public HashMap<String, String> getCellParams() {
		return cellParams;
	}

	public void setCellParams(HashMap<String, String> cellParams) {
		this.cellParams = cellParams;
	}

}
