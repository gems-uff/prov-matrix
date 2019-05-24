package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.la4j.matrix.sparse.CRSMatrix;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasStartedBy;

/**
 * @author Victor
 * 
 *         Represents Activity -> Activity : WasInformedBy || WasStartedBy ||
 *         WasEndedBy
 *
 */
public class ActivityActivity extends BasicProv implements ProvMatrix {

	private CRSMatrix matrix;
	private Relation relation;
	private Document document;
	private List<String> originActivitiesId;
	private List<String> destinationActivitiesId;

	public ActivityActivity() {
		super();
		this.originActivitiesId = new ArrayList<>();
		this.destinationActivitiesId = new ArrayList<>();
		this.matrix = new CRSMatrix();
	}

	public ActivityActivity(Relation relation) {
		this();
		this.relation = relation;
	}

	public ActivityActivity(Document d) {
		this();
		this.document = d;
		List<StatementOrBundle> sbs = d.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb instanceof Statement) {
				buildIndex(sb);
			} else {
				Bundle bundle = (Bundle) sb;
				buildBundleIndex(bundle.getStatement());
			}
		}
		Collections.sort(this.originActivitiesId);
		Collections.sort(this.destinationActivitiesId);
		matrix = new CRSMatrix(originActivitiesId.size(), destinationActivitiesId.size());
	}

	private void buildIndex(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == Kind.PROV_ACTIVITY) {
			Activity ac = (Activity) sb;
			originActivitiesId.add(id(ac.getId()));
			destinationActivitiesId.add(id(ac.getId()));
		}
	}
	
	private void buildBundleIndex(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			buildIndex(iterator.next());
		}
	}

	public void buildMatrix() {
		List<StatementOrBundle> sbs = document.getStatementOrBundle();
		for (Iterator<StatementOrBundle> iterator = sbs.iterator(); iterator.hasNext();) {
			StatementOrBundle sb = iterator.next();
			if (sb instanceof Statement) {
				processStatement(sb);
			} else {
				Bundle bundle = (Bundle) sb;
				processStatements(bundle.getStatement());
			}
		}
	}
	
	private void processStatements(List<Statement> statements) {
		for (Iterator<Statement> iterator = statements.iterator(); iterator.hasNext();) {
			processStatement(iterator.next());
		}
	}

	private void processStatement(StatementOrBundle sb) {
		if (sb != null && sb.getKind() == this.relation.getKind()) {
			switch (sb.getKind()) {
			case PROV_COMMUNICATION: {
				WasInformedBy wi = (WasInformedBy) sb;
				int i = originActivitiesId.indexOf(id(wi.getInformed()));
				int j = destinationActivitiesId.indexOf(id(wi.getInformant()));
				matrix.set(i, j, matrix.get(i, j) + 1);
				break;
			}
			case PROV_START: {
				WasStartedBy ws = (WasStartedBy) sb;
				int i = originActivitiesId.indexOf(id(ws.getActivity()));
				int j = destinationActivitiesId.indexOf(id(ws.getTrigger()));
				matrix.set(i, j, matrix.get(i, j) + 1);
				break;
			}
			case PROV_END: {
				WasEndedBy we = (WasEndedBy) sb;
				int i = originActivitiesId.indexOf(id(we.getActivity()));
				int j = destinationActivitiesId.indexOf(id(we.getTrigger()));
				matrix.set(i, j, matrix.get(i, j) + 1);
				break;
			}
			default:
				break;
			}
		}
	}

	public CRSMatrix getMatrix() {
		return matrix;
	}

	public void setMatrix(CRSMatrix matrix) {
		this.matrix = matrix;
	}

	public List<String> getOriginActivitiesId() {
		return originActivitiesId;
	}

	public void setOriginActivitiesId(List<String> activitiesId) {
		this.originActivitiesId = activitiesId;
	}

	public List<String> getDestinationActivitiesId() {
		return destinationActivitiesId;
	}

	public void setDestinationActivitiesId(List<String> agentsId) {
		this.destinationActivitiesId = agentsId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	@Override
	public List<String> getColumnDescriptors() {
		return this.getDestinationActivitiesId();
	}

	@Override
	public List<String> getRowDescriptors() {
		return this.getOriginActivitiesId();
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	@Override
	public String getRowDimentionName() {
		return ProvMatrix.PROV_ACTIVITY;
	}

	@Override
	public String getRowDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ACTIVITY;
	}

	@Override
	public String getColumnDimentionName() {
		return ProvMatrix.PROV_ACTIVITY;
	}

	@Override
	public String getColumnDimentionAbbreviate() {
		return ProvMatrix.PROV_ABBREVIATE_ACTIVITY;
	}

}
