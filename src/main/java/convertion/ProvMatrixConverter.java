package convertion;

import java.util.Iterator;

import org.la4j.matrix.sparse.CRSMatrix;

import model.ProvMatrix;

public class ProvMatrixConverter {

	private static ProvMatrixConverter instance = null;

	public ProvMatrixConverter() {
		super();
	}

	public static ProvMatrixConverter getInstance() {
		if (instance == null) {
			instance = new ProvMatrixConverter();
		}
		return instance;
	}

	public String toCSV(ProvMatrix pmatrix, boolean withDescriptors) {
		StringBuffer sb = new StringBuffer();
		String firstLine = "";
		if (pmatrix != null) {
			if (withDescriptors) {
				firstLine = "X,";
				for (Iterator<String> iterator = pmatrix.getColumnDescriptors().iterator(); iterator.hasNext();) {
					String colName = iterator.next();
					firstLine += colName + ",";
				}
				firstLine = firstLine.substring(0, firstLine.length() - 1);
				firstLine += "\n";
			}
			CRSMatrix matrix = pmatrix.getMatrix();
			int i = 0;
			int j = 0;
			int rows = pmatrix.getRowDescriptors().size();
			int columns = pmatrix.getColumnDescriptors().size();
			if (withDescriptors) {
				columns += 1;
			}
			while (i < rows) {
				while (j < columns) {
					if (j == 0 && withDescriptors) {
						sb.append(pmatrix.getRowDescriptors().get(i) + ",");
					} else {
						Double d = new Double(matrix.get(i, j - 1));
						if (j != columns - 1) {
							sb.append(d.intValue() + ",");
						} else {
							sb.append(d.intValue());
						}
					}
					j++;
				}
				if (i != (rows - 1)) {
					sb.append("\n");
				}
				j = 0;
				i++;
			}
		}
		return firstLine + sb.toString();
	}

}
