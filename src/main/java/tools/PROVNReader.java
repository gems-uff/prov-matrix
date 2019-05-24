package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PROVNReader extends InputStreamReader {

	int edgeOptionalID = 0;
	private File input;
	private FileWriter fileWriter;
	private Map<String, String> indexes;
	private List<String> activities;
	private List<String> agents;
	private List<String> entities;

	public PROVNReader(String input, String output) throws IOException {
		super(new FileInputStream(input));
		this.indexes = new HashMap<>();
		this.activities = new ArrayList<>();
		this.agents = new ArrayList<>();
		this.entities = new ArrayList<>();
		this.input = new File(input);
		this.fileWriter = new FileWriter(output);
	}

	public void readFile() throws URISyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(this.input));
		try {
			String line = br.readLine();
			while (line != null) {
				line = readLine(line);
				this.fileWriter.write(line + "\n");
				line = br.readLine();
			}
		} finally {
			br.close();
			this.fileWriter.flush();
			this.fileWriter.close();
		}
	}

	public void readFile2() throws URISyntaxException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(this.input));
		try {
			String line = br.readLine();
			while (line != null) {
				String newLine = line;
				newLine = newLine.replace("=,", "=\"\",");
				newLine = newLine.replace(" ", "");
				newLine = newLine.replace("(semi-static)", "");
				newLine = newLine.replace("(Clone)", "");

				Pattern pattern = Pattern.compile("=[-]\\d*[.]\\d*|\\d*[.]\\d*|=\\w*");
				Matcher matcher = pattern.matcher(line);
				while (matcher.find()) {
					String oldToken = line.substring(matcher.start(), matcher.end());
					String newToken = oldToken;
					if (newToken.length() > 1) {
						newToken = newToken.replace("=", "=\"");
						newToken += "\"";
					}
					newLine = newLine.replace(oldToken, newToken);
					newLine = newLine.replace("\"Spawned\"Smoke", "\"SpawnedSmoke\"");
					newLine = newLine.replace("\"Fired\"Missile", "\"FiredMissile\"");
					newLine = newLine.replace("\"Spawning\"Smoke", "\"SpawningSmoke\"");
					newLine = newLine.replace("\"Taking\"Damage", "\"TakingDamage\"");

					newLine = newLine.replace("\"Enemy\"Spider", "EnemySpider\"");
					newLine = newLine.replace("\"Taking\"Hit", "\"TakingHit\"");
					newLine = newLine.replace("\"Being\"Hit", "\"BeingHit\"");
					newLine = newLine.replace("\"Terminal\"ForDoorFromHell_1", "\"TerminalForDoorFromHell_1\"");
					newLine = newLine.replace("\"Terminal\"ForDoubleHellDoor3A", "\"TerminalForDoubleHellDoor3A\"");
					newLine = newLine.replace("\"Terminal\"ForDoubleHellDoor3B", "\"TerminalForDoubleHellDoor3B\"");
					newLine = newLine.replace("\"Terminal\"ForDoubleHellDoor_3B", "\"TerminalForDoubleHellDoor_3B\"");
					newLine = newLine.replace("\"LockedDoor\"FromHell_1", "\"LockedDoorFromHell_1\"");
					newLine = newLine.replace("\"LockedDoor\"FromHellDouble_3", "\"LockedDoorFromHellDouble_3\"");

					newLine = newLine.replace("\"Score\"Missed", "ScoreMissed");
					newLine = newLine.replace("\"Enemy\"Mech", "EnemyMech");
					newLine = newLine.replace("\"(", "(");
					newLine = newLine.replace("E-4", "");
					newLine = newLine.replace("0\"0", "00");
					newLine = newLine.replace("0\"2", "02\"");
					newLine = newLine.replace("2\"7", "27");
					newLine = newLine.replace("2\"3", "23");
					newLine = newLine.replace("),", ")\",");

				}
				newLine = newLine.replace(",", ", ");
				newLine = newLine.replace("\".", ".");
				newLine = newLine.replace("=]", "=\"\"]");
				this.fileWriter.write(newLine + "\n");
				line = br.readLine();
			}
		} finally {
			br.close();
			this.fileWriter.flush();
			this.fileWriter.close();
		}

	}

	public String readLine(String line) {
		String[] elements;
		String[] statement;
		String[] attributes;
		String[] optionalAttributes = null;

		line = line.replace(" ", "");
		line = line.replace("\t", "");
		elements = line.split("\\(");
		if (elements.length > 1) {
			statement = elements[1].split("\\[");
			attributes = statement[0].split(",");

			if (statement.length > 1) {
				optionalAttributes = statement[1].split(",");
			}
			if (elements[0].contains("agent")) {
				line = rewriteAgent(line, attributes, optionalAttributes);
			} else if (elements[0].contains("activity")) {
				line = rewriteActivity(line, attributes, optionalAttributes);
			} else if (elements[0].contains("entity")) {
				line = rewriteEntity(line, attributes, optionalAttributes);
			} else {
				// line = line.replace("wasInfluencedBy", "wasInformedBy");
				// line = line.replace("wasInformedBy(edge_1237", "wasInfluencedBy(edge_1237");
				// line = line.replace("wasInformedBy(edge_1238", "wasInfluencedBy(edge_1238");
				// line = line.replace("wasInformedBy(edge_495", "wasInfluencedBy(edge_495");

				/*
				 * if (line.contains("wasInformedBy(edge_1121") ||
				 * line.contains("wasInformedBy(edge_928") ||
				 * line.contains("wasInformedBy(edge_476")) { line = ""; }
				 */
				line = line.replaceFirst(",\\[.*\\]", "");
				line = updateLineIndexes(line);
			}
		}
		line = line.replace("])]", "]");
		return line;
	}

	private String updateLineIndexes(String line) {
		Set<String> keysSet = this.indexes.keySet();
		List<String> keys = new ArrayList<>(keysSet);
		Collections.sort(keys);
		Collections.reverse(keys);
		for (String key : keys) {
			line = line.replace(key, this.indexes.get(key));
		}
		return line;
	}

	private String getStringAttributes(String[] atts) {
		String stringAttributes = "[";
		for (int i = 0; i < atts.length; i++) {
			if (atts[i].split("=")[0].equals("Health") || atts[i].split("=")[0].equals("ObjectPosition_X")
					|| atts[i].split("=")[0].equals("ObjectPosition_Y")
					|| atts[i].split("=")[0].equals("ObjectPosition_Z") || atts[i].split("=")[0].equals("Timestamp")
					|| atts[i].split("=")[0].equals("Label") || atts[i].split("=")[0].equals("ObjectName")
					|| atts[i].split("=")[0].equals("ObjectID") || atts[i].split("=")[0].equals("ObjectTag")) {
				stringAttributes += atts[i] + ",";
			}
		}
		stringAttributes += "]";
		stringAttributes = stringAttributes.replace(",]", "]");
		return stringAttributes;
	}

	private String getAttribute(String attKey, String[] atts) {
		String attValue = "";
		for (int i = 0; i < atts.length; i++) {
			String[] attMap = atts[i].split("=");
			if (attMap[0].equals(attKey)) {
				attValue = attMap[1];
			}
		}
		return attValue;
	}

	public String rewriteEntity(String line, String[] attributes, String[] optionalAttributes) {
		String id = attributes[0].replace("\"", "");
		String entity = getAttribute("ObjectName", optionalAttributes).replace("])", "")/* + "_"*/;
		/*entity += getAttribute("ObjectID", optionalAttributes).replace(".0", "");*/
		entity = entity.replace("\"", "");
		line = "entity(" + entity + "," + getStringAttributes(optionalAttributes) + ")";
		this.indexes.put(id, entity);
		return line;
	}

	public String rewriteAgent(String line, String[] attributes, String[] optionalAttributes) {
		String id = attributes[0].replace("\"", "");
		;
		String agent = getAttribute("ObjectName", optionalAttributes).replace("])", "") + "_";
		agent += getAttribute("ObjectID", optionalAttributes).replace(".0", "").replace("\t", "").trim();
		agent = agent.replace("\"", "");
		line = "agent(" + agent + "," + getStringAttributes(optionalAttributes) + ")";
		this.addAgent(agent, optionalAttributes);
		this.indexes.put(id, agent);
		return line;
	}

	public String rewriteActivity(String line, String[] attributes, String[] optionalAttributes) {
		String id = attributes[0].replace("\"", "");
		String activity = getAttribute("Label", optionalAttributes).replace("])", "");
		activity = activity.replace(" ", "_");
		activity = activity.replace("\"", "");

		String newLine = "";
		String agentOrEntity = getAttribute("ObjectName", optionalAttributes).replace("])", "") + "_";
		agentOrEntity += getAttribute("ObjectID", optionalAttributes).replace(".0", "").replace("\t", "").trim();
		agentOrEntity = agentOrEntity.replace("\"", "");
		if (getAttribute("ObjectTag", optionalAttributes).contains("Player")
				|| getAttribute("ObjectTag", optionalAttributes).contains("Enemy")) {
			newLine = addAgent(agentOrEntity, optionalAttributes);
			newLine = addActivity(activity);
			newLine += "wasAssociatedWith(" + activity + ", " + agentOrEntity + ", "
					+ getStringAttributes(optionalAttributes) + ",-)";
		} else {
			newLine = addEntity(agentOrEntity, optionalAttributes);
			newLine = addActivity(activity);
			newLine += "wasGeneratedBy(" + agentOrEntity + ", " + activity + ", "
					+ getStringAttributes(optionalAttributes) + ",-)";
		}
		this.indexes.put(id, activity);
		return newLine;
	}

	private String addEntity(String entity, String[] optionalAttributes) {
		String newLine = "";
		for (String ent : this.entities) {
			if (ent != null && ent.equals(entity)) {
				return "";
			}
		}
		this.entities.add(entity);
		newLine = "entity(" + entity + "," + getStringAttributes(optionalAttributes) + ")\n";
		return newLine;
	}

	private String addActivity(String activity) {
		String newLine = "";
		if (!this.activities.contains(activity)) {
			this.activities.add(activity);
			newLine = "activity(" + activity + ",-,-)\n";
		}
		return newLine;
	}

	private String addAgent(String agent, String[] optionalAttributes) {
		String newLine = "";
		for (String agt : this.agents) {
			if (agt != null && agt.equals(agent)) {
				return "";
			}
		}
		this.agents.add(agent);
		newLine = "agent(" + agent + "," + getStringAttributes(optionalAttributes) + ")\n";
		return newLine;
	}

	public String getEdgeID(String[] att, String id) {
		if (att.length == 2) {
			if (att[0].equalsIgnoreCase("-")) {
				id = "Edge_" + edgeOptionalID;
				edgeOptionalID++;
			} else {
				id = att[0];
			}
		} else {
			id = "Edge_" + edgeOptionalID;
			edgeOptionalID++;
		}
		return id;
	}

	public String getEdge1stAttribute(String attribute, String attr) {
		String[] att = attribute.split(";");
		if (att.length == 2) {
			attr = att[1];
		} else {
			attr = att[0];
		}
		return attr;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		PROVNReader pReader = new PROVNReader("etc/games/old/smoke-squadron-3-src.provn",
				"etc/games/old/smoke-squadron-3-new.provn");
		pReader.readFile();
		pReader.close();
	}

}