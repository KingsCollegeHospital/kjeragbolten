package uk.kch.nhs.geneva.kjeragbolten

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.ExchangePattern
import org.apache.commons.net.smtp.SMTPClient
import org.apache.commons.net.smtp.SimpleSMTPHeader
import javax.mail.internet.MimeMessage
import org.apache.camel.Processor
import org.apache.camel.Exchange
import javax.mail.internet.MimeMessage
import java.io.InputStream
import javax.mail.Session
import java.util.Properties
import scala.collection.JavaConversions._
import java.util.HashMap
import org.apache.commons.mail.util.MimeMessageParser
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import java.io.ByteArrayOutputStream
import org.apache.camel.component.mail.MailMessage
import javax.mail.internet.InternetAddress
import javax.mail.internet.InternetHeaders.InternetHeader
import javax.mail.Header
import javax.mail.internet.MimeMultipart
import javax.mail.Address
import org.apache.commons.lang.StringUtils
import org.apache.commons.io.IOUtils
import com.sun.mail.util.PropUtil
import uk.kch.nhs.geneva.kjeragbolten.processor.MailToCdaProcessor
import org.scalatest.junit.JUnitSuite
import org.scalatest.junit.ShouldMatchersForJUnit
import org.junit.Test
import org.junit.Ignore

class ViewPointParserTest extends JUnitSuite with ShouldMatchersForJUnit {
  val identValue = "|Bloggs|Jessie|V123456||01/01/1985|KINGS COLLEGE HOSPITAL|ViewPoint|26/07/2013||332013|Dr Chorouk Kohler-Boureq, visiting doctor|26/07/2013|G2648532G85644|Department of Womens Health|Early Pregnancy Unit "
  val parser = new MailToCdaProcessor
  val data = parser.parserEprIdentSegment(identValue)

  
  @Test @Ignore def destination_should_be_parsed_in() {
    data.Destination should equal("G85001")
  }

  @Test @Ignore def PatientRoleId_should_be_parsed_in() {
    data.PatientRoleId should equal("1234567890")
  }

  @Test @Ignore def Title_should_be_parsed_in() {
    data.Title should equal("A&E GP Letter")
  }
  @Test @Ignore def PatientFamily_should_be_parsed_in() {
    data.PatientFamily should equal("Bloggs")
  }

  @Test @Ignore def PatientGender_should_be_parsed_in() {
    data.PatientGender should equal("")
  }

  @Test @Ignore def PatientBirthdate_should_be_parsed_in() {
    data.PatientBirthdate should equal("19900101")
  }

  @Test @Ignore def PatientGiven_should_be_parsed_in() {
    data.PatientGiven should equal("Joe")
  }

  @Test @Ignore def AuthorGiven_should_be_parsed_in() {
    data.AuthorGiven should equal("")
  }

  @Test @Ignore def AuthorFamily_should_be_parsed_in() {
    data.AuthorFamily should equal("")
  }

  @Test @Ignore def EffectiveTimeValue_should_be_parsed_in() {
    data.EffectiveTimeValue should equal("20130805000905")
  }

  @Test @Ignore def RecipientFamily_should_be_parsed_in() {
    data.RecipientFamily should equal("")
  }

  @Test @Ignore def RecipientGiven_should_be_parsed_in() {
    data.RecipientGiven should equal("G8630894")
  }

  @Test @Ignore def ReceivedOrgainisationName_should_be_parsed_in() {
    data.ReceivedOrgainisationName should equal("")
  }

  @Test @Ignore def OrganisationName_should_be_parsed_in() {
    data.OrganisationName should equal("KINGS COLLEGE HOSPITAL")
  }
}