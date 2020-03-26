package model;

import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;

public interface ProvMatrix extends ProvRelation {

	CRSMatrix getMatrix();

	Relation getRelation();
	
	String getIdentifier();

	String getRowDimentionName();

	String getRowDimentionAbbreviate();

	String getColumnDimentionName();

	String getColumnDimentionAbbreviate();

	List<String> getColumnDescriptors();

	List<String> getRowDescriptors();
	
	boolean isEmpty();

}
