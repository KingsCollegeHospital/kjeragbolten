package uk.kch.nhs.geneva.kjeragbolten.route

import org.apache.camel.Processor
import org.apache.camel.Exchange
import javax.mail.Session
import java.util.Properties
import javax.mail.internet.MimeMessage
import org.apache.camel.component.mail.MailMessage
import org.apache.commons.mail.util.MimeMessageParser
import javax.mail.Header
import javax.mail.internet.MimeMultipart
import scala.collection.JavaConversions._
import javax.mail.internet.InternetAddress
import org.apache.commons.lang.StringUtils
import javax.mail.Address
import org.slf4j.LoggerFactory
import javax.mail.internet.MimeBodyPart
import org.apache.camel.component.mail.MailConstants
import javax.mail.internet.MimeBodyPart.MimePartDataHandler
import javax.activation.DataHandler

class MailExchangeProcessor extends Processor {

  val log = LoggerFactory.getLogger(getClass)

  def process(exchange: Exchange): Unit = {
    val in = exchange.getIn
    val mimeMessage = in.getBody(classOf[MimeMessage])
    val mimeMessageParser = new MimeMessageParser(mimeMessage)
    val headers = mimeMessage.getAllHeaders
    headers.map(f => f.asInstanceOf[Header]).foreach(f => {
      in.setHeader(f.getName, f.getValue)
    })

    in.setHeader("From", mimeMessageParser.getFrom)
    in.setHeader("Subject", mimeMessageParser.getSubject)

    def ma(f: Address): String = f.asInstanceOf[InternetAddress].getAddress

    val addressList = mimeMessageParser.getTo.map(ma)
    val addresses = StringUtils.join(addressList, ',')
    in.setHeader("To", addresses)

    if (mimeMessageParser.getCc.size > 0) {
      val ccList = mimeMessageParser.getCc.map(ma)
      val cc = StringUtils.join(ccList, ',')
      in.setHeader("CC", cc)
    }

    mimeMessageParser.parse

    val extractedLocalValue = mimeMessageParser.hasHtmlContent.toString

    log.error(extractedLocalValue)
    val extractedLocalValue2 = mimeMessageParser.hasPlainContent.toString
    log.error(extractedLocalValue2)
    if (mimeMessageParser.isMultipart) {
      val mixed = new MimeMultipart()
      if (mimeMessageParser.hasPlainContent()) {
        in.setHeader(MailConstants.MAIL_ALTERNATIVE_BODY, mimeMessageParser.getPlainContent)
        in.setBody(mimeMessageParser.getPlainContent)
        /*val plainPart = new MimeBodyPart()
        plainPart.setText(mimeMessageParser.getPlainContent)
        mixed.addBodyPart(plainPart)*/
      }
      if (mimeMessageParser.hasHtmlContent) {
        in.setBody(mimeMessageParser.hasHtmlContent)
        /*val htmlPart = new MimeBodyPart();
        htmlPart.setText(mimeMessageParser.getHtmlContent())
        mixed.addBodyPart(htmlPart)*/
      }

    } else {
      if (mimeMessageParser.hasHtmlContent)
        in.setBody(mimeMessageParser.getHtmlContent)
      else if (mimeMessageParser.hasPlainContent)
        in.setBody(mimeMessageParser.getPlainContent)
    }

    mimeMessageParser.getAttachmentList().foreach(f => in.addAttachment(f.getName(), new DataHandler(f)))

    /*mimeMessage match {
      case multipart: MimeMultipart => {
        for (i <- 0 to multipart.getCount) {
          val part = multipart.getBodyPart(i)
          in.addAttachment(part.getFileName, part.getDataHandler)
        }
      }
      case _ => {


      }

    }*/

  }

}