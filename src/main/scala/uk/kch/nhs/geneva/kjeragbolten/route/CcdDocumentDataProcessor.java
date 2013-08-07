package uk.kch.nhs.geneva.kjeragbolten.route;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import java.math.BigInteger;

import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;

import javax.activation.DataHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import org.openhealthtools.mdht.uml.cda.AssignedAuthor;
import org.openhealthtools.mdht.uml.cda.AssignedCustodian;
import org.openhealthtools.mdht.uml.cda.Author;
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
import org.openhealthtools.mdht.uml.cda.ccd.ContinuityOfCareDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;
import org.openhealthtools.mdht.uml.hl7.datatypes.BinaryDataEncoding;
import org.openhealthtools.mdht.uml.hl7.datatypes.CE;
import org.openhealthtools.mdht.uml.hl7.datatypes.ED;
import org.openhealthtools.mdht.uml.hl7.datatypes.II;
import org.openhealthtools.mdht.uml.hl7.datatypes.ON;
import org.openhealthtools.mdht.uml.hl7.datatypes.PN;
import org.openhealthtools.mdht.uml.hl7.datatypes.ST;
import org.openhealthtools.mdht.uml.hl7.datatypes.TS;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.kch.nhs.geneva.kjeragbolten.CcdDocumentData;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.AddressListType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.AddressType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.AuditIdentityType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.DistributionEnvelopeType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.DistributionHeaderType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.HandlingSpecType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.HandlingType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.IdentityType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.ManifestItemType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.ManifestType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.PayloadType;
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.PayloadsType;

public class CcdDocumentDataProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		// CDABuilder builder = new CDABuilder();
		Message in = exchange.getIn();
		CcdDocumentData ccdDocumentData = in.getBody(CcdDocumentData.class);

		for (Entry<String, DataHandler> attachment : in.getAttachments()
				.entrySet()) {

			InputStream inputAttachment = attachment.getValue().getDataSource()
					.getInputStream();

			InputStream outputAttachment = Pdf2Tiff
					.convertPdf2Tiff(inputAttachment);
			ContinuityOfCareDocument continuityOfCareDocument = this
					.generateContinuityOfCareDocument(ccdDocumentData,
							outputAttachment);

			String document = this
					.renderContinuityOfCareDocument(continuityOfCareDocument);

			DistributionEnvelopeType d = this
					.createPrimaryCDADistributionEnvelope(document,
							ccdDocumentData.getDestination(),
							"https://10.10.10.10/Acks",
							"urn:nhs-uk:identity:tactix4");

			exchange.getOut().setBody(d);
			// CDA only supports one document/attachment
			outputAttachment.close();
			inputAttachment.close();
			break;
		}
	}

	private String renderContinuityOfCareDocument(
			ContinuityOfCareDocument continuityOfCareDocument) throws Exception {
		StringWriter sw = new StringWriter();
		CDAUtil.save(continuityOfCareDocument, sw);
		return sw.toString();
	}

	private ContinuityOfCareDocument generateContinuityOfCareDocument(
			CcdDocumentData data, InputStream attachment) throws IOException {
		String patientRoleIdRoot = data.getPatientRoleIdRoot();
		String patientRoleId = data.getPatientRoleId();
		String patientGiven = data.getPatientGiven();
		String patientFamily = data.getPatientFamily();
		String patientGenderCode = data.getPatientGender();
		String patientGenderCodeSystem = data.getAdministrativeGenderCode();
		String patientBirthdate = data.getPatientBirthdate();
		PatientRole patientRole = createPatientRole(patientRoleIdRoot,
				patientRoleId, patientGiven, patientFamily, patientGenderCode,
				patientGenderCodeSystem, patientBirthdate);

		String effectiveTimeValue = data.getEffectiveTimeValue();

		TS authorDocumentEffectiveDate = CdaBuilderFactory
				.createTs(effectiveTimeValue);
		// Healthtools don't allow same node to exist in 2 places
		TS documentEffectiveDate = CdaBuilderFactory
				.createTs(effectiveTimeValue);

		String authorGiven = data.getAuthorGiven();
		String authorFamily = data.getAuthorFamily();
		Author author = createAuthor(authorGiven, authorFamily,
				authorDocumentEffectiveDate);

		String recipientFamily = data.getRecipientFamily();
		String recipientGiven = data.getRecipientGiven();
		String receivedOrgainisationName = data.getReceivedOrgainisationName();
		InformationRecipient recipient = createRecipient(recipientFamily,
				recipientGiven, receivedOrgainisationName);

		String organisationName = data.getOrganisationName();
		String infrastructureRootId = data.getInfrastructureRootId();
		Custodian custodian = createCustodian(organisationName,
				infrastructureRootId);

		// Add attachment

		String mediaType = "image/tiff";
		Component2 component = createComponent(mediaType, attachment);

		String title = data.getTitle();
		ST documentTitle = CdaBuilderFactory.createSt(title);

		String root = data.getRoot();
		String extension = data.getExtension();
		II docId = CdaBuilderFactory.createIi(root, extension);

		ContinuityOfCareDocument ccdDocument = CdaBuilderFactory
				.createContinuityOfCareDocument(documentTitle,
						documentEffectiveDate, author, recipient, custodian,
						docId, patientRole);
		ccdDocument.setComponent(component);

		return ccdDocument;
	}

	public Component2 createComponent(String mediaType, InputStream inputStream)
			throws IOException {
		String base64 = new String(Base64.encodeBase64(IOUtils
				.toByteArray(inputStream)));

		ED nonXmlData = CdaBuilderFactory.createCustodian(mediaType,
				BinaryDataEncoding.B64, base64);

		NonXMLBody nonXMLBody = CdaBuilderFactory.createNonXmlBody(nonXmlData);

		// return document as string

		return CdaBuilderFactory.createComponent2(nonXMLBody);
	}

	public Custodian createCustodian(String organisationName,
			String infrastructureRootId) {
		ON custOrgName = CdaBuilderFactory.createOn(organisationName);

		InfrastructureRootTypeId rootId = CdaBuilderFactory
				.createInfrastructureRootTypeId(infrastructureRootId);

		CustodianOrganization custOrg = CdaBuilderFactory
				.createCustodianOrganization(custOrgName, rootId);

		AssignedCustodian assignedCustodian = CdaBuilderFactory
				.createAssignedCustodian(custOrg);

		Custodian custodian = CdaBuilderFactory
				.createCustodian(assignedCustodian);
		return custodian;
	}

	public InformationRecipient createRecipient(String recipientFamily,
			String recipientGiven, String receivedOrgainisationName) {
		Person intendedPerson = CdaBuilderFactory.createPerson();

		PN intendedPersonName = CdaBuilderFactory.createPn(recipientGiven,
				recipientFamily);

		ON orgName = CdaBuilderFactory.createOn(receivedOrgainisationName);

		Organization org = CdaBuilderFactory.createOrganization();

		IntendedRecipient intendedRecipient = CdaBuilderFactory
				.createIntendedRecipient(intendedPerson, org,
						intendedPersonName, orgName);

		InformationRecipient recipient = CdaBuilderFactory
				.createInformationRecipient(intendedRecipient);
		return recipient;
	}

	public Author createAuthor(String authorGiven, String authorFamily,
			TS documentEffectiveDate) {
		Person authorPerson = CdaBuilderFactory.createPerson();
		AssignedAuthor assignedAuthor = CdaBuilderFactory
				.createAssignedAuthor(authorPerson);

		PN authorName = CdaBuilderFactory.createPn(authorGiven, authorFamily);

		Author author = CdaBuilderFactory.createAuthor(documentEffectiveDate,
				assignedAuthor, authorName);
		return author;
	}

	public PatientRole createPatientRole(String patientRoleIdRoot,
			String patientRoleId, String patientGiven, String patientFamily,
			String patientGenderCode, String patientGenderCodeSystem,
			String patientBirthdate) {
		II id = CdaBuilderFactory.createIi(patientRoleIdRoot, patientRoleId);

		// create a patient object and add it to patient role

		PN patientName = CdaBuilderFactory
				.createPn(patientGiven, patientFamily);

		CE patientGender = CdaBuilderFactory.createCe(patientGenderCode,
				patientGenderCodeSystem);

		TS patientDateOfBirth = CdaBuilderFactory.createTs(patientBirthdate);

		Patient patient = CdaBuilderFactory.createPatient(patientName,
				patientGender, patientDateOfBirth);

		PatientRole patientRole = CdaBuilderFactory.createPatientRole(id,
				patient);

		return patientRole;
	}

	public DistributionEnvelopeType createPrimaryCDADistributionEnvelope(
			String payload, String desination, String senderAddress,
			String auditIdentity) throws SAXException, IOException,
			ParserConfigurationException {
		HandlingType handling = new HandlingType();
		HandlingSpecType spec1 = new HandlingSpecType();
		spec1.setKey("urn:nhs-itk:ns:201005:ackrequested");
		spec1.setValue("false");
		HandlingSpecType spec2 = new HandlingSpecType();
		spec2.setKey("urn:nhs-itk:ns:201005:interaction");
		spec2.setValue("urn:nhs-itk:interaction:primaryRecipientNonCodedCDADocument-v2-0");
		handling.getSpec().add(spec1);
		handling.getSpec().add(spec2);

		return createDistributionEnvelope(
				"urn:nhs-en:profile:nonCodedCDADocument-v2-0",
				"urn:nhs-itk:services:201005:SendCDADocument-v2-0", handling,
				desination, payload, senderAddress, auditIdentity);
	}

	public static DistributionEnvelopeType createCopyCDADistributionEnvelope(
			String payload, String destination, String senderAddress,
			String auditIdentity) throws SAXException, IOException,
			ParserConfigurationException {
		HandlingType handling = new HandlingType();
		HandlingSpecType spec1 = new HandlingSpecType();
		spec1.setKey("urn:nhs-itk:ns:201005:ackrequested");
		spec1.setValue("true");
		HandlingSpecType spec2 = new HandlingSpecType();
		spec2.setKey("urn:nhs-itk:ns:201005:interaction");
		spec2.setValue("urn:nhs-itk:interaction:copyRecipientNonCodedCDADocument-v2-0");
		handling.getSpec().add(spec1);
		handling.getSpec().add(spec2);

		return createDistributionEnvelope(
				"urn:nhs-en:profile:nonCodedCDADocument-v2-0",
				"urn:nhs-itk:services:201005:SendCDADocument-v2-0", handling,
				destination, payload, senderAddress, auditIdentity);
	}

	public static DistributionEnvelopeType createInfrastructureAckDistributionEnvelope(
			String payload, String destination, String senderAddress,
			String auditIdentity) throws SAXException, IOException,
			ParserConfigurationException {
		HandlingType handling = new HandlingType();
		HandlingSpecType spec = new HandlingSpecType();
		spec.setKey("urn:nhs-itk:ns:201005:interaction");
		spec.setValue("urn:nhs-itk:interaction:ITKInfrastructureAcknowledgement-v1-0");
		handling.getSpec().add(spec);
		return createDistributionEnvelope(
				"urn:nhs-en:profile:ITKInfrastructureAcknowledgement-v1-0",
				"urn:nhs-itk:services:201005:SendInfrastructureAck-v1-0",
				handling, destination, payload, senderAddress, auditIdentity);
	}

	private static DistributionEnvelopeType createDistributionEnvelope(
			String profileID,
			String service,
			uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.HandlingType handlingType,
			String destination, String stringPayload, String senderAddress,
			String auditIdentity) throws SAXException, IOException,
			ParserConfigurationException {

		String headerUuid = UUID.randomUUID().toString().toUpperCase();
		Random r = new Random();
		char c = (char) (r.nextInt(26) + 'A');
		String payloadId = String.format("%c%s", c, headerUuid);

		IdentityType id = new IdentityType();
		id.setUri(auditIdentity);

		AuditIdentityType ai = new AuditIdentityType();
		ai.getId().add(id);

		AddressType address = new AddressType();
		address.setUri("urn:nhs-uk:addressing:ods:" + destination);

		AddressListType addressList = new AddressListType();
		addressList.getAddress().add(address);

		AddressType saddress = new AddressType();
		saddress.setUri(senderAddress);

		ManifestItemType item = new ManifestItemType();
		item.setMimetype("text/xml");
		item.setId(payloadId);
		item.setProfileid(profileID);

		ManifestType manifest = new ManifestType();
		manifest.setCount(BigInteger.ONE);
		manifest.getManifestitem().add(item);

		DistributionHeaderType header = new DistributionHeaderType();
		header.setTrackingid(headerUuid);
		header.setAuditIdentity(ai);
		header.setService(service);
		header.setAddresslist(addressList);
		header.setSenderAddress(saddress);
		header.setHandlingSpecification(handlingType);
		header.setManifest(manifest);

		DistributionEnvelopeType d = new DistributionEnvelopeType();
		d.setHeader(header);

		// build xml data from string and insert into payload

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.parse(new InputSource(new StringReader(
				stringPayload)));

		PayloadType payload = new PayloadType();
		payload.setId(payloadId);
		payload.getContent().add(doc.getDocumentElement());

		PayloadsType payloads = new PayloadsType();
		payloads.setCount(BigInteger.ONE);
		payloads.getPayload().add(payload);
		d.setPayloads(payloads);

		return d;

	}
}
