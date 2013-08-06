package uk.kch.nhs.geneva.kjeragbolten.route;

import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedCustodian;
import org.openhealthtools.mdht.uml.cda.Author;
import org.openhealthtools.mdht.uml.cda.CDAFactory;
import org.openhealthtools.mdht.uml.cda.Component2;
import org.openhealthtools.mdht.uml.cda.Custodian;
import org.openhealthtools.mdht.uml.cda.CustodianOrganization;
import org.openhealthtools.mdht.uml.cda.InformationRecipient;
import org.openhealthtools.mdht.uml.cda.InfrastructureRootTypeId;
import org.openhealthtools.mdht.uml.cda.IntendedRecipient;
import org.openhealthtools.mdht.uml.cda.NonXMLBody;
import org.openhealthtools.mdht.uml.cda.Organization;
import org.openhealthtools.mdht.uml.cda.Patient;
import org.openhealthtools.mdht.uml.cda.PatientRole;
import org.openhealthtools.mdht.uml.cda.Person;
import org.openhealthtools.mdht.uml.cda.ccd.CCDFactory;
import org.openhealthtools.mdht.uml.cda.ccd.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.hl7.datatypes.BinaryDataEncoding;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory;
import org.openhealthtools.mdht.uml.hl7.datatypes.ED;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;

public class CdaBuilderFactory {

	public static PatientRole createPatientRole(II id, Patient patient) {
		PatientRole patientRole = CDAFactory.eINSTANCE.createPatientRole();
		patientRole.getIds().add(id);
		patientRole.setPatient(patient);
		return patientRole;
	}

	public static ContinuityOfCareDocument createContinuityOfCareDocument(
			ST title, TS effectiveTime, Author author,
			InformationRecipient informationRecipient, Custodian custodian,
			Component2 component2, II id, PatientRole patientRole) {
		ContinuityOfCareDocument ccdDocument = CCDFactory.eINSTANCE
				.createContinuityOfCareDocument().init();
		ccdDocument.setTitle(title);
		ccdDocument.setId(id);
		ccdDocument.setEffectiveTime(effectiveTime);
		ccdDocument.getAuthors().add(author);
		ccdDocument.getInformationRecipients().add(informationRecipient);
		ccdDocument.setCustodian(custodian);
		ccdDocument.setComponent(component2);
		ccdDocument.addPatientRole(patientRole);
		return ccdDocument;
	}

	public static Component2 createComponent2(NonXMLBody nonXmlBody) {
		Component2 component = CDAFactory.eINSTANCE.createComponent2();
		component.setNonXMLBody(nonXmlBody);
		return component;
	}

	public static ED createCustodian(String mediaType,
			BinaryDataEncoding encoding, String text) {
		ED nonXmlData = DatatypesFactory.eINSTANCE.createED();
		nonXmlData.setMediaType(mediaType);

		nonXmlData.setRepresentation(encoding);
		nonXmlData.addText(text);
		return nonXmlData;
	}

	public static NonXMLBody createNonXmlBody(ED ed) {
		NonXMLBody nonXMLBody = CDAFactory.eINSTANCE.createNonXMLBody();
		nonXMLBody.setText(ed);
		return nonXMLBody;
	}

	public static Custodian createCustodian(AssignedCustodian assignedCustodian) {
		Custodian custodian = CDAFactory.eINSTANCE.createCustodian();
		custodian.setAssignedCustodian(assignedCustodian);
		return custodian;
	}

	public static AssignedCustodian createAssignedCustodian(
			CustodianOrganization representedCustodianOrganization) {
		AssignedCustodian assignedCustodian = CDAFactory.eINSTANCE
				.createAssignedCustodian();
		assignedCustodian
				.setRepresentedCustodianOrganization(representedCustodianOrganization);
		return assignedCustodian;
	}

	public static CustodianOrganization createCustodianOrganization(ON on,
			InfrastructureRootTypeId infrastructureRootTypeId) {
		CustodianOrganization custOrg = CDAFactory.eINSTANCE
				.createCustodianOrganization();
		custOrg.setName(on);
		custOrg.setTypeId(infrastructureRootTypeId);
		return custOrg;
	}

	public static InfrastructureRootTypeId createInfrastructureRootTypeId(
			String root) {
		InfrastructureRootTypeId rootId = CDAFactory.eINSTANCE
				.createInfrastructureRootTypeId();

		rootId.setRoot(root);
		return rootId;
	}

	public static InformationRecipient createInformationRecipient(
			IntendedRecipient intendedRecipient) {
		InformationRecipient recipient = CDAFactory.eINSTANCE
				.createInformationRecipient();
		recipient.setIntendedRecipient(intendedRecipient);
		return recipient;
	}

	public static IntendedRecipient createIntendedRecipient(Person person,
			Organization organization, PN pn, ON on) {
		IntendedRecipient intendedRecipient = CDAFactory.eINSTANCE
				.createIntendedRecipient();
		intendedRecipient.setInformationRecipient(person);
		intendedRecipient.setReceivedOrganization(organization);
		intendedRecipient.getInformationRecipient().getNames().add(pn);
		intendedRecipient.getReceivedOrganization().getNames().add(on);
		return intendedRecipient;
	}

	public static Organization createOrganization() {
		Organization org = CDAFactory.eINSTANCE.createOrganization();
		return org;
	}

	public static ON createOn(String text) {
		ON orgName = DatatypesFactory.eINSTANCE.createON();

		orgName.addText(text);
		return orgName;
	}

	public static Author createAuthor(TS ts, AssignedAuthor assignedAuthor,
			PN pn) {
		Author author = CDAFactory.eINSTANCE.createAuthor();
		author.setTime(ts);
		author.setAssignedAuthor(assignedAuthor);
		author.getAssignedAuthor().getAssignedPerson().getNames().add(pn);
		return author;
	}

	public static AssignedAuthor createAssignedAuthor(Person person) {
		AssignedAuthor aAuthor = CDAFactory.eINSTANCE.createAssignedAuthor();
		aAuthor.setAssignedPerson(person);
		return aAuthor;
	}

	public static Person createPerson() {
		Person authorPerson = CDAFactory.eINSTANCE.createPerson();
		return authorPerson;
	}

	public static Patient createPatient(PN pn, CE ce, TS ts) {
		Patient patient = CDAFactory.eINSTANCE.createPatient();
		patient.getNames().add(pn);
		patient.setAdministrativeGenderCode(ce);
		patient.setBirthTime(ts);
		return patient;
	}

	public static CE createCe(String code, String codeSystem) {
		CE administrativeGenderCode = DatatypesFactory.eINSTANCE.createCE();
		administrativeGenderCode.setCode(code); // M F
												// or
												// U
		// use HL7 rather than Snomed

		administrativeGenderCode.setCodeSystem(codeSystem);
		return administrativeGenderCode;
	}

	public static PN createPn(String given, String family) {
		PN name = DatatypesFactory.eINSTANCE.createPN();

		name.addGiven(given);

		name.addFamily(family);
		return name;
	}

	public static II createIi() {
		II docId = DatatypesFactory.eINSTANCE.createII();
		return docId;
	}

	public static II createIi(String root, String extension) {
		II id = createIi();
		id.setRoot(root);
		id.setExtension(extension);
		return id;
	}

	public static TS createTs(String value) {
		TS effectiveTime = DatatypesFactory.eINSTANCE.createTS();
		effectiveTime.setValue(value);
		return effectiveTime;
	}

	public static ST createSt(String text) {
		ST title = DatatypesFactory.eINSTANCE.createST();
		title.addText(text);
		return title;
	}

}
