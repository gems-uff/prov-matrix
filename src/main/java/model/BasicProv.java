package model;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.QualifiedName;

public class BasicProv {
	
	public String id(QualifiedName qn) {
		String id = "";
		if (qn != null) {
			if (qn.getPrefix() != null) {
				if (qn.getPrefix().length() == 0) {
					id = qn.getLocalPart();
				} else {
					id = qn.getPrefix() + ":" + qn.getLocalPart();
				}
			} else {
				id = qn.getLocalPart();
			}
		}
		return id;
	}
	
	public CRSMatrix growMatrix(CRSMatrix matrix, int rows, int columns) {
		CRSMatrix newMatrix = new CRSMatrix(rows, columns);
		for (int i = 0; i < matrix.rows(); i++) {
			for (int j = 0; j < matrix.columns(); j++) {
				newMatrix.set(i, j, matrix.get(i, j));
			}
		}
		return newMatrix;
	}

}
