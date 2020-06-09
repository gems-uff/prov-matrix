package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Document;

/**
 * @author Victor
 * 
 *         Represents Entity -> Instance of Entity Type (prov:type) : EnT
 *
 */
public class EntityInstance extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originEntitiesId;
	private List<String> destinationTypeEntitiesId;

	public EntityInstance() {
		super();
		this.relation = Relation.RELATION_ENTITY_INSTANCE;
		this.originEntitiesId = new ArrayList<>();
		this.destinationTypeEntitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityInstance(List<String> entitiesList, Set<String> entityTypes) {
		this();
		this.originEntitiesId = entitiesList;
		this.destinationTypeEntitiesId = new ArrayList<String>();
		for (String ett : entityTypes) {
			if (ett.contains("::") && !ett.endsWith("::")) {
				String[] etts = ett.split("::");
				for (int i = 0; i < etts.length; i++) {
					if (!destinationTypeEntitiesId.contains(etts[i])) {
						destinationTypeEntitiesId.add(etts[i]);
					}
				}
			} else {
				if (!destinationTypeEntitiesId.contains(ett)) {
					destinationTypeEntitiesId.add(ett);
				}
			}
		}
		matrix = new CRSMatrix(originEntitiesId.size(), destinationTypeEntitiesId.size());
	}

	public void add(String src, String dest) {
		int i = this.originEntitiesId.indexOf(src);
		int j = this.destinationTypeEntitiesId.indexOf(dest);
		if (i == -1) {
			this.originEntitiesId.add(src);
			i = this.originEntitiesId.indexOf(src);
		}
		if (j == -1) {
			this.destinationTypeEntitiesId.add(dest);
			j = this.destinationTypeEntitiesId.indexOf(dest);
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

	public List<String> getOriginEntitiesId() {
		return originEntitiesId;
	}

	public void setOriginEntitiesId(List<String> activitiesId) {
		this.originEntitiesId = activitiesId;
	}

	public List<String> getDestinationTypeEntitiesId() {
		return destinationTypeEntitiesId;
	}

	public void setDestinationTypeEntitiesId(List<String> agentsId) {
		this.destinationTypeEntitiesId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationTypeEntitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getOriginEntitiesId();
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String getRowDimentionName() {
		return ProvType.PROV_ENTITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvType.PROV_ABBREVIATE_ENTITY;
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