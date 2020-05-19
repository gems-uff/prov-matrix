package tools;
import java.util.Arrays;

import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.matrix.functor.MatrixProcedure;
import org.la4j.matrix.sparse.CRSMatrix;

/**
 * 
 * @author Victor
 *
 */
public class PrintMatrix {

	public PrintMatrix() {
	}

	public static void main(String[] args) {
		/*InteropFramework intF = new InteropFramework();
		Document d = intF.readDocumentFromFile("etc/cook.provn");
		ActivityAgent aa = new ActivityAgent(d);
		aa.buildMatrix();
		System.out.println(ProvMatrixConverter.getInstance().toCSV(aa,true));
		EntityAgent ea = new EntityAgent(d);
		ea.buildMatrix();
		System.out.println();
		System.out.println(ProvMatrixConverter.getInstance().toCSV(ea,true));
		
		CRSMatrix c = new CRSMatrix(ea.getMatrix().multiply(aa.getMatrix().transpose()));
		EntityActivity eac = new EntityActivity(c);
		eac.setActivitiesId(aa.getActivitiesId());
		eac.setEntitiesId(ea.getEntitiesId());
		
		System.out.println();
		System.out.println(ProvMatrixConverter.getInstance().toCSV(eac,true));*/

		/*Matrix m1 = new Basic2DMatrix(new double[][] { 
			{ 0, 0, 0, 0, 0 }, 
			{ 0, 0, 0, 0, 1 }, 
			{ 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 1 }, 
			{ 1, 0, 0, 0, 0 } });*/
		Matrix m1 = new Basic2DMatrix(new double[][] { 
		{ 18, 5, 7, 10}, 
		{ 3,  8, 0, 4 },
		{ 7,  3, 19, 9 },
		{ 10, 4, 0, 0 }});
		Matrix inverse = m1.withInverter(InverterFactory.GAUSS_JORDAN).inverse();
		System.out.println(m1);
		System.out.println(inverse);
		
		CRSMatrix temp = new CRSMatrix(m1.rows(),m1.columns());
		temp.assign(Double.POSITIVE_INFINITY);
		m1.eachNonZero(new MatrixProcedure() {
			@Override
			public void apply(int row, int col, double value) {
				temp.set(row, col, value);
			}
		});
		double[] min = new double[m1.rows()*m1.columns()];
		int[] totalNonZero = new int[m1.rows()*m1.columns()];
		m1.eachNonZero(new MatrixProcedure() {
			@Override
			public void apply(int row, int col, double value) {
				totalNonZero[row*m1.columns()+col]=1;
			}
		});
		temp.eachNonZero(new MatrixProcedure() {
			@Override
			public void apply(int row, int col, double value) {
				min[row*m1.columns()+col]=value;
			}
		});
		int total = Arrays.stream(totalNonZero).sum();
		double mininum = Arrays.stream(min).min().getAsDouble();
		System.out.println("Non-zero Min Value = "+mininum);
		System.out.println("Max Value = "+m1.max());
		System.out.println("Non-zero Average = "+m1.sum()/total);
		System.out.println("Non-zero Total = "+total);
	}

}