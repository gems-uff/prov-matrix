package tools;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.la4j.LinearAlgebra.InverterFactory;
import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

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
		{ 18, 1, 7, 10}, 
		{ 1,  8, 3, 4 },
		{ 7,  3, 19, 9 },
		{ 10, 4, 9, 23 }});
		Matrix inverse = m1.withInverter(InverterFactory.GAUSS_JORDAN).inverse();
		System.out.println(m1);
		System.out.println(inverse);
	}

}