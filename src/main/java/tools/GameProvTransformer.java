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

public class GameProvTransformer extends InputStreamReader {

	int edgeOptionalID = 0;
	private File input;
	private FileWriter fileWriter;
	private Map<String, String> indexes;
	private List<String> activities;
	private List<String> agents;
	private List<String> entities;

	public GameProvTransformer(String input, String output) throws IOException {
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
			attributes = statement[0].split("\\s*,\\s*");

			if (statement.length > 1) {
				optionalAttributes = statement[1].split("\\s*,\\s*");
			}
			if (elements[0].contains("agent") && optionalAttributes != null) {
				line = rewriteAgent(line, attributes, optionalAttributes);
			} else if (elements[0].contains("activity") && optionalAttributes != null) {
				line = rewriteActivity(line, attributes, optionalAttributes);
			} else if (elements[0].contains("entity") && optionalAttributes != null) {
				line = rewriteEntity(line, attributes, optionalAttributes);
			} else if (elements[0].contains("wasAssociatedWith") && optionalAttributes != null) {
				line = rewriteAssociation(line, attributes, optionalAttributes);
			} else {
				// line = line.replaceFirst(",\\[.*\\]", "");
				line = updateLineIndexes(line);
			}
		}
		line = line.replace("])]", "]");
		return line;
	}

	private String rewriteAssociation(String line, String[] attributes, String[] optionalAttributes) {

		String newLine = "";
		String agentOrEntity = getAttribute("ObjectName", optionalAttributes).replace("]", "") + "_";
		agentOrEntity += getAttribute("ObjectID", optionalAttributes).replace(".0", "").replace("\t", "").trim();
		agentOrEntity = agentOrEntity.replace("\"", "");
		if (getAttribute("ObjectTag", optionalAttributes).contains("Player")
				|| getAttribute("ObjectTag", optionalAttributes).contains("Enemy")) {
			newLine = addAgent(agentOrEntity, optionalAttributes);
		} else {
			newLine = addEntity(agentOrEntity, optionalAttributes);
		}
		if (newLine.length() > 0) {
			newLine = newLine + line;
		} else {
			newLine = line;
		}
		return newLine;
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
		if (atts != null) {
			for (int i = 0; i < atts.length; i++) {
				if (atts[i].split("=")[0].equals("Health") || atts[i].split("=")[0].equals("ObjectPosition_X")
						|| atts[i].split("=")[0].equals("ObjectPosition_Y")
						|| atts[i].split("=")[0].equals("ObjectPosition_Z") || atts[i].split("=")[0].equals("Timestamp")
						|| atts[i].split("=")[0].equals("Label") || atts[i].split("=")[0].equals("ObjectName")
						|| atts[i].split("=")[0].equals("ObjectID") || atts[i].split("=")[0].equals("ObjectTag")) {
					stringAttributes += atts[i] + ",";
				}
			}
		}
		stringAttributes += "]";
		stringAttributes = stringAttributes.replace(",]", "]");
		return stringAttributes;
	}

	private String getAttribute(String attKey, String[] atts) {
		String attValue = "";
		if (atts != null) {
			for (int i = 0; i < atts.length; i++) {
				String[] attMap = atts[i].split("=");
				if (attMap[0].equals(attKey)) {
					attValue = attMap[1];
				}
			}
		}
		return attValue;
	}

	public String rewriteEntity(String line, String[] attributes, String[] optionalAttributes) {
		String id = attributes[0].replace("\"", "");
		String entity = getAttribute("ObjectName", optionalAttributes).replace("])", "");
		entity = entity.replace("\"", "");
		line = "entity(" + entity + "," + getStringAttributes(optionalAttributes) + ")";
		this.indexes.put(id, entity);
		return line;
	}

	public String rewriteAgent(String line, String[] attributes, String[] optionalAttributes) {
		String id = attributes[0].replace("\"", "");
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

	public static void main(String[] args) throws IOException, URISyntaxException {
		GameProvTransformer pReader = new GameProvTransformer("etc/games/angry-bots-1.provn",
				"etc/games/angry-bots-11.provn");
		pReader.readFile();
		pReader.close();
	}

}