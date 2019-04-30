package convertion;

import java.util.ArrayList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Document;

import model.ActivityActivity;
import model.ActivityAgent;
import model.ActivityEntity;
import model.AgentAgent;
import model.EntityActivity;
import model.EntityAgent;
import model.EntityEntity;
import model.ProvMatrix;
import model.ProvMatrix.Relation;

public class ProvMatrixFactory {

	private Document document;

	public ProvMatrixFactory() {
		super();
	}

	public ProvMatrixFactory(String filename) {
		InteropFramework intF = new InteropFramework();
		this.document = intF.readDocumentFromFile(filename);
	}

	public ProvMatrixFactory(Document document) {
		this.document = document;
	}

	public List<ProvMatrix> buildMatrices() {
		List<ProvMatrix> matrices = new ArrayList<>();
		if (this.document != null) {
			buildEntityMatrices(matrices);
			buildActivityMatrices(matrices);
			AgentAgent aa = new AgentAgent(this.document);
			aa.buildMatrix();
			matrices.add(aa);
		}
		return matrices;
	}

	private void buildEntityMatrices(List<ProvMatrix> matrices) {
		EntityEntity ee1 = new EntityEntity(this.document);
		ee1.setRelation(Relation.RELATION_DERIVATION);
		ee1.buildMatrix();
		matrices.add(ee1);
		EntityEntity ee2 = new EntityEntity(this.document);
		ee2.setRelation(Relation.RELATION_ALTERNATE);
		ee2.buildMatrix();
		matrices.add(ee2);
		EntityEntity ee3 = new EntityEntity(this.document);
		ee3.setRelation(Relation.RELATION_INFLUENCE);
		ee3.buildMatrix();
		matrices.add(ee3);
		EntityEntity ee4 = new EntityEntity(this.document);
		ee4.setRelation(Relation.RELATION_SPECIALIZATION);
		ee4.buildMatrix();
		matrices.add(ee4);
		EntityEntity ee5 = new EntityEntity(this.document);
		ee5.setRelation(Relation.RELATION_MENTION);
		ee5.buildMatrix();
		matrices.add(ee5);

		EntityActivity eac1 = new EntityActivity(this.document);
		eac1.setRelation(Relation.RELATION_GENERATION);
		eac1.buildMatrix();
		matrices.add(eac1);
		EntityActivity eac2 = new EntityActivity(this.document);
		eac2.setRelation(Relation.RELATION_INVALIDATION);
		eac2.buildMatrix();
		matrices.add(eac2);

		EntityAgent eag = new EntityAgent(this.document);
		eag.buildMatrix();
		matrices.add(eag);
	}

	private void buildActivityMatrices(List<ProvMatrix> matrices) {
		ActivityEntity ae = new ActivityEntity(this.document);
		ae.buildMatrix();
		matrices.add(ae);

		ActivityAgent aag = new ActivityAgent(this.document);
		aag.buildMatrix();
		matrices.add(aag);

		ActivityActivity aac1 = new ActivityActivity(this.document);
		aac1.setRelation(Relation.RELATION_COMMUNICATION);
		aac1.buildMatrix();
		matrices.add(aac1);
		ActivityActivity aac2 = new ActivityActivity(this.document);
		aac2.setRelation(Relation.RELATION_START);
		aac2.buildMatrix();
		matrices.add(aac2);
		ActivityActivity aac3 = new ActivityActivity(this.document);
		aac3.setRelation(Relation.RELATION_END);
		aac3.buildMatrix();
		matrices.add(aac3);
	}

}
