package uk.kch.nhs.geneva.kjeragbolten.processor

import org.apache.camel.Processor
import org.apache.camel.Exchange
import uk.kch.nhs.geneva.kjeragbolten.CcdDocumentData
import java.text.SimpleDateFormat
import org.apache.commons.lang.StringUtils

class MailToCdaProcessor extends Processor {

  def process(exchange: Exchange): Unit = {
    val previousBody = exchange.getIn().getBody(classOf[String])
    exchange.getIn().setBody(parserEprIdentSegment(previousBody))
  }

  def getDemoMap(): CcdDocumentData = {
    val ccdDocumentData = new CcdDocumentData();
    ccdDocumentData.PatientRoleId = "9999999999";
    ccdDocumentData
      .Title = "Letter from Kings College Hospital Renal Department";
    ccdDocumentData.PatientFamily = "Jones";
    ccdDocumentData.PatientGender = "M";
    ccdDocumentData.PatientBirthdate = "19320924";
    ccdDocumentData.PatientGiven = "Tom";
    ccdDocumentData.AuthorGiven = "Hugh";
    ccdDocumentData.AuthorFamily = "Cairns";
    ccdDocumentData.EffectiveTimeValue = "20130316";
    ccdDocumentData.RecipientFamily = "Bunny";
    ccdDocumentData.RecipientGiven = "Buggs";
    ccdDocumentData.ReceivedOrgainisationName = "The Good Health Clinic";
    ccdDocumentData.OrganisationName = "Kings College Hospital";
    ccdDocumentData.Destination = "G85001";

    return ccdDocumentData;

  }
  def getEmptyDemoMap(): CcdDocumentData = {
    val ccdDocumentData = new CcdDocumentData();
    ccdDocumentData.PatientRoleId = "";
    ccdDocumentData.Title = "";
    ccdDocumentData.PatientFamily = "";
    ccdDocumentData.PatientGender = "";
    ccdDocumentData.PatientBirthdate = "";
    ccdDocumentData.PatientGiven = "";
    ccdDocumentData.AuthorGiven = "";
    ccdDocumentData.AuthorFamily = "";
    ccdDocumentData.EffectiveTimeValue = "";
    ccdDocumentData.RecipientFamily = "";
    ccdDocumentData.RecipientGiven = "";
    ccdDocumentData.ReceivedOrgainisationName = "";
    ccdDocumentData.OrganisationName = "";
    ccdDocumentData.Destination = "";

    return ccdDocumentData;

  }

  def parserEprIdentSegment(value: String): CcdDocumentData = {
    val cleaned = value.replaceAll("<IDENT>", "").replaceAll("</IDENT>", "").split("\\|")
    val default = getEmptyDemoMap
    default.Destination = cleaned(0)
    default.PatientFamily = cleaned(1)
    default.PatientGiven = cleaned(2)
    default.PatientRoleId = if (StringUtils.isEmpty(cleaned(4))) "9999999999" else cleaned(4)
    val dateofBirth = cleaned(5)
    if (dateofBirth.indexOf("-") > -1)
      default.PatientBirthdate = convetRfc1123DtmToHl7(dateofBirth)
    else
      default.PatientBirthdate = convetRfc1123DtmToHl72(dateofBirth)

    default.OrganisationName = cleaned(6)
    default.Title = cleaned(9)
    val effectiveValue = cleaned(12)
    if (effectiveValue.length() > 10)
      default.EffectiveTimeValue = convertGeneralDtmToHl7(effectiveValue)
    else
      default.EffectiveTimeValue = convetRfc1123DtmToHl72(effectiveValue)
    default.RecipientGiven = cleaned(13)

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