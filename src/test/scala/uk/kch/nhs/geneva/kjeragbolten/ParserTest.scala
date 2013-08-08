package uk.kch.nhs.geneva.kjeragbolten

import org.apache.camel.test.junit4.CamelTestSupport
import org.apache.camel.builder.RouteBuilder
import org.junit._
import Assert._
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
import uk.kch.nhs.geneva.kjeragbolten.route.MailExchangeProcessor
import javax.mail.Address
import org.apache.commons.lang.StringUtils
import org.apache.commons.io.IOUtils
import com.sun.mail.util.PropUtil
import uk.kch.nhs.geneva.kjeragbolten.processor.MailToCdaProcessor

class ParserTest extends CamelTestSupport {
  val identValue = "<IDENT>G85001|Cather|Peter|D446358|4961016322|24-Jul-1924|KINGS COLLEGE HOSPITAL|||A&E GP Letter|986867886||05/08/2013 00:09:05|G8630894||</IDENT>"
  val parser = new MailToCdaProcessor
  val data = parser.parserEprIdentSegment(identValue)
  
  @Test
  def getDestination() {  
    assertEquals("G85001", data.Destination)
  }
  @Test
  def test_PatientRoleId() {  
    assertEquals("4961016322", data.PatientRoleId)
  }
  @Test
  def test_Title() {  
    assertEquals("A&E GP Letter", data.Title)
  }
  @Test
  def test_PatientFamily() {  
    assertEquals("Cather", data.PatientFamily)
  }
  @Test
  def test_PatientGender() {  
    assertEquals("", data.PatientGender)
  }
  @Test
  def test_PatientBirthdate() {  
    assertEquals("19240724", data.PatientBirthdate)
  }
  @Test
  def test_PatientGiven() {  
    assertEquals("Peter", data.PatientGiven)
  }
  @Test
  def test_AuthorGiven() {  
    assertEquals("", data.AuthorGiven)
  }
  @Test
  def test_AuthorFamily() {  
    assertEquals("", data.AuthorFamily)
  }
  @Test
  def test_EffectiveTimeValue() {  
    assertEquals("20130805000905", data.EffectiveTimeValue)
  }
  @Test
  def test_RecipientFamily() {  
    assertEquals("", data.RecipientFamily)
  }
  @Test
  def test_RecipientGiven() {  
    assertEquals("G8630894", data.RecipientGiven)
  }
  @Test
  def test_ReceivedOrgainisationName() {  
    assertEquals("", data.ReceivedOrgainisationName)
  }
  @Test
  def test_OrganisationName() {  
    assertEquals("KINGS COLLEGE HOSPITAL", data.OrganisationName)
  }
}