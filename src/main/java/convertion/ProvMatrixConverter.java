package convertion;

import java.util.Iterator;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
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

	public String toCSV(Matrix matrix) {
		StringBuffer sb = new StringBuffer();
		if (matrix != null) {
			int i = 0;
			int j = 0;
			int rows = matrix.rows();
			int columns = matrix.columns();
			while (i < rows) {
				while (j < columns) {
					Double d = new Double(matrix.get(i, j));
					if (j != columns - 1) {
						sb.append(d.intValue() + ",");
					} else {
						sb.append(d.intValue());
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
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Matrix a = new Basic2DMatrix(new double[][]{
			   { 1.0, 2.0, 3.0 },
			   { 4.0, 5.0, 6.0 },
			   { 7.0, 8.0, 9.0 }
			});
		Matrix b = new Basic2DMatrix(new double[][]{
			   { 7.0, 8.0, 9.0 },
			   { 1.0, 2.0, 3.0 },
			   { 5.0, 5.0, 6.0 }
			});
		Matrix c = a.multiply(b);
		Matrix d = b.withInverter(InverterFactory.GAUSS_JORDAN).inverse();
		System.out.println(ProvMatrixConverter.getInstance().toCSV(c));
		System.out.println(ProvMatrixConverter.getInstance().toCSV(c.multiply(d)));
	}

}
