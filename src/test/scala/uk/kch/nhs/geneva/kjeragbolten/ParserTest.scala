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

class ParserTest extends JUnitSuite  with ShouldMatchersForJUnit {
  val identValue = "<IDENT>G85001|Cather|Peter|D446358|4961016322|24-Jul-1924|KINGS COLLEGE HOSPITAL|||A&E GP Letter|986867886||05/08/2013 00:09:05|G8630894||</IDENT>"
  val parser = new MailToCdaProcessor
  val data = parser.parserEprIdentSegment(identValue)

  @Test def destination_should_be_parsed_in = {
    data.Destination should equal("G85001")
  }

  @Test def PatientRoleId_should_be_parsed_in = {
    data.PatientRoleId should equal("4961016322")
  }

  @Test def Title_should_be_parsed_in = {
    data.Title should equal("A&E GP Letter")
  }
  @Test def PatientFamily_should_be_parsed_in = {
    data.PatientFamily should equal("Cather")
  }

  @Test def PatientGender_should_be_parsed_in = {
    data.PatientGender should equal("")
  }

  @Test def PatientBirthdate_should_be_parsed_in = {
    data.PatientBirthdate should equal("19240724")
  }

  @Test def PatientGiven_should_be_parsed_in = {
    data.PatientGiven should equal("Peter")
  }

  @Test def AuthorGiven_should_be_parsed_in = {
    data.AuthorGiven should equal("")
  }

  @Test def AuthorFamily_should_be_parsed_in = {
    data.AuthorFamily should equal("")
  }

  @Test def EffectiveTimeValue_should_be_parsed_in = {
    data.EffectiveTimeValue should equal("20130805000905")
  }

  @Test def RecipientFamily_should_be_parsed_in = {
    data.RecipientFamily should equal("")
  }

  @Test def RecipientGiven_should_be_parsed_in = {
    data.RecipientGiven should equal("G8630894")
  }

  @Test def ReceivedOrgainisationName_should_be_parsed_in = {
    data.ReceivedOrgainisationName should equal("")
  }

  @Test def OrganisationName_should_be_parsed_in = {
    data.OrganisationName should equal("KINGS COLLEGE HOSPITAL")
  }
}