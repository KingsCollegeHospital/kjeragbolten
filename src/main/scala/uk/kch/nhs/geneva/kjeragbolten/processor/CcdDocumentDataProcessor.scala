package uk.kch.nhs.geneva.kjeragbolten.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import java.math.BigInteger;

import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;

import java.net.URLConnection

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
import uk.kch.nhs.geneva.kjeragbolten.util.CdaBuilderFactory;
import uk.kch.nhs.geneva.kjeragbolten.util.Pdf2Tiff;
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
import uk.kch.nhs.geneva.kjeragbolten.generated.datatypes.itk.HandlingType;

import scala.collection.JavaConversions._

class CcdDocumentDataProcessor extends Processor {

  @Override
  @throws(classOf[Exception])
  def process(exchange: Exchange) = {

    val in = exchange.getIn();
    val ccdDocumentData = in.getBody(classOf[CcdDocumentData]);
    val attachments = in.getAttachments()
      .entrySet();

    for (attachment <- attachments) {

      val inputAttachment = attachment.getValue().getDataSource()
        .getInputStream();

      val attachmentName = attachment.getKey();

      val outputAttachment = Pdf2Tiff
        .convertPdf2Tiff(inputAttachment, attachmentName);
      val outputAttachmentName = attachmentName.split("\\.")(0) + ".tiff"

      val continuityOfCareDocument = this
        .generateContinuityOfCareDocument(ccdDocumentData,
          outputAttachmentName, outputAttachment);

      val document = this
        .renderContinuityOfCareDocument(continuityOfCareDocument);

      val d = this
        .createPrimaryCDADistributionEnvelope(document,
          ccdDocumentData.Destination,
          "https://10.10.10.10/Acks",
          "urn:nhs-uk:identity:tactix4");

      exchange.getOut().setBody(d);
      outputAttachment.close();
      inputAttachment.close();
    }
  }

  @throws(classOf[Exception])
  private def renderContinuityOfCareDocument(
    continuityOfCareDocument: ContinuityOfCareDocument): String = {
    val sw = new StringWriter();
    CDAUtil.save(continuityOfCareDocument, sw);
    return sw.toString();
  }

  @throws(classOf[IOException])
  private def generateContinuityOfCareDocument(
    data: CcdDocumentData, attachmentName: String, attachment: InputStream): ContinuityOfCareDocument = {
    val patientRole = createPatientRole(data.PatientRoleIdRoot,
      data.PatientRoleId, data.PatientGiven, data.PatientFamily, data.PatientGender,
      data.AdministrativeGenderCode, data.PatientBirthdate);

    val effectiveTimeValue = data.EffectiveTimeValue;

    val authorDocumentEffectiveDate = CdaBuilderFactory
      .createTs(effectiveTimeValue);
    // Healthtools don't allow same node to exist in 2 places
    val documentEffectiveDate = CdaBuilderFactory
      .createTs(effectiveTimeValue);

    val author = createAuthor(data.AuthorGiven, data.AuthorFamily,
      authorDocumentEffectiveDate);

    val recipient = createRecipient(data.RecipientFamily,
      data.RecipientGiven, data.ReceivedOrgainisationName);

    val custodian = createCustodian(data.OrganisationName,
      data.InfrastructureRootId);

    // Add attachment

    val mediaType = getMediaType(attachmentName);
    val component = createComponent(mediaType, attachment);

    val documentTitle = CdaBuilderFactory.createSt(data.Title);

    val docId = CdaBuilderFactory.createIi(data.Root, data.Extension);

    val ccdDocument = CdaBuilderFactory
      .createContinuityOfCareDocument(documentTitle,
        documentEffectiveDate, author, recipient, custodian,
        docId, patientRole);
    ccdDocument.setComponent(component);

    return ccdDocument;
  }

  def getMediaType(attachmentName: String): String = {
    URLConnection.guessContentTypeFromName(attachmentName)
  }

  @throws(classOf[IOException])
  def createComponent(mediaType: String, inputStream: InputStream): Component2 = {
    val base64 = new String(Base64.encodeBase64(IOUtils
      .toByteArray(inputStream)));

    val nonXmlData = CdaBuilderFactory.createCustodian(mediaType,
      BinaryDataEncoding.B64, base64);

    val nonXMLBody = CdaBuilderFactory.createNonXmlBody(nonXmlData);

    // return document as string

    return CdaBuilderFactory.createComponent2(nonXMLBody);
  }

  def createCustodian(organisationName: String,
    infrastructureRootId: String): Custodian = {
    val custOrgName = CdaBuilderFactory.createOn(organisationName);

    val rootId = CdaBuilderFactory
      .createInfrastructureRootTypeId(infrastructureRootId);

    val custOrg = CdaBuilderFactory
      .createCustodianOrganization(custOrgName, rootId);

    val assignedCustodian = CdaBuilderFactory
      .createAssignedCustodian(custOrg);

    val custodian = CdaBuilderFactory
      .createCustodian(assignedCustodian);
    return custodian;
  }

  def createRecipient(recipientFamily: String,
    recipientGiven: String, receivedOrgainisationName: String): InformationRecipient = {
    val intendedPerson = CdaBuilderFactory.createPerson();

    val intendedPersonName = CdaBuilderFactory.createPn(recipientGiven,
      recipientFamily);

    val orgName = CdaBuilderFactory.createOn(receivedOrgainisationName);

    val org = CdaBuilderFactory.createOrganization;

    val intendedRecipient = CdaBuilderFactory
      .createIntendedRecipient(intendedPerson, org,
        intendedPersonName, orgName);

    val recipient = CdaBuilderFactory
      .createInformationRecipient(intendedRecipient);
    return recipient;
  }

  def createAuthor(authorGiven: String, authorFamily: String,
    documentEffectiveDate: TS): Author = {
    val authorPerson = CdaBuilderFactory.createPerson();
    val assignedAuthor = CdaBuilderFactory
      .createAssignedAuthor(authorPerson);

    val authorName = CdaBuilderFactory.createPn(authorGiven, authorFamily);

    val author = CdaBuilderFactory.createAuthor(documentEffectiveDate,
      assignedAuthor, authorName);
    return author;
  }

  def createPatientRole(patientRoleIdRoot: String,
    patientRoleId: String, patientGiven: String, patientFamily: String,
    patientGenderCode: String, patientGenderCodeSystem: String,
    patientBirthdate: String): PatientRole = {
    val id = CdaBuilderFactory.createIi(patientRoleIdRoot, patientRoleId);

    // create a patient object and add it to patient role

    val patientName = CdaBuilderFactory
      .createPn(patientGiven, patientFamily);

    val patientGender = CdaBuilderFactory.createCe(patientGenderCode,
      patientGenderCodeSystem);

    val patientDateOfBirth = CdaBuilderFactory.createTs(patientBirthdate);

    val patient = CdaBuilderFactory.createPatient(patientName,
      patientGender, patientDateOfBirth);

    val patientRole = CdaBuilderFactory.createPatientRole(id,
      patient);

    return patientRole;
  }

  @throws(classOf[SAXException])
  @throws(classOf[IOException])
  @throws(classOf[ParserConfigurationException])
  def createPrimaryCDADistributionEnvelope(
    payload: String, desination: String, senderAddress: String,
    auditIdentity: String): DistributionEnvelopeType = {
    val handling = new HandlingType();
    val spec1 = new HandlingSpecType();
    spec1.setKey("urn:nhs-itk:ns:201005:ackrequested");
    spec1.setValue("false");
    val spec2 = new HandlingSpecType();
    spec2.setKey("urn:nhs-itk:ns:201005:interaction");
    spec2.setValue("urn:nhs-itk:interaction:primaryRecipientNonCodedCDADocument-v2-0");
    handling.getSpec().add(spec1);
    handling.getSpec().add(spec2);

    return createDistributionEnvelope(
      "urn:nhs-en:profile:nonCodedCDADocument-v2-0",
      "urn:nhs-itk:services:201005:SendCDADocument-v2-0", handling,
      desination, payload, senderAddress, auditIdentity);
  }

  @throws(classOf[SAXException]) @throws(classOf[IOException]) @throws(classOf[ParserConfigurationException])
  def createCopyCDADistributionEnvelope(
    payload: String, destination: String, senderAddress: String,
    auditIdentity: String): DistributionEnvelopeType = {
    val handling = new HandlingType();
    val spec1 = new HandlingSpecType();
    spec1.setKey("urn:nhs-itk:ns:201005:ackrequested");
    spec1.setValue("true");
    val spec2 = new HandlingSpecType();
    spec2.setKey("urn:nhs-itk:ns:201005:interaction");
    spec2.setValue("urn:nhs-itk:interaction:copyRecipientNonCodedCDADocument-v2-0");
    handling.getSpec().add(spec1);
    handling.getSpec().add(spec2);

    return createDistributionEnvelope(
      "urn:nhs-en:profile:nonCodedCDADocument-v2-0",
      "urn:nhs-itk:services:201005:SendCDADocument-v2-0", handling,
      destination, payload, senderAddress, auditIdentity);
  }

  @throws(classOf[SAXException]) @throws(classOf[IOException]) @throws(classOf[ParserConfigurationException])
  def createInfrastructureAckDistributionEnvelope(
    payload: String, destination: String, senderAddress: String,
    auditIdentity: String): DistributionEnvelopeType = {
    val handling = new HandlingType();
    val spec = new HandlingSpecType();
    spec.setKey("urn:nhs-itk:ns:201005:interaction");
    spec.setValue("urn:nhs-itk:interaction:ITKInfrastructureAcknowledgement-v1-0");
    handling.getSpec().add(spec);
    return createDistributionEnvelope(
      "urn:nhs-en:profile:ITKInfrastructureAcknowledgement-v1-0",
      "urn:nhs-itk:services:201005:SendInfrastructureAck-v1-0",
      handling, destination, payload, senderAddress, auditIdentity);
  }

  @throws(classOf[SAXException]) @throws(classOf[IOException]) @throws(classOf[ParserConfigurationException])
  def createDistributionEnvelope(
    profileID: String,
    service: String,
    handlingType: HandlingType,
    destination: String, stringPayload: String, senderAddress: String,
    auditIdentity: String): DistributionEnvelopeType = {

    val headerUuid = UUID.randomUUID().toString().toUpperCase();
    val r = new Random();
    val c = (r.nextInt(26) + 65).toChar.toString;
    val payloadId = String.format("%s%s", c, headerUuid);

    val id = new IdentityType();
    id.setUri(auditIdentity);

    val ai = new AuditIdentityType();
    ai.getId().add(id);

    val address = new AddressType();
    address.setUri("urn:nhs-uk:addressing:ods:" + destination);

    val addressList = new AddressListType();
    addressList.getAddress().add(address);

    val saddress = new AddressType();
    saddress.setUri(senderAddress);

    val item = new ManifestItemType();
    item.setMimetype("text/xml");
    item.setId(payloadId);
    item.setProfileid(profileID);

    val manifest = new ManifestType();
    manifest.setCount(BigInteger.ONE);
    manifest.getManifestitem().add(item);

    val header = new DistributionHeaderType();
    header.setTrackingid(headerUuid);
    header.setAuditIdentity(ai);
    header.setService(service);
    header.setAddresslist(addressList);
    header.setSenderAddress(saddress);
    header.setHandlingSpecification(handlingType);
    header.setManifest(manifest);

    val d = new DistributionEnvelopeType();
    d.setHeader(header);

    // build xml data from string and insert into payload

    val factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    val docBuilder = factory.newDocumentBuilder();
    val doc = docBuilder.parse(new InputSource(new StringReader(
      stringPayload)));

    val payload = new PayloadType();
    payload.setId(payloadId);
    payload.getContent().add(doc.getDocumentElement());

    val payloads = new PayloadsType();
    payloads.setCount(BigInteger.ONE);
    payloads.getPayload().add(payload);
    d.setPayloads(payloads);

    return d;

  }
}
