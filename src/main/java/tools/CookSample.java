package tools;
import java.util.Arrays;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasStartedBy;

/**
 * @author Victor
 *
 */
public class CookSample {

	public static final String CORDONBLEU_NS = "http://www.cordonbleu.edu";
	public static final String CORDONBLEU_PREFIX = "cb";

	private final ProvFactory pFactory;
	private final Namespace ns;

	public CookSample(ProvFactory pFactory) {
		this.pFactory = pFactory;
		ns = new Namespace();
		ns.addKnownNamespaces();
		ns.register(CORDONBLEU_PREFIX, CORDONBLEU_NS);
	}

	public QualifiedName qn(String n) {
		return ns.qualifiedName(CORDONBLEU_PREFIX, n, pFactory);
	}

	public Document makeDocument() {
		Entity entityMilk = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Milk", pFactory));
		Entity entityButter = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Butter", pFactory));
		Entity entityFlour = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Flour", pFactory));
		Entity entitySugar = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Sugar", pFactory));
		Entity entityIcing = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Icing", pFactory));
		Entity entityCake = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Cake", pFactory));
		Entity entityEggs = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Eggs", pFactory));
		Entity entityHandMixer = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "HandMixer", pFactory));
		Entity entityOven = pFactory.newEntity(ns.qualifiedName(CORDONBLEU_PREFIX, "Oven", pFactory));

		Agent agentCook = pFactory.newAgent(ns.qualifiedName(CORDONBLEU_PREFIX, "Cook", pFactory));

		Activity activityMix = pFactory.newActivity(ns.qualifiedName(CORDONBLEU_PREFIX, "Mix", pFactory));
		Activity activityBake = pFactory.newActivity(ns.qualifiedName(CORDONBLEU_PREFIX, "Bake", pFactory));
		Activity activityDecorate = pFactory.newActivity(ns.qualifiedName(CORDONBLEU_PREFIX, "Decorate", pFactory));

		WasDerivedFrom wd1 = pFactory.newWasDerivedFrom(entityIcing.getId(), entityButter.getId());
		WasDerivedFrom wd2 = pFactory.newWasDerivedFrom(entityButter.getId(), entityMilk.getId());

		Used used1 = pFactory.newUsed(activityMix.getId(), entityButter.getId());
		Used used2 = pFactory.newUsed(activityMix.getId(), entityFlour.getId());
		Used used3 = pFactory.newUsed(activityMix.getId(), entitySugar.getId());
		Used used4 = pFactory.newUsed(activityMix.getId(), entityEggs.getId());
		Used used5 = pFactory.newUsed(activityMix.getId(), entityHandMixer.getId());
		Used used6 = pFactory.newUsed(activityBake.getId(), entityOven.getId());
		Used used7 = pFactory.newUsed(activityDecorate.getId(), entityIcing.getId());

		WasAssociatedWith waw1 = pFactory.newWasAssociatedWith(ns.qualifiedName(CORDONBLEU_PREFIX, "CookMix", pFactory),
				activityMix.getId(), agentCook.getId());
		WasAssociatedWith waw2 = pFactory.newWasAssociatedWith(
				ns.qualifiedName(CORDONBLEU_PREFIX, "CookBake", pFactory), activityBake.getId(), agentCook.getId());
		WasAssociatedWith waw3 = pFactory.newWasAssociatedWith(
				ns.qualifiedName(CORDONBLEU_PREFIX, "CookDecorate", pFactory), activityDecorate.getId(),
				agentCook.getId());

		WasStartedBy ws1 = pFactory.newWasStartedBy(ns.qualifiedName(CORDONBLEU_PREFIX, "DecorateAfterBake", pFactory),
				activityDecorate.getId(), activityBake.getId());
		WasStartedBy ws2 = pFactory.newWasStartedBy(ns.qualifiedName(CORDONBLEU_PREFIX, "BakeAfterMix", pFactory),
				activityBake.getId(), activityMix.getId());

		WasGeneratedBy wg = pFactory.newWasGeneratedBy(ns.qualifiedName(CORDONBLEU_PREFIX, "coating", pFactory),
				entityCake.getId(), activityDecorate.getId());

		Document document = pFactory.newDocument();
		document.getStatementOrBundle()
				.addAll(Arrays.asList(new StatementOrBundle[] { entityMilk, entityButter, entityFlour, entitySugar,
						entityIcing, entityCake, entityEggs, entityHandMixer, entityOven, agentCook, activityMix,
						activityBake, activityDecorate, wd1, wd2, used1, used2, used3, used4, used5, used6, used7, waw1,
						waw2, waw3, ws1, ws2, wg }));
		document.setNamespace(ns);
		return document;
	}

	public void doConversions(Document document, String file) {
		InteropFramework intF = new InteropFramework();
		intF.writeDocument(file, document);
		intF.writeDocument(System.out, ProvFormat.PROVN, document);
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
		CookSample little = new CookSample(InteropFramework.newXMLProvFactory());
		little.openingBanner();
		Document document = little.makeDocument();
		little.doConversions(document, "etc/cook.svg");
		little.closingBanner();

	}

}
