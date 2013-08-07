package uk.kch.nhs.geneva.kjeragbolten.util

import org.openhealthtools.mdht.uml.cda.AssignedAuthor
import org.openhealthtools.mdht.uml.cda.AssignedCustodian
import org.openhealthtools.mdht.uml.cda.Author
import org.openhealthtools.mdht.uml.cda.CDAFactory
import org.openhealthtools.mdht.uml.cda.Component2
import org.openhealthtools.mdht.uml.cda.Custodian
import org.openhealthtools.mdht.uml.cda.CustodianOrganization
import org.openhealthtools.mdht.uml.cda.InformationRecipient
import org.openhealthtools.mdht.uml.cda.InfrastructureRootTypeId
import org.openhealthtools.mdht.uml.cda.IntendedRecipient
import org.openhealthtools.mdht.uml.cda.NonXMLBody
import org.openhealthtools.mdht.uml.cda.Organization
import org.openhealthtools.mdht.uml.cda.Patient
import org.openhealthtools.mdht.uml.cda.PatientRole
import org.openhealthtools.mdht.uml.cda.Person
import org.openhealthtools.mdht.uml.cda.ccd.CCDFactory
import org.openhealthtools.mdht.uml.cda.ccd.ContinuityOfCareDocument
import org.openhealthtools.mdht.uml.hl7.datatypes.BinaryDataEncoding
import org.openhealthtools.mdht.uml.hl7.datatypes.CE
import org.openhealthtools.mdht.uml.hl7.datatypes.DatatypesFactory
import org.openhealthtools.mdht.uml.hl7.datatypes.ED
import org.openhealthtools.mdht.uml.hl7.datatypes.II
import org.openhealthtools.mdht.uml.hl7.datatypes.ON
import org.openhealthtools.mdht.uml.hl7.datatypes.PN
import org.openhealthtools.mdht.uml.hl7.datatypes.ST
import org.openhealthtools.mdht.uml.hl7.datatypes.TS

object CdaBuilderFactory {

  def createPatientRole(id: II, patient: Patient): PatientRole = {
    val patientRole = CDAFactory.eINSTANCE.createPatientRole()
    patientRole.getIds().add(id)
    patientRole.setPatient(patient)
    patientRole
  }

  def createContinuityOfCareDocument(
    title: ST, effectiveTime: TS, author: Author,
    informationRecipient: InformationRecipient, custodian: Custodian,
    id: II, patientRole: PatientRole): ContinuityOfCareDocument = {
    val ccdDocument = CCDFactory.eINSTANCE
      .createContinuityOfCareDocument().init()
    ccdDocument.setTitle(title)
    ccdDocument.setId(id)
    ccdDocument.setEffectiveTime(effectiveTime)
    ccdDocument.getAuthors().add(author)
    ccdDocument.getInformationRecipients().add(informationRecipient)
    ccdDocument.setCustodian(custodian)
    ccdDocument.addPatientRole(patientRole)
    ccdDocument
  }

  def createComponent2(nonXmlBody: NonXMLBody): Component2 = {
    val component = CDAFactory.eINSTANCE.createComponent2()
    component.setNonXMLBody(nonXmlBody)
    component
  }

  def createCustodian(mediaType: String,
    encoding: BinaryDataEncoding, text: String): ED = {
    val nonXmlData = DatatypesFactory.eINSTANCE.createED()
    nonXmlData.setMediaType(mediaType)

    nonXmlData.setRepresentation(encoding)
    nonXmlData.addText(text)
    nonXmlData
  }

  def createNonXmlBody(ed: ED): NonXMLBody = {
    val nonXMLBody = CDAFactory.eINSTANCE.createNonXMLBody()
    nonXMLBody.setText(ed)
    nonXMLBody
  }

  def createCustodian(assignedCustodian: AssignedCustodian): Custodian = {
    val custodian = CDAFactory.eINSTANCE.createCustodian()
    custodian.setAssignedCustodian(assignedCustodian)
    custodian
  }

  def createAssignedCustodian(
    representedCustodianOrganization: CustodianOrganization): AssignedCustodian = {
    val assignedCustodian = CDAFactory.eINSTANCE
      .createAssignedCustodian()
    assignedCustodian
      .setRepresentedCustodianOrganization(representedCustodianOrganization)
    assignedCustodian
  }

  def createCustodianOrganization(on: ON,
    infrastructureRootTypeId: InfrastructureRootTypeId): CustodianOrganization = {
    val custOrg = CDAFactory.eINSTANCE
      .createCustodianOrganization()
    custOrg.setName(on)
    custOrg.setTypeId(infrastructureRootTypeId)
    custOrg
  }

  def createInfrastructureRootTypeId(
    root: String): InfrastructureRootTypeId = {
    val rootId = CDAFactory.eINSTANCE
      .createInfrastructureRootTypeId()

    rootId.setRoot(root)
    rootId
  }

  def createInformationRecipient(
    intendedRecipient: IntendedRecipient): InformationRecipient = {
    val recipient = CDAFactory.eINSTANCE
      .createInformationRecipient()
    recipient.setIntendedRecipient(intendedRecipient)
    recipient
  }

  def createIntendedRecipient(person: Person,
    organization: Organization, pn: PN, on: ON): IntendedRecipient = {
    val intendedRecipient = CDAFactory.eINSTANCE
      .createIntendedRecipient()
    intendedRecipient.setInformationRecipient(person)
    intendedRecipient.setReceivedOrganization(organization)
    intendedRecipient.getInformationRecipient().getNames().add(pn)
    intendedRecipient.getReceivedOrganization().getNames().add(on)
    intendedRecipient
  }

  def createOrganization: Organization = {
    val org = CDAFactory.eINSTANCE.createOrganization()
    org
  }

  def createOn(text: String): ON = {
    val orgName = DatatypesFactory.eINSTANCE.createON()
    orgName.addText(text)
    orgName
  }

  def createAuthor(ts: TS, assignedAuthor: AssignedAuthor,
    pn: PN): Author = {
    val author = CDAFactory.eINSTANCE.createAuthor()
    author.setTime(ts)
    author.setAssignedAuthor(assignedAuthor)
    author.getAssignedAuthor().getAssignedPerson().getNames().add(pn)
    author
  }

  def createAssignedAuthor(person: Person): AssignedAuthor = {
    val aAuthor = CDAFactory.eINSTANCE.createAssignedAuthor()
    aAuthor.setAssignedPerson(person)
    aAuthor
  }

  def createPerson(): Person = {
    val authorPerson = CDAFactory.eINSTANCE.createPerson()
    authorPerson
  }

  def createPatient(pn: PN, ce: CE, ts: TS): Patient = {
    val patient = CDAFactory.eINSTANCE.createPatient()
    patient.getNames().add(pn)
    patient.setAdministrativeGenderCode(ce)
    patient.setBirthTime(ts)
    patient
  }

  def createCe(code: String, codeSystem: String): CE = {
    val administrativeGenderCode = DatatypesFactory.eINSTANCE.createCE()
    administrativeGenderCode.setCode(code)

    administrativeGenderCode.setCodeSystem(codeSystem)
    administrativeGenderCode
  }

  def createPn(given: String, family: String): PN = {
    val name = DatatypesFactory.eINSTANCE.createPN()

    name.addGiven(given)

    name.addFamily(family)
    name
  }

  def createIi(): II = {
    DatatypesFactory.eINSTANCE.createII()
  }

  def createIi(root: String, extension: String): II = {
    val id = createIi()
    id.setRoot(root)
    id.setExtension(extension)
    id
  }

  def createTs(value: String): TS = {
    val effectiveTime = DatatypesFactory.eINSTANCE.createTS()
    effectiveTime.setValue(value)
    effectiveTime
  }

  def createSt(text: String): ST = {
    val title = DatatypesFactory.eINSTANCE.createST()
    title.addText(text)
    title
  }

}
