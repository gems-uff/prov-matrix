package tools;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

/**
 * @author Victor
 *
 */
public class ProvnToSVG {

	public static void main(String[] args) {
		InteropFramework intF = new InteropFramework();
		Document document = intF.readDocumentFromFile("etc/project-management.provn");
		intF.writeDocument("etc/project-management.svg", document);

	}

}
