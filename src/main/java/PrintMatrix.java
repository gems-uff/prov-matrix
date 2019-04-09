import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import model.ActivityAgent;

/**
 * 
 * @author Victor
 *
 */
public class PrintMatrix {

	public PrintMatrix() {
	}

	public static void main(String[] args) {
		if (args.length != 1)
			throw new UnsupportedOperationException("main to be called with two filenames");
		String filein = args[0];

		InteropFramework intF = new InteropFramework();
		Document d = intF.readDocumentFromFile(filein);
		ActivityAgent aa = new ActivityAgent(d);
		aa.buildMatrix();
		System.out.println(aa.getMatrix());

	}

}