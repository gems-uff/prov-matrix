package convertion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

import model.ActivityActivity;
import model.ActivityAgent;
import model.ActivityEntity;
import model.ActivityInstance;
import model.AgentAgent;
import model.AgentInstance;
import model.EntityActivity;
import model.EntityAgent;
import model.EntityEntity;
import model.EntityInstance;
import model.ProvMatrix;
import model.ProvRelation.Relation;

public class ProvMatrixDefaultFactory implements ProvMatrixFactory {

	private Document[] documents;
	private ActivityInstance activityInstanceOf;
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

	private EntityInstance entityInstanceOf;
	private EntityEntity wasDerivedFrom;
	private EntityEntity specializationOf;
	private EntityEntity alternateOf;
	private EntityEntity mentionOf;
	private EntityEntity hadMember;
	private EntityEntity wasInfluencedByEE;

	private AgentInstance agentInstanceOf;
	private AgentAgent actedOnBehalfOf;
	private AgentAgent wasInfluencedByAgAg;

	private Set<String> agents;
	private Set<String> entities;
	private Set<String> activities;
	private List<String> agentsList;
	private List<String> activitiesList;
	private List<String> entitiesList;
	private HashMap<String, String> cellParams;
	private Map<String, String> labels;
	private Map<String, String> entityTypes;
	private Map<String, String> activityTypes;
	private Map<String, String> agentTypes;

	public ProvMatrixDefaultFactory() {
		this.agents = new HashSet<>();
		this.entities = new HashSet<>();
		this.activities = new HashSet<>();
		this.labels = new HashMap<String, String>();
		this.cellParams = new HashMap<String, String>();
		this.entityTypes = new HashMap<String, String>();
		this.activityTypes = new HashMap<String, String>();
		this.agentTypes = new HashMap<String, String>();
	}

	public ProvMatrixDefaultFactory(String[] filePaths, String dir) {
		this();
		this.documents = new Document[filePaths.length];
		InteropFramework intF = new InteropFramework();
		for (int i = 0; i < filePaths.length; i++) {
			String path = filePaths[i];
			if (path != null && !path.contains(":\\")) {
				path = dir + path;
			}
			this.documents[i] = intF.readDocumentFromFile(path);
		}
	}

	public String id(QualifiedName qn) {
		String id = "";
		if (qn != null) {
			if (qn.getPrefix() != null) {
				if (qn.getPrefix().length() == 0) {
					id = qn.getLocalPart();
				} else {
					id = qn.getPrefix() + ":" + qn.getLocalPart();
				}
			} else {
				id = qn.getLocalPart();
			}
		}
		return id;
	}

	private void buildBundleIndex(List<Statement> sbs) {
		for (Iterator<Statement> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			buildTypes(sb);
		}
	}

	private void buildTypes(StatementOrBundle sb) {
		if (sb != null) {
			if (sb instanceof Statement) {
				if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
					Entity et = (Entity) sb;
					String id = id(et.getId());

					String extraParams = getStringParams(et);
					if (et.getLabel() != null && !et.getLabel().isEmpty()) {
						labels.put(id, et.getLabel().get(0).getValue() + "," + extraParams);
					} else {
						if (extraParams.length() > 0) {
							labels.put(id, "," + extraParams);
						}
					}
					if (et.getType() != null && !et.getType().isEmpty()) {
						if (et.getType().get(0).getType() instanceof QualifiedName
								&& !et.getType().get(0).getType().toString().contains("string")) {
							entityTypes.put(id, id((QualifiedName) et.getType().get(0).getValue()));
						} else {
							entityTypes.put(id, (String) et.getType().get(0).getValue());
						}
					}
					entities.add(id);
				} else if (sb != null && sb.getKind() == Kind.PROV_ACTIVITY) {
					Activity ac = (Activity) sb;
					String id = id(ac.getId());

					String extraParams = getStringParams(ac);
					if (ac.getLabel() != null && !ac.getLabel().isEmpty()) {
						labels.put(id, ac.getLabel().get(0).getValue() + "," + extraParams);
					} else {
						if (extraParams.length() > 0) {
							labels.put(id, "," + extraParams);
						}
					}
					if (ac.getType() != null && !ac.getType().isEmpty()) {
						if (ac.getType().get(0).getType() instanceof QualifiedName
								&& !ac.getType().get(0).getType().toString().contains("string")) {
							activityTypes.put(id, id((QualifiedName) ac.getType().get(0).getValue()));
						} else {
							activityTypes.put(id, (String) ac.getType().get(0).getValue());
						}
					}
					activities.add(id);
				} else if (sb != null && sb.getKind() == Kind.PROV_AGENT) {
					Agent ag = (Agent) sb;
					String id = id(ag.getId());

					String extraParams = getStringParams(ag);
					if (ag.getLabel() != null && !ag.getLabel().isEmpty()) {
						labels.put(id, ag.getLabel().get(0).getValue() + "," + extraParams);
					} else {
						if (extraParams.length() > 0) {
							labels.put(id, "," + extraParams);
						}
					}
					if (ag.getType() != null && !ag.getType().isEmpty()) {
						if (ag.getType().get(0).getType() instanceof QualifiedName
								&& !ag.getType().get(0).getType().toString().contains("string")) {
							agentTypes.put(id, id((QualifiedName) ag.getType().get(0).getValue()));
						} else {
							agentTypes.put(id, (String) ag.getType().get(0).getValue());
						}
					}
					agents.add(id(ag.getId()));
				}
			}
		}
	}

	private String getStringParams(StatementOrBundle sb) {
		String others = "";
		if (sb instanceof Statement) {
			if (sb != null && sb.getKind() == Kind.PROV_ENTITY) {
				Entity concept = (Entity) sb;
				if (concept.getOther().size() > 0) {
					List<Other> params = concept.getOther();
					for (Other other : params) {
						others += other.getElementName().getLocalPart() + " = \"" + other.getValue() + "\"; ";
					}
				}
			} else if (sb != null && sb.getKind() == Kind.PROV_AGENT) {
				Agent concept = (Agent) sb;
				if (concept.getOther().size() > 0) {
					List<Other> params = concept.getOther();
					for (Other other : params) {
						others += other.getElementName().getLocalPart() + " = \"" + other.getValue() + "\"; ";
					}
				}
			} else if (sb != null && sb.getKind() == Kind.PROV_ACTIVITY) {
				Activity concept = (Activity) sb;
				if (concept.getOther().size() > 0) {
					List<Other> params = concept.getOther();
					for (Other other : params) {
						others += other.getElementName().getLocalPart() + " = \"" + other.getValue() + "\"; ";
					}
				}
			}
		}
		return others;
	}

	public List<ProvMatrix> buildMatrices(boolean deriveInfluence) {
		List<ProvMatrix> matrices = new ArrayList<>();
		for (int i = 0; i < documents.length; i++) {
			List<StatementOrBundle> sbs;
			buildTypeLists(i);
			agentsList = new ArrayList<>(agents);
			activitiesList = new ArrayList<>(activities);
			entitiesList = new ArrayList<>(entities);
			setupStatements(agentsList, activitiesList, entitiesList);
			sbs = documents[i].getStatementOrBundle();
			for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
				StatementOrBundle sb = iterator.next();
				if (sb != null) {
					if (sb instanceof Statement) {
						processStatement(sb, deriveInfluence);
					} else {
						Bundle bundle = (Bundle) sb;
						buildBundleIndex(bundle.getStatement());
					}
				}
			}
		}
		addStatements(matrices);
		return matrices;
	}

	private void addStatements(List<ProvMatrix> matrices) {
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
		matrices.add(this.entityInstanceOf);
		matrices.add(this.activityInstanceOf);
		matrices.add(this.agentInstanceOf);
	}

	private void setupStatements(List<String> agentsList, List<String> activitiesList, List<String> entitiesList) {
		if (this.wasInformedBy == null) {
			this.wasInformedBy = new ActivityActivity(activitiesList);
			this.getWasInformedBy().setRelation(Relation.RELATION_COMMUNICATION);
		} else {
			this.wasInformedBy.add(activitiesList);
		}
		if (this.wasStartedBy == null) {
			this.wasStartedBy = new ActivityActivity(activitiesList);
			this.wasStartedBy.setRelation(Relation.RELATION_START);
		} else {
			this.wasStartedBy.add(activitiesList);
		}
		if (this.wasEndedBy == null) {
			this.wasEndedBy = new ActivityActivity(activitiesList);
			this.wasEndedBy.setRelation(Relation.RELATION_END);
		} else {
			this.wasEndedBy.add(activitiesList);
		}
		if (this.wasInfluencedByAcAc == null) {
			this.wasInfluencedByAcAc = new ActivityActivity(activitiesList);
			this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByAcAc.add(activitiesList);
		}

		if (this.wasAssociatedWith == null) {
			this.wasAssociatedWith = new ActivityAgent(activitiesList, agentsList);
			this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
		} else {
			this.wasAssociatedWith.add(activitiesList, agentsList);
		}
		if (this.wasInfluencedByAcAg == null) {
			this.wasInfluencedByAcAg = new ActivityAgent(activitiesList, agentsList);
			this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByAcAg.add(activitiesList, agentsList);
		}

		if (this.used == null) {
			this.used = new ActivityEntity(activitiesList, entitiesList);
			this.used.setRelation(Relation.RELATION_USAGE);
		} else {
			this.used.add(activitiesList, entitiesList);
		}

		if (this.wasInfluencedByAcE == null) {
			this.wasInfluencedByAcE = new ActivityEntity(activitiesList, entitiesList);
			this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByAcE.add(activitiesList, entitiesList);
		}

		if (this.wasGeneratedBy == null) {
			this.wasGeneratedBy = new EntityActivity(entitiesList, activitiesList);
			this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
		} else {
			this.wasGeneratedBy.add(entitiesList, activitiesList);
		}
		if (this.wasInfluencedByEAc == null) {
			this.wasInfluencedByEAc = new EntityActivity(entitiesList, activitiesList);
			this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByEAc.add(entitiesList, activitiesList);
		}
		if (this.wasInvalidatedBy == null) {
			this.wasInvalidatedBy = new EntityActivity(entitiesList, activitiesList);
			this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
		} else {
			this.wasInvalidatedBy.add(entitiesList, activitiesList);
		}

		if (this.wasAttributedTo == null) {
			this.wasAttributedTo = new EntityAgent(entitiesList, agentsList);
			this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
		} else {
			this.wasAttributedTo.add(entitiesList, agentsList);
		}
		if (this.wasInfluencedByEAg == null) {
			this.wasInfluencedByEAg = new EntityAgent(entitiesList, agentsList);
			this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByEAg.add(entitiesList, agentsList);
		}

		if (this.wasDerivedFrom == null) {
			this.wasDerivedFrom = new EntityEntity(entitiesList);
			this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
		} else {
			this.wasDerivedFrom.add(entitiesList);
		}
		if (this.wasInfluencedByEE == null) {
			this.wasInfluencedByEE = new EntityEntity(entitiesList);
			this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByEE.add(entitiesList);
		}
		if (this.specializationOf == null) {
			this.specializationOf = new EntityEntity(entitiesList);
			this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
		} else {
			this.specializationOf.add(entitiesList);
		}
		if (this.alternateOf == null) {
			this.alternateOf = new EntityEntity(entitiesList);
			this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
		} else {
			this.alternateOf.add(entitiesList);
		}
		if (this.mentionOf == null) {
			this.mentionOf = new EntityEntity(entitiesList);
			this.mentionOf.setRelation(Relation.RELATION_MENTION);
		} else {
			this.mentionOf.add(entitiesList);
		}
		if (this.hadMember == null) {
			this.hadMember = new EntityEntity(entitiesList);
			this.hadMember.setRelation(Relation.RELATION_MEMBERSHIP);
		} else {
			this.hadMember.add(entitiesList);
		}

		if (this.actedOnBehalfOf == null) {
			this.actedOnBehalfOf = new AgentAgent(agentsList);
			this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
		} else {
			this.actedOnBehalfOf.add(agentsList);
		}
		if (this.wasInfluencedByAgAg == null) {
			this.wasInfluencedByAgAg = new AgentAgent(agentsList);
			this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
		} else {
			this.wasInfluencedByAgAg.add(agentsList);
		}
	}

	private void buildTypeLists(int i) {
		List<StatementOrBundle> sbs = documents[i].getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb != null) {
				if (sb instanceof Statement) {
					buildTypes(sb);
				} else {
					Bundle bundle = (Bundle) sb;
					buildBundleIndex(bundle.getStatement());
				}
			}
		}
		buildSubtypes();
	}

	public void buildAgentTypes() {
		if (agentTypes != null && !agentTypes.isEmpty()) {
			this.agentInstanceOf = new AgentInstance(new ArrayList<String>(this.agents), new HashSet<String>());
			for (String key : agents) {
				String value = agentTypes.get(key);
				if (value != null && value.contains("::") && !value.endsWith("::")) {
					String[] values = value.split("::");
					for (int i = 0; i < values.length; i++) {
						if (!agentInstanceOf.getDestinationTypeAgentsId().contains(values[i])) {
							agentInstanceOf.getDestinationTypeAgentsId().add(values[i]);
						}
						this.agentInstanceOf.add(key, values[i]);
					}
				} else {
					if (!agentInstanceOf.getDestinationTypeAgentsId().contains(value)) {
						agentInstanceOf.getDestinationTypeAgentsId().add(value);
					}
					this.agentInstanceOf.add(key, value);
				}
			}
		}
	}

	public void buildActivityTypes() {
		if (activityTypes != null && !activityTypes.isEmpty()) {
			this.activityInstanceOf = new ActivityInstance(new ArrayList<String>(this.activities),
					new HashSet<String>());
			for (String key : activities) {
				String value = activityTypes.get(key);
				if (value != null && value.contains("::") && !value.endsWith("::")) {
					String[] values = value.split("::");
					for (int i = 0; i < values.length; i++) {
						if (!activityInstanceOf.getDestinationTypeActivitiesId().contains(values[i])) {
							activityInstanceOf.getDestinationTypeActivitiesId().add(values[i]);
						}
						this.activityInstanceOf.add(key, values[i]);
					}
				} else {
					if (!activityInstanceOf.getDestinationTypeActivitiesId().contains(value)) {
						activityInstanceOf.getDestinationTypeActivitiesId().add(value);
					}
					this.activityInstanceOf.add(key, value);
				}
			}
		}
	}

	public void buildEntityTypes() {
		if (entityTypes != null && !entityTypes.isEmpty()) {
			this.entityInstanceOf = new EntityInstance(new ArrayList<String>(this.entities), new HashSet<String>());
			for (String key : entities) {
				String value = entityTypes.get(key);
				if (value != null && value.contains("::") && !value.endsWith("::")) {
					String[] values = value.split("::");
					for (int i = 0; i < values.length; i++) {
						if (!entityInstanceOf.getDestinationTypeEntitiesId().contains(values[i])) {
							entityInstanceOf.getDestinationTypeEntitiesId().add(values[i]);
						}
						this.entityInstanceOf.add(key, values[i]);
					}
				} else {
					if (!entityInstanceOf.getDestinationTypeEntitiesId().contains(value)) {
						entityInstanceOf.getDestinationTypeEntitiesId().add(value);
					}
					this.entityInstanceOf.add(key, value);
				}
			}
		}
	}

	private void buildSubtypes() {
		buildAgentTypes();
		buildActivityTypes();
		buildEntityTypes();
	}

	private void processStatement(StatementOrBundle sb, boolean deriveInfluence) {
		if (sb != null) {
			switch (sb.getKind()) {
			case PROV_COMMUNICATION: {
				WasInformedBy wi = (WasInformedBy) sb;
				if (wi.getInformant() != null && wi.getInformed() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAc.add(id(wi.getInformed()), id(wi.getInformant()));
					}
					this.wasInformedBy.setRelation(Relation.RELATION_COMMUNICATION);
					if (this.activities.contains(id(wi.getInformed()))
							&& this.activities.contains(id(wi.getInformant()))) {
						this.wasInformedBy.add(id(wi.getInformed()), id(wi.getInformant()));
						String extraParams = "";
						for (Other param : wi.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("WFB(" + activitiesList.indexOf(id(wi.getInformed())) + ","
								+ activitiesList.indexOf(id(wi.getInformant())) + ")", extraParams);
					} else {
						System.out.println("Did not find act:" + wi.getInformed() + ", act: " + id(wi.getInformant()));
						throw new RuntimeException(
								"Did not find act:" + wi.getInformed() + ", act: " + id(wi.getInformant()));
					}
				}
				break;
			}
			case PROV_START: {
				WasStartedBy ws = (WasStartedBy) sb;
				if (ws.getStarter() != null && ws.getTrigger() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAc.add(id(ws.getStarter()), id(ws.getTrigger()));
					}
					this.wasStartedBy.setRelation(Relation.RELATION_START);
					this.wasStartedBy.add(id(ws.getStarter()), id(ws.getTrigger()));
				}
				break;
			}
			case PROV_END: {
				WasEndedBy we = (WasEndedBy) sb;
				if (we.getEnder() != null && we.getTrigger() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAc.add(id(we.getEnder()), id(we.getTrigger()));
					}
					this.wasEndedBy.setRelation(Relation.RELATION_START);
					this.wasEndedBy.add(id(we.getEnder()), id(we.getTrigger()));
				}
				break;
			}
			case PROV_ASSOCIATION: {
				WasAssociatedWith waw = (WasAssociatedWith) sb;
				if (waw.getActivity() != null && waw.getAgent() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAg.add(id(waw.getActivity()), id(waw.getAgent()));
					}
					this.wasAssociatedWith.setRelation(Relation.RELATION_ASSOCIATION);
					if (this.activities.contains(id(waw.getActivity())) && this.agents.contains(id(waw.getAgent()))) {
						this.wasAssociatedWith.add(id(waw.getActivity()), id(waw.getAgent()));
						String extraParams = "";
						for (Other param : waw.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("WAW(" + activitiesList.indexOf(id(waw.getActivity())) + ","
								+ agentsList.indexOf(id(waw.getAgent())) + ")", extraParams);
					} else {
						System.out
								.println("Did not find act:" + id(waw.getActivity()) + ", agt: " + id(waw.getAgent()));
						throw new RuntimeException(
								"Did not find act:" + id(waw.getActivity()) + ", agt: " + id(waw.getAgent()));
					}
				}
				break;
			}
			case PROV_ATTRIBUTION: {
				WasAttributedTo wat = (WasAttributedTo) sb;
				if (wat.getEntity() != null && wat.getAgent() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAg.add(id(wat.getEntity()), id(wat.getAgent()));
					}
					this.wasAttributedTo.setRelation(Relation.RELATION_ATTRIBUTION);
					if (this.entities.contains(id(wat.getEntity())) && this.agents.contains(id(wat.getAgent()))) {
						this.wasAttributedTo.add(id(wat.getEntity()), id(wat.getAgent()));
						String extraParams = "";
						for (Other param : wat.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("WAT(" + entitiesList.indexOf(id(wat.getEntity())) + ","
								+ agentsList.indexOf(id(wat.getAgent())) + ")", extraParams);
					} else {
						System.out.println("Did not find ent:" + id(wat.getEntity()) + ", agt: " + id(wat.getAgent()));
						throw new RuntimeException(
								"Did not find ent:" + id(wat.getEntity()) + ", agt: " + id(wat.getAgent()));
					}

				}
				break;
			}
			case PROV_USAGE: {
				Used usd = (Used) sb;
				if (usd.getActivity() != null && usd.getEntity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcE.add(id(usd.getActivity()), id(usd.getEntity()));
					}
					this.used.setRelation(Relation.RELATION_USAGE);
					if (this.activities.contains(id(usd.getActivity()))
							&& this.entities.contains(id(usd.getEntity()))) {
						this.used.add(id(usd.getActivity()), id(usd.getEntity()));
						String extraParams = "";
						for (Other param : usd.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("USD(" + activitiesList.indexOf(id(usd.getActivity())) + ","
								+ entitiesList.indexOf(id(usd.getEntity())) + ")", extraParams);
					} else {
						System.out
								.println("Did not find act:" + id(usd.getActivity()) + ", ent: " + id(usd.getEntity()));
						throw new RuntimeException(
								"Did not find act:" + id(usd.getActivity()) + ", ent: " + id(usd.getEntity()));
					}
				}
				break;
			}
			case PROV_INVALIDATION: {
				WasInvalidatedBy wvb = (WasInvalidatedBy) sb;
				if (wvb.getEntity() != null && wvb.getActivity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAc.add(id(wvb.getEntity()), id(wvb.getActivity()));
					}
					this.wasInvalidatedBy.setRelation(Relation.RELATION_INVALIDATION);
					this.wasInvalidatedBy.add(id(wvb.getEntity()), id(wvb.getActivity()));
				}
				break;
			}
			case PROV_GENERATION: {
				WasGeneratedBy wgb = (WasGeneratedBy) sb;
				if (wgb.getEntity() != null && wgb.getActivity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAc.add(id(wgb.getEntity()), id(wgb.getActivity()));
					}
					this.wasGeneratedBy.setRelation(Relation.RELATION_GENERATION);
					if (this.entities.contains(id(wgb.getEntity()))
							&& this.activities.contains(id(wgb.getActivity()))) {
						this.wasGeneratedBy.add(id(wgb.getEntity()), id(wgb.getActivity()));
						String extraParams = "";
						for (Other param : wgb.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("WGB(" + entitiesList.indexOf(id(wgb.getEntity())) + ","
								+ activitiesList.indexOf(id(wgb.getActivity())) + ")", extraParams);
					} else {
						System.out
								.println("Did not find ent:" + id(wgb.getEntity()) + ", act: " + id(wgb.getActivity()));
						throw new RuntimeException(
								"Did not find ent:" + id(wgb.getEntity()) + ", act: " + id(wgb.getActivity()));
					}
				}
				break;
			}
			case PROV_DERIVATION: {
				WasDerivedFrom wdf = (WasDerivedFrom) sb;
				if (wdf.getGeneratedEntity() != null && wdf.getUsedEntity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(wdf.getGeneratedEntity()), id(wdf.getUsedEntity()));
					}
					this.wasDerivedFrom.setRelation(Relation.RELATION_DERIVATION);
					this.wasDerivedFrom.add(id(wdf.getGeneratedEntity()), id(wdf.getUsedEntity()));
					if (this.entities.contains(id(wdf.getGeneratedEntity()))
							&& this.entities.contains(id(wdf.getUsedEntity()))) {
						this.wasDerivedFrom.add(id(wdf.getGeneratedEntity()), id(wdf.getUsedEntity()));
						String extraParams = "";
						for (Other param : wdf.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("WDF(" + entitiesList.indexOf(id(wdf.getGeneratedEntity())) + ","
								+ entitiesList.indexOf(id(wdf.getUsedEntity())) + ")", extraParams);
					} else {
						System.out.println("Did not find ent:" + id(wdf.getGeneratedEntity()) + ", ent: "
								+ id(wdf.getUsedEntity()));
						throw new RuntimeException("Did not find ent:" + id(wdf.getGeneratedEntity()) + ", ent: "
								+ id(wdf.getUsedEntity()));
					}
				}
				break;
			}
			case PROV_SPECIALIZATION: {
				SpecializationOf so = (SpecializationOf) sb;
				if (so.getSpecificEntity() != null && so.getGeneralEntity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(so.getSpecificEntity()), id(so.getGeneralEntity()));
					}
					this.specializationOf.setRelation(Relation.RELATION_SPECIALIZATION);
					this.specializationOf.add(id(so.getSpecificEntity()), id(so.getGeneralEntity()));
				}
				break;
			}
			case PROV_ALTERNATE: {
				AlternateOf ao = (AlternateOf) sb;
				if (ao.getAlternate1() != null && ao.getAlternate2() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(ao.getAlternate1()), id(ao.getAlternate2()));
					}
					this.alternateOf.setRelation(Relation.RELATION_ALTERNATE);
					this.alternateOf.add(id(ao.getAlternate1()), id(ao.getAlternate2()));
				}
				break;
			}
			case PROV_MENTION: {
				MentionOf mo = (MentionOf) sb;
				if (mo.getSpecificEntity() != null && mo.getGeneralEntity() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(mo.getSpecificEntity()), id(mo.getGeneralEntity()));
					}
					this.mentionOf.setRelation(Relation.RELATION_MENTION);
					this.mentionOf.add(id(mo.getSpecificEntity()), id(mo.getGeneralEntity()));
				}
				break;
			}
			case PROV_MEMBERSHIP: {
				HadMember hm = (HadMember) sb;
				if (hm.getEntity() != null) {
					this.hadMember.setRelation(Relation.RELATION_MEMBERSHIP);
					List<QualifiedName> members = hm.getEntity();
					for (QualifiedName member : members) {
						if (member != null) {
							this.hadMember.add(id(hm.getCollection()), id(member));
						}
					}
				}
				break;
			}
			case PROV_DELEGATION: {
				ActedOnBehalfOf aob = (ActedOnBehalfOf) sb;
				if (aob.getDelegate() != null && aob.getResponsible() != null) {
					if (deriveInfluence) {
						this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAgAg.add(id(aob.getDelegate()), id(aob.getResponsible()));
					}
					this.actedOnBehalfOf.setRelation(Relation.RELATION_DELEGATION);
					if (this.agents.contains(id(aob.getDelegate())) && this.agents.contains(id(aob.getResponsible()))) {
						this.actedOnBehalfOf.add(id(aob.getDelegate()), id(aob.getResponsible()));
						String extraParams = "";
						for (Other param : aob.getOther()) {
							extraParams += param.getElementName().getLocalPart() + " = " + param.getValue() + ";";
						}
						this.cellParams.put("AOB(" + agentsList.indexOf(id(aob.getDelegate())) + ","
								+ agentsList.indexOf(id(aob.getResponsible())) + ")", extraParams);
					} else {
						System.out.println(
								"Did not find agt:" + id(aob.getDelegate()) + ", agt: " + id(aob.getResponsible()));
						throw new RuntimeException(
								"Did not find agt:" + id(aob.getDelegate()) + ", agt: " + id(aob.getResponsible()));
					}
				}
				break;
			}
			case PROV_INFLUENCE: {
				WasInfluencedBy wib = (WasInfluencedBy) sb;
				if (wib.getInfluencee() != null && wib.getInfluencer() != null) {
					if (entities.contains(id(wib.getInfluencee())) && entities.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByEE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEE.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (agents.contains(id(wib.getInfluencee())) && agents.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByAgAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAgAg.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (activities.contains(id(wib.getInfluencee()))
							&& activities.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByAcAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAc.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (activities.contains(id(wib.getInfluencee()))
							&& entities.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByAcE.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcE.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (activities.contains(id(wib.getInfluencee()))
							&& agents.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByAcAg.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByAcAg.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (entities.contains(id(wib.getInfluencee()))
							&& activities.contains(id(wib.getInfluencer()))) {
						this.wasInfluencedByEAc.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAc.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					} else if (entities.contains(id(wib.getInfluencee())) && agents.contains(id(wib.getInfluencer()))) {
						this.wasAttributedTo.setRelation(Relation.RELATION_INFLUENCE);
						this.wasInfluencedByEAg.add(id(wib.getInfluencee()), id(wib.getInfluencer()));
					}
				}
				break;
			}
			default:
				break;
			}
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

	@Override
	public Map<String, String> getDimensionLabels() {
		return this.labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public Map<String, String> getEntityTypes() {
		return entityTypes;
	}

	public void setEntityTypes(Map<String, String> entityTypes) {
		this.entityTypes = entityTypes;
	}

	public Map<String, String> getActivityTypes() {
		return activityTypes;
	}

	public void setActivityTypes(Map<String, String> activityTypes) {
		this.activityTypes = activityTypes;
	}

	public Map<String, String> getAgentTypes() {
		return agentTypes;
	}

	public void setAgentTypes(Map<String, String> agentTypes) {
		this.agentTypes = agentTypes;
	}

	public Set<String> getAgents() {
		return agents;
	}

	public void setAgents(Set<String> agents) {
		this.agents = agents;
	}

	public Set<String> getEntities() {
		return entities;
	}

	public void setEntities(Set<String> entities) {
		this.entities = entities;
	}

	public Set<String> getActivities() {
		return activities;
	}

	public void setActivities(Set<String> activities) {
		this.activities = activities;
	}

	public AgentInstance getAgentInstanceOf() {
		return agentInstanceOf;
	}

	public void setAgentInstanceOf(AgentInstance agentInstanceOf) {
		this.agentInstanceOf = agentInstanceOf;
	}

	public ActivityInstance getActivityInstanceOf() {
		return activityInstanceOf;
	}

	public void setActivityInstanceOf(ActivityInstance activityInstanceOf) {
		this.activityInstanceOf = activityInstanceOf;
	}

	public EntityInstance getEntityInstanceOf() {
		return entityInstanceOf;
	}

	public void setEntityInstanceOf(EntityInstance entityInstanceOf) {
		this.entityInstanceOf = entityInstanceOf;
	}

	public HashMap<String, String> getCellParams() {
		return cellParams;
	}

	public void setCellParams(HashMap<String, String> cellParams) {
		this.cellParams = cellParams;
	}

}
