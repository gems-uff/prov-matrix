package model;

import java.util.ArrayList;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;

public class EntityActivity implements ProvMatrix {

	private CRSMatrix matrix;

	private List<String> entitiesId;
	private List<String> activitiesId;

	public EntityActivity() {
		this.entitiesId = new ArrayList<>();
		this.activitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public EntityActivity(CRSMatrix c) {
		this.matrix = c;
	}

	@Override
	public CRSMatrix getMatrix() {
		return this.matrix;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.activitiesId;
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.entitiesId;
	}

	public List<String> getEntitiesId() {
		return entitiesId;
	}

	public void setEntitiesId(List<String> entitiesId) {
		this.entitiesId = entitiesId;
	}

	public List<String> getActivitiesId() {
		return activitiesId;
	}

	public void setActivitiesId(List<String> activitiesId) {
		this.activitiesId = activitiesId;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

}
