package model;

import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;

public interface ProvMatrix {

	public CRSMatrix getMatrix();
	public List<String> getColumnDescriptors();

	public List<String> getRowDescriptors();

}
