package model;

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
			}
		}
		return id;
	}

}
