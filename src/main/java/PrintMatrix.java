import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;

import model.ActivityAgent;

/**
 * A little provenance goes a long way. 
 * ProvToolbox Tutorial 1: creating a provenance document in Java and serializing it
 * to SVG (in a file) and to PROVN (on the console).
 * 
 * @author lucmoreau
 * @see <a href="http://blog.provbook.org/2013/10/11/a-little-provenance-goes-a-long-way/">a-little-provenance-goes-a-long-way blog post</a>
 */
public class PrintMatrix {

	public static final String PROVBOOK_NS = "http://www.provbook.org";
	public static final String PROVBOOK_PREFIX = "provbook";

	public static final String JIM_PREFIX = "jim";
	public static final String JIM_NS = "http://www.cs.rpi.edu/~hendler/";

	private final ProvFactory pFactory;
	private final Namespace ns;

	public PrintMatrix(ProvFactory pFactory) {
		this.pFactory = pFactory;
		ns = new Namespace();
		ns.addKnownNamespaces();
		ns.register(PROVBOOK_PREFIX, PROVBOOK_NS);
		ns.register(JIM_PREFIX, JIM_NS);
	}

	public void doConversions(String filein) {
		InteropFramework intF = new InteropFramework();
		Document d = intF.readDocumentFromFile(filein);
		ActivityAgent aa = new ActivityAgent(d);
		aa.buildMatrix();
		System.out.println(aa.getMatrix());
	}

	public void closingBanner() {
		System.out.println("");
		System.out.println("*************************");
	}

	public void openingBanner() {
		System.out.println("*************************");
		System.out.println("* Converting document  ");
		System.out.println("*************************");
	}

	public static void main(String[] args) {
		if (args.length != 1)
			throw new UnsupportedOperationException("main to be called with two filenames");
		String filein = args[0];

		PrintMatrix tutorial = new PrintMatrix(InteropFramework.newXMLProvFactory());
		tutorial.openingBanner();
		tutorial.doConversions(filein);
		tutorial.closingBanner();

	}

}