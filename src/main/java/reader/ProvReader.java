package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Victor
 */
public class ProvReader {

	private File provFile;
	private List<ProvElement> entities;
	private List<ProvElement> agents;
	private List<ProvActivity> activities;
	private List<ProvStatement> statements;

	public ProvReader() {
		super();
		this.entities = new ArrayList<>();
		this.agents = new ArrayList<>();
		this.activities = new ArrayList<>();
		this.statements = new ArrayList<>();
	}

	public ProvReader(String f) throws URISyntaxException, IOException {
		this();
		this.provFile = new File(f);
	}

	public void readFile() throws URISyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(provFile));
		try {
			String line = br.readLine();

			while (line != null) {
				processLine(line);
				line = br.readLine();
			}
		} finally {
			br.close();
		}

	}

	public void processLine(String line) {
		String[] elements;
		String[] statement;
		String[] attributes;
		String[] optionalAttributes = null;

		line = line.replace(")", "");
		line = line.replace(" ", "");
		line = line.replace("\t", "");
		line = line.replace("]", "");
		line = line.replace("'", "");
		line = line.replace("\"", "");
		elements = line.split("\\(");
		if (elements.length > 1) {
			statement = elements[1].split("\\[");
			attributes = statement[0].split(",");

			if (statement.length > 1) {
				optionalAttributes = statement[1].split(",");
			}

			if (elements[0].contains("entity")) {
				readEntity(attributes, optionalAttributes);
			}
			if (elements[0].contains("activity")) {
				readActivity(attributes, optionalAttributes);
			}
			if (elements[0].contains("agent")) {
				readAgent(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasGeneratedBy")) {
				readGeneration(attributes, optionalAttributes);
			}
			if (elements[0].contains("used")) {
				readUsage(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasInformedBy")) {
				readCommunication(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasStartedBy")) {
				readStart(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasEndedBy")) {
				readEnd(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasInvalidatedBy")) {
				readInvalidation(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasDerivedFrom")) {
				readDerivation(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasAttributedTo")) {
				readAttribution(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasAssociatedWith")) {
				readAssociation(attributes, optionalAttributes);
			}
			if (elements[0].contains("actedOnBehalfOf")) {
				readDelegation(attributes, optionalAttributes);
			}
			if (elements[0].contains("wasInfluencedBy")) {
				readInfluence(attributes, optionalAttributes);
			}
			if (elements[0].contains("hadMember")) {
				readMembership(attributes, optionalAttributes);
			}
			if (elements[0].contains("specializationOf")) {
				readSpecialization(attributes, optionalAttributes);
			}
			if (elements[0].contains("alternateOf")) {
				readAlternate(attributes, optionalAttributes);
			}
		}
	}

	private ProvElement getAgent(String agent) {
		for (ProvElement a : this.agents) {
			if (a != null && a.getName().equals(agent)) {
				return a;
			}
		}
		return null;
	}

	private ProvElement getEntity(String entity) {
		for (ProvElement e : this.entities) {
			if (e != null && e.getName().equals(entity)) {
				return e;
			}
		}
		return null;
	}

	private ProvStatement getStatement(String statementId) {
		for (ProvStatement statement : this.statements) {
			if (statement != null && statement.getId() != null && statement.getId().equals(statementId)) {
				return statement;
			}
		}
		return null;
	}

	private ProvActivity getActivity(String activity) {
		for (ProvActivity a : this.activities) {
			if (a != null && a.getName().equals(activity)) {
				return a;
			}
		}
		return null;
	}

	public void readEntity(String[] attributes, String[] optionalAttributes) {
		String id = attributes[0];
		this.entities.add(new ProvElement(id, optionalAttributes));
	}

	public void readActivity(String[] attributes, String[] optionalAttributes) {
		String id = attributes[0];
		ProvActivity provActivity = new ProvActivity(id, optionalAttributes);
		if (attributes.length > 1) {
			String startTime = attributes[1];
			provActivity.setStartTime(startTime);
		}
		if (attributes.length > 2) {
			String endTime = attributes[2];
			provActivity.setEndTime(endTime);
		}
		this.activities.add(provActivity);
	}

	public void readAgent(String[] attributes, String[] optionalAttributes) {
		String id = attributes[0];
		this.agents.add(new ProvElement(id, optionalAttributes));
	}

	public void readGeneration(String[] attributes, String[] optionalAttributes) {
		String entity = "-";
		String activity = "-";
		String time;
		String id = getOptionalID(attributes[0]);
		if (attributes.length >= 3) {
			entity = get1stAttribute(attributes[0]);
			activity = attributes[1];
			time = attributes[2];
		} else {
			entity = get1stAttribute(attributes[0]);
			time = "";
		}
		ProvTimedStatement wasGeneratedBy = new ProvTimedStatement("wasGeneratedBy", getEntity(entity),
				getActivity(activity), optionalAttributes);
		wasGeneratedBy.setTime(time);
		wasGeneratedBy.setId(id);
		this.statements.add(wasGeneratedBy);
	}

	public void readUsage(String[] attributes, String[] optionalAttributes) {
		String entity = "-";
		String activity = "-";
		String time;
		String id = getOptionalID(attributes[0]);
		if (attributes.length >= 3) {
			activity = get1stAttribute(attributes[0]);
			entity = attributes[1];
			time = attributes[2];
		} else {
			activity = get1stAttribute(attributes[0]);
			time = "";
		}
		ProvTimedStatement used = new ProvTimedStatement("used", getActivity(activity), getEntity(entity),
				optionalAttributes);
		used.setTime(time);
		used.setId(id);
		this.statements.add(used);
	}

	public void readCommunication(String[] attributes, String[] optionalAttributes) {
		String informed = "-";
		String informant = "-";
		String id = getOptionalID(attributes[0]);
		informed = get1stAttribute(attributes[0]);
		informant = attributes[1];

		ProvStatement wasInformedBy = new ProvStatement("wasInformedBy", getActivity(informed), getActivity(informant),
				optionalAttributes);
		wasInformedBy.setId(id);
		this.statements.add(wasInformedBy);
	}

	public void readStart(String[] attributes, String[] optionalAttributes) {
		starterOrEnder(attributes, optionalAttributes, "wasStartedBy");
	}

	public void readEnd(String[] attributes, String[] optionalAttributes) {
		starterOrEnder(attributes, optionalAttributes, "wasEndedBy");
	}

	public void starterOrEnder(String[] attributes, String[] optionalAttributes, String type) {
		String activity = "-";
		String trigger = "-";
		String starterOrEnder = "-";
		String time;
		String id = getOptionalID(attributes[0]);
		if (attributes.length >= 4) {
			activity = get1stAttribute(attributes[0]);
			trigger = attributes[1];
			starterOrEnder = attributes[2];
			time = attributes[3];
		} else {
			activity = get1stAttribute(attributes[0]);
			time = "";
		}

		ProvTriggeredStament provTrigger = new ProvTriggeredStament(type, getActivity(activity), getActivity(trigger),
				optionalAttributes);
		provTrigger.setStarterOrEnder(starterOrEnder);
		provTrigger.setTime(time);
		provTrigger.setId(id);
		this.statements.add(provTrigger);
	}

	public void readInvalidation(String[] attributes, String[] optionalAttributes) {
		String entity = "-";
		String activity = "-";
		String time;
		String id = getOptionalID(attributes[0]);
		if (attributes.length >= 3) {
			entity = get1stAttribute(attributes[0]);
			activity = attributes[1];
			time = attributes[2];
		} else {
			entity = get1stAttribute(attributes[0]);
			time = "";
		}
		ProvTimedStatement wasInvalidatedBy = new ProvTimedStatement("wasInvalidatedBy", getEntity(entity),
				getActivity(activity), optionalAttributes);
		wasInvalidatedBy.setId(id);
		wasInvalidatedBy.setTime(time);
		this.statements.add(wasInvalidatedBy);
	}

	public void readDerivation(String[] attributes, String[] optionalAttributes) {
		String generatedEntity = null;
		String usedEntity;
		String activity = "-";
		String generation = "-";
		String usage = "-";
		String id = getOptionalID(attributes[0]);
		ProvDerivationStatement provDerivation = null;
		generatedEntity = get1stAttribute(attributes[0]);
		usedEntity = attributes[1];
		provDerivation = new ProvDerivationStatement("wasDerivedBy", getEntity(generatedEntity), getEntity(usedEntity),
				optionalAttributes);
		if (attributes.length >= 5) {
			activity = attributes[2];
			generation = attributes[3];
			usage = attributes[4];
			provDerivation.setActivity(getActivity(activity));
			provDerivation.setUsage(getStatement(usage));
			provDerivation.setGeneration((ProvTimedStatement) getStatement(generation));
		}
		provDerivation.setId(id);
		this.statements.add(provDerivation);
	}

	public void readAttribution(String[] attributes, String[] optionalAttributes) {
		String id = null;
		String entity = "-";
		String agent;

		id = getOptionalID(attributes[0]);
		entity = get1stAttribute(attributes[0]);
		agent = attributes[1];

		ProvStatement provAttribution = new ProvStatement("wasAttributedTo", getEntity(entity), getAgent(agent),
				optionalAttributes);
		provAttribution.setId(id);
		this.statements.add(provAttribution);
	}

	public void readAssociation(String[] attributes, String[] optionalAttributes) {
		String id = null;
		String activity = "-";
		String agent;

		id = getOptionalID(attributes[0]);
		activity = get1stAttribute(attributes[0]);
		agent = attributes[1];

		ProvStatement wasAssociatedWith = new ProvStatement("wasAssociatedWith", getAgent(agent), getActivity(activity),
				optionalAttributes);
		wasAssociatedWith.setId(id);
		this.statements.add(wasAssociatedWith);
	}

	public void readDelegation(String[] attributes, String[] optionalAttributes) {
		String id = null;
		String delegate = "-";
		String responsible;
		String activity = "-";

		id = getOptionalID(attributes[0]);
		delegate = get1stAttribute(attributes[0]);
		responsible = attributes[1];

		if (attributes.length >= 3) {
			activity = attributes[2];
		}
		ProvDelegationStatement actedOnBehalfOf = new ProvDelegationStatement("actedOnBehalfOf", getAgent(delegate),
				getAgent(responsible), optionalAttributes);
		if (activity != null && !activity.matches("-")) {
			actedOnBehalfOf.setActivity(getActivity(activity));
		}
		actedOnBehalfOf.setId(id);
		this.statements.add(actedOnBehalfOf);
	}

	public void readInfluence(String[] attributes, String[] optionalAttributes) {
		String id = null;
		String influencer = "-";
		String influencee = "-";

		id = getOptionalID(attributes[0]);
		influencer = get1stAttribute(attributes[0]);
		influencee = attributes[1];

		ProvStatement wasInfluencedBy = new ProvStatement("wasInfluencedBy", getEntity(influencer),
				getEntity(influencee), optionalAttributes);
		wasInfluencedBy.setId(id);
		this.statements.add(wasInfluencedBy);
	}

	public void readAlternate(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembership(attributes, optionalAttributes, "alternateOf");
	}

	public void readSpecialization(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembership(attributes, optionalAttributes, "specializationOf");
	}

	public void readMembership(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembership(attributes, optionalAttributes, "hadMember");
	}

	public void alternateOrSpecializationOrMembership(String[] attributes, String[] optionalAttributes, String type) {
		String id = null;
		String alternateSpecMember1 = "-";
		String alternateSpecMember2;

		id = getOptionalID(attributes[0]);
		alternateSpecMember1 = get1stAttribute(attributes[0]);
		alternateSpecMember2 = attributes[1];

		ProvStatement alternateOrSpecializationOrMemberOf = new ProvStatement(type, getEntity(alternateSpecMember2),
				getEntity(alternateSpecMember1), optionalAttributes);
		alternateOrSpecializationOrMemberOf.setId(id);
		this.statements.add(alternateOrSpecializationOrMemberOf);
	}

	public String getOptionalID(String attribute) {
		String id = null;
		String[] att = attribute.split(";");
		if (att.length >= 2) {
			id = att[0];
		}
		return id;
	}

	public String get1stAttribute(String attribute) {
		String id = null;
		String[] att = attribute.split(";");
		if (att.length == 2) {
			id = att[1];
		} else {
			id = att[0];
		}
		return id;
	}

	public File getProvFile() {
		return provFile;
	}

	public void setProvFile(File provFile) {
		this.provFile = provFile;
	}

	public List<ProvElement> getEntities() {
		return entities;
	}

	public void setEntities(List<ProvElement> entities) {
		this.entities = entities;
	}

	public List<ProvElement> getAgents() {
		return agents;
	}

	public void setAgents(List<ProvElement> agents) {
		this.agents = agents;
	}

	public List<ProvActivity> getActivities() {
		return activities;
	}

	public void setActivities(List<ProvActivity> activities) {
		this.activities = activities;
	}

	public List<ProvStatement> getStatements() {
		return statements;
	}

	public void setStatements(List<ProvStatement> statements) {
		this.statements = statements;
	}

	public static void main(String[] args) throws URISyntaxException, IOException {
		ProvReader provReader = new ProvReader("etc/project-management2.provn");
		provReader.readFile();
		for (ProvElement e : provReader.getEntities()) {
			System.out.println("entity(" + e);
		}
		for (ProvElement a : provReader.getAgents()) {
			System.out.println("agent(" + a);
		}
		for (ProvActivity a : provReader.getActivities()) {
			System.out.println("activity(" + a);
		}
		for (ProvStatement s : provReader.getStatements()) {
			System.out.println(s);
		}
	}

}
