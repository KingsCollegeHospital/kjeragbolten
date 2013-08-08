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

class IntegrationTest extends CamelTestSupport {
  @Ignore
  @Test
  def sendToExternalServer() {
    val smlMessage = IOUtils.toString(getClass.getClassLoader.getResourceAsStream("Subject  Anaemia Clinic  from King's Renal Unit (KCH No V278328).eml"))
    val client = new SMTPClient();
    client.connect("localhost", 26);
    client.helo("localhost");
    client.setSender("epr.notifications@nhs.net")
    client.addRecipient("elijah.charles@nhs.net")
    val writer = client.sendMessageData()
    writer.write(smlMessage)
    writer.close()

    client.quit()
    client.disconnect()

    assertTrue(true)
  }

}