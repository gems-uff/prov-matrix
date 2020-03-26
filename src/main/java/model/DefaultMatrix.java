package model;

import java.util.ArrayList;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;

public class DefaultMatrix implements ProvMatrix {

	private CRSMatrix matrix;
	private String identifier;
	private String rowDimentionName;
	private String columnDimentionName;
	private List<String> rowDescriptors;
	private List<String> columnDescriptors;
	private String rowDimentionAbbreviate;
	private String columnDimentionAbbreviate;

	public DefaultMatrix() {
		super();
		this.matrix = new CRSMatrix();
		this.rowDescriptors = new ArrayList<String>();
		this.columnDescriptors = new ArrayList<String>();
	}

	public DefaultMatrix(int i, int j) {
		super();
		this.matrix = new CRSMatrix(i, j);
		this.rowDescriptors = new ArrayList<String>();
		this.columnDescriptors = new ArrayList<String>();
	}

	public void addRowDescriptor(String desc) {
		this.rowDescriptors.add(desc);
	}

	public void addRowValues(int i, String[] line) {
		for (int j = 1; j < line.length; j++) {
			this.matrix.set(i, j - 1, Double.valueOf(line[j]));
		}
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getRowDescriptors() {
		return rowDescriptors;
	}

	public void setRowDescriptors(List<String> rowDescriptors) {
		this.rowDescriptors = rowDescriptors;
	}

	public List<String> getColumnDescriptors() {
		return columnDescriptors;
	}

	public void setColumnDescriptors(List<String> columnDescriptors) {
		this.columnDescriptors = columnDescriptors;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isEmpty() {
		return this.matrix.density() == 0.0;
	}

	public String getRowDimentionName() {
		return rowDimentionName;
	}

	public void setRowDimentionName(String rowDimensionName) {
		this.rowDimentionName = rowDimensionName;
	}

	public String getColumnDimentionName() {
		return columnDimentionName;
	}

	public void setColumnDimentionName(String columnDimensionName) {
		this.columnDimentionName = columnDimensionName;
	}

	@Override
	public Relation getRelation() {
		return null;
	}

	public String getRowDimentionAbbreviate() {
		return rowDimentionAbbreviate;
	}

	public void setRowDimentionAbbreviate(String rowDimentionAbbreviate) {
		this.rowDimentionAbbreviate = rowDimentionAbbreviate;
	}

	public String getColumnDimentionAbbreviate() {
		return columnDimentionAbbreviate;
	}

	public void setColumnDimentionAbbreviate(String columnDimentionAbbreviate) {
		this.columnDimentionAbbreviate = columnDimentionAbbreviate;
	}

}
