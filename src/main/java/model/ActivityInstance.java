package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Document;

/**
 * @author Victor
 * 
 *         Represents Activity -> Instance of Activity Type (prov:type) : AcT
 *
 */
public class ActivityInstance extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originActivitiesId;
	private List<String> destinationTypeActivitiesId;

	public ActivityInstance() {
		super();
		this.relation = Relation.RELATION_ACTIVITY_INSTANCE;
		this.originActivitiesId = new ArrayList<>();
		this.destinationTypeActivitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public ActivityInstance(List<String> activitiesList, Set<String> activityTypes) {
		this.originActivitiesId = activitiesList;
		this.destinationTypeActivitiesId = new ArrayList<String>(activityTypes);
		matrix = new CRSMatrix(activitiesList.size(), activityTypes.size());
	}
	
	public void add(String src, String dest) {
		int i = this.originActivitiesId.indexOf(src);
		int j = this.destinationTypeActivitiesId.indexOf(dest);
		if (i == -1) {
			this.originActivitiesId.add(src);
			i = this.originActivitiesId.indexOf(src);
		}
		if (j == -1) {
			this.destinationTypeActivitiesId.add(dest);
			j = this.destinationTypeActivitiesId.indexOf(dest);
		}
		this.matrix.set(i, j, this.matrix.get(i, j) + 1);
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getOriginActivitiesId() {
		return originActivitiesId;
	}

	public void setOriginActivitiesId(List<String> activitiesId) {
		this.originActivitiesId = activitiesId;
	}

	public List<String> getDestinationTypeActivitiesId() {
		return destinationTypeActivitiesId;
	}

	public void setDestinationTypeActivitiesId(List<String> agentsId) {
		this.destinationTypeActivitiesId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationTypeActivitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getOriginActivitiesId();
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String getRowDimentionName() {
		return ProvType.PROV_ACTIVITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ACTIVITY;
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