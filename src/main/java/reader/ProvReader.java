/*
 * The MIT License
 *
 * Copyright 2019 Victor Alencar.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import model.ProvActivity;
import model.ProvRelation;
import model.ProvType;
import model.ProvRelation.Relation;

/**
 *
 * @author Victor Alencar
 */
public class ProvReader {

	private File provFile;
	private List<ProvType> entities;
	private List<ProvType> agents;
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

		line = line.replaceAll("\\)", "");
		line = line.replaceAll(" ", "");
		line = line.replaceAll("\t", "");
		line = line.replaceAll("]", "");
		line = line.replaceAll("'", "");
		line = line.replaceAll("\"", "");
		elements = line.split("\\(");
		if (elements.length > 1) {
			statement = elements[1].split("\\[");
			attributes = statement[0].split("\\s*,\\s*");

			if (statement.length > 1) {
				optionalAttributes = statement[1].split("\\s*,\\s*");
			}

			if (elements[0].equals(ProvType.PROV_ENTITY.toLowerCase())) {
				readEntity(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvType.PROV_ACTIVITY.toLowerCase())) {
				readActivity(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvType.PROV_AGENT.toLowerCase())) {
				readAgent(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_GENERATION)) {
				readGeneration(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_USAGE)) {
				readUsage(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_COMMUNICATION)) {
				readCommunication(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_START)) {
				readStart(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_END)) {
				readEnd(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_INVALIDATION)) {
				readInvalidation(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_DERIVATION)) {
				readDerivation(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_ATTRIBUTION)) {
				readAttribution(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_ASSOCIATION)) {
				readAssociation(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_DELEGATION)) {
				readDelegation(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_INFLUENCE)) {
				readInfluence(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_MEMBERSHIP)) {
				readMembership(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_SPECIALIZATION)) {
				readSpecialization(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_ALTERNATE)) {
				readAlternate(attributes, optionalAttributes);
			}
			if (elements[0].equals(ProvRelation.PROV_MENTION)) {
				readMention(attributes, optionalAttributes);
			}
		}
	}

	private ProvType getAgent(String agent) {
		for (ProvType ag : this.agents) {
			if (ag != null && ag.getName().equals(agent)) {
				return ag;
			}
		}
		return null;
	}

	private ProvType getEntity(String entity) {
		for (ProvType e : this.entities) {
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
		for (ProvActivity ac : this.activities) {
			if (ac != null && ac.getName().equals(activity)) {
				return ac;
			}
		}
		return null;
	}

	public void readEntity(String[] attributes, String[] optionalAttributes) {
		String id = attributes[0];
		this.entities.add(new ProvType(id, ProvType.PROV_ENTITY, optionalAttributes));
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
		this.agents.add(new ProvType(id, ProvType.PROV_AGENT, optionalAttributes));
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
		ProvTimedStatement wasGeneratedBy = new ProvTimedStatement(Relation.RELATION_GENERATION, getEntity(entity),
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
		ProvTimedStatement used = new ProvTimedStatement(Relation.RELATION_USAGE, getActivity(activity),
				getEntity(entity), optionalAttributes);
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

		ProvStatement wasInformedBy = new ProvStatement(Relation.RELATION_COMMUNICATION, getActivity(informed),
				getActivity(informant), optionalAttributes);
		wasInformedBy.setId(id);
		this.statements.add(wasInformedBy);
	}

	public void readStart(String[] attributes, String[] optionalAttributes) {
		starterOrEnder(attributes, optionalAttributes, Relation.RELATION_START);
	}

	public void readEnd(String[] attributes, String[] optionalAttributes) {
		starterOrEnder(attributes, optionalAttributes, Relation.RELATION_END);
	}

	public void starterOrEnder(String[] attributes, String[] optionalAttributes, Relation relation) {
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

		ProvTriggeredStament provTrigger = new ProvTriggeredStament(relation, getActivity(activity),
				getActivity(trigger), optionalAttributes);
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
		ProvTimedStatement wasInvalidatedBy = new ProvTimedStatement(Relation.RELATION_INVALIDATION, getEntity(entity),
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
		provDerivation = new ProvDerivationStatement(Relation.RELATION_DERIVATION, getEntity(generatedEntity),
				getEntity(usedEntity), optionalAttributes);
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

		ProvStatement provAttribution = new ProvStatement(Relation.RELATION_ATTRIBUTION, getEntity(entity),
				getAgent(agent), optionalAttributes);
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

		if (getAgent(agent) == null || getActivity(activity)== null) {
			throw new RuntimeException("Agente ("+agent+") ou atividade ("+activity+") não encontrado!");
		}
		ProvStatement wasAssociatedWith = new ProvStatement(Relation.RELATION_ASSOCIATION, getActivity(activity),
				getAgent(agent), optionalAttributes);
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
		ProvDelegationStatement actedOnBehalfOf = new ProvDelegationStatement(Relation.RELATION_DELEGATION,
				getAgent(delegate), getAgent(responsible), optionalAttributes);
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

		ProvStatement wasInfluencedBy = new ProvStatement(Relation.RELATION_INFLUENCE, getEntity(influencer),
				getEntity(influencee), optionalAttributes);
		wasInfluencedBy.setId(id);
		this.statements.add(wasInfluencedBy);
	}

	public void readAlternate(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembershipMention(attributes, optionalAttributes, Relation.RELATION_ALTERNATE);
	}

	public void readSpecialization(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembershipMention(attributes, optionalAttributes, Relation.RELATION_SPECIALIZATION);
	}
	
	public void readMention(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembershipMention(attributes, optionalAttributes, Relation.RELATION_MENTION);
	}

	public void readMembership(String[] attributes, String[] optionalAttributes) {
		alternateOrSpecializationOrMembershipMention(attributes, optionalAttributes, Relation.RELATION_MEMBERSHIP);
	}

	public void alternateOrSpecializationOrMembershipMention(String[] attributes, String[] optionalAttributes,
			Relation relation) {
		String id = null;
		String alternateSpecMember1 = "-";
		String alternateSpecMember2;

		id = getOptionalID(attributes[0]);
		alternateSpecMember1 = get1stAttribute(attributes[0]);
		alternateSpecMember2 = attributes[1];

		ProvStatement alternateOrSpecializationOrMemberOforMentionOf = new ProvStatement(relation, getEntity(alternateSpecMember2),
				getEntity(alternateSpecMember1), optionalAttributes);
		alternateOrSpecializationOrMemberOforMentionOf.setId(id);
		this.statements.add(alternateOrSpecializationOrMemberOforMentionOf);
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

	public List<ProvType> getEntities() {
		return entities;
	}

	public void setEntities(List<ProvType> entities) {
		this.entities = entities;
	}

	public List<ProvType> getAgents() {
		return agents;
	}

	public void setAgents(List<ProvType> agents) {
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
		for (ProvType e : provReader.getEntities()) {
			System.out.println("entity(" + e);
		}
		for (ProvType a : provReader.getAgents()) {
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
