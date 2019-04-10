import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import convertion.ProvMatrixConverter;
import model.ActivityAgent;
import model.EntityActivity;
import model.EntityAgent;

/**
 * 
 * @author Victor
 *
 */
public class PrintMatrix {

	public PrintMatrix() {
	}

	public static void main(String[] args) {
		InteropFramework intF = new InteropFramework();
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
		System.out.println(ProvMatrixConverter.getInstance().toCSV(eac,true));
	}

}