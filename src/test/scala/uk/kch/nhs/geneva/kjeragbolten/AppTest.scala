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

@Test
class AppTest extends CamelTestSupport {

  override def createRouteBuilder() = new RouteBuilder() {
    override def configure() {
      val props = System.getProperties()
      props.setProperty("mail.mime.multipart.ignoremissingboundaryparameter", "true")
      props.setProperty("mail.mime.multipart.allowempty", "true")
      from("james-smtp:localhost:25") process (new MailExchangeProcessor) to ("smtp:10.148.129.237:25")
    }
  }

  @Test
  def sendSimpleBody() {
    val sender = "epr.notifications@nhs.net"
    val rcpt = "elijah.charles@nhs.net"
    val cc = "epr.notifications@nhs.net"
    val subject = "test"
    val body = "Testmail";
    val client = new SMTPClient();
    val header = new SimpleSMTPHeader(sender, rcpt, subject)

    header.addCC(cc)
    header.addCC(cc)
    client.connect("localhost", 25);
    client.helo("localhost");
    client.setSender(sender);
    client.addRecipient(rcpt);

    val writer = client.sendMessageData()
    val headerText = header.toString()

    writer.write(headerText)
    writer.write(body)
    writer.close()

    client.quit();
    client.disconnect();

    assertTrue(true)

  }

  @Test
  def sendAttachmentMessage() {

    val smlMessage = IOUtils.toString(getClass.getClassLoader.getResourceAsStream("rfc-822-with-attachment-test.eml"))
    val client = new SMTPClient();
    client.connect("localhost", 25);
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


