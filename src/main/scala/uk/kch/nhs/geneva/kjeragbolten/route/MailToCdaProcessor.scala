package uk.kch.nhs.geneva.kjeragbolten.route

import org.apache.camel.Processor
import org.apache.camel.Exchange
import uk.kch.nhs.geneva.kjeragbolten.CcdDocumentData
import java.text.SimpleDateFormat

class MailToCdaProcessor extends Processor {

  def process(exchange: Exchange): Unit = {
    val previousBody = exchange.getIn().getBody(classOf[String])
    exchange.getIn().setBody(parserEprIdentSegment(previousBody))
  }

  def getDemoMap(): CcdDocumentData = {
    val ccdDocumentData = new CcdDocumentData();
    ccdDocumentData.setPatientRoleId("9999999999");
    ccdDocumentData
      .setTitle("Letter from Kings College Hospital Renal Department");
    ccdDocumentData.setPatientFamily("Jones");
    ccdDocumentData.setPatientGender("M");
    ccdDocumentData.setPatientBirthdate("19320924");
    ccdDocumentData.setPatientGiven("Tom");
    ccdDocumentData.setAuthorGiven("Hugh");
    ccdDocumentData.setAuthorFamily("Cairns");
    ccdDocumentData.setEffectiveTimeValue("20130316");
    ccdDocumentData.setRecipientFamily("Bunny");
    ccdDocumentData.setRecipientGiven("Buggs");
    ccdDocumentData.setReceivedOrgainisationName("The Good Health Clinic");
    ccdDocumentData.setOrganisationName("Kings College Hospital");
    ccdDocumentData.setDestination("G85001");

    return ccdDocumentData;

  }
  def getEmptyDemoMap(): CcdDocumentData = {
    val ccdDocumentData = new CcdDocumentData();
    ccdDocumentData.setPatientRoleId("");
    ccdDocumentData
      .setTitle("");
    ccdDocumentData.setPatientFamily("");
    ccdDocumentData.setPatientGender("");
    ccdDocumentData.setPatientBirthdate("");
    ccdDocumentData.setPatientGiven("");
    ccdDocumentData.setAuthorGiven("");
    ccdDocumentData.setAuthorFamily("");
    ccdDocumentData.setEffectiveTimeValue("");
    ccdDocumentData.setRecipientFamily("");
    ccdDocumentData.setRecipientGiven("");
    ccdDocumentData.setReceivedOrgainisationName("");
    ccdDocumentData.setOrganisationName("");
    ccdDocumentData.setDestination("");

    return ccdDocumentData;

  }

  def parserEprIdentSegment(value: String): CcdDocumentData = {
    val cleaned = value.replaceAll("<IDENT>", "").replaceAll("</IDENT>", "").split("\\|")
    val default = getEmptyDemoMap
    default.setDestination(cleaned(0))
    default.setPatientFamily(cleaned(1))
    default.setPatientGiven(cleaned(2))
    default.setPatientRoleId(cleaned(4))
    val dateofBirth = cleaned(5)
    if(dateofBirth.indexOf("-") > -1)
    default.setPatientBirthdate(convetRfc1123DtmToHl7(dateofBirth))
    else
      default.setPatientBirthdate(convetRfc1123DtmToHl72(dateofBirth))
    
    default.setOrganisationName(cleaned(6))
    default.setTitle(cleaned(9))
    val effectiveValue = cleaned(12)
    if (effectiveValue.length() > 10)
      default.setEffectiveTimeValue(convertGeneralDtmToHl7(effectiveValue))
    else
      default.setEffectiveTimeValue(convetRfc1123DtmToHl72(effectiveValue))
    default.setRecipientGiven(cleaned(13))

    return default
  }

  def convetRfc1123DtmToHl7(value: String): String = { convertFromFormatToFormat(value, "dd-MMM-yyyy", "yyyyMMdd") }
  def convetRfc1123DtmToHl72(value: String): String = { convertFromFormatToFormat(value, "dd/MM/yyyy", "yyyyMMdd") }
  def convertGeneralDtmToHl7(value: String): String = { convertFromFormatToFormat(value, "dd/MM/yyyy HH:mm:ss", "yyyyMMddHHmmss") }
  def convertFromFormatToFormat(
    value: String,
    sourceFormat: String,
    destinationFormat: String): String = {
    val sourceDateFormat = new SimpleDateFormat(sourceFormat)
    val destinationDateFormat = new SimpleDateFormat(destinationFormat)
    destinationDateFormat.format(sourceDateFormat.parse(value))
  }
}