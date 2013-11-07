package uk.kch.nhs.geneva.kjeragbolten.processor

import org.apache.camel.Processor
import org.apache.camel.Exchange
import scala.collection.JavaConversions._
import uk.kch.nhs.geneva.kjeragbolten.util.Pdf2Tiff
import javax.activation.DataHandler
import uk.kch.nhs.geneva.kjeragbolten.InputStreamDataSource
import java.util.Map.Entry
import javax.activation.DataHandler

import scala.collection.mutable.ListBuffer

class PdfToTiffProcessor extends Processor {
  @Override
  @throws(classOf[Exception])
  def process(exchange: Exchange): Unit = {
    val in = exchange.getIn();
    val attachments = in.getAttachments()
      .entrySet();

    val convertedItems = new ListBuffer[(String, DataHandler)]
    for (attachment <- attachments) {

      val inputAttachment = attachment.getValue().getDataSource()
        .getInputStream();
      val inputAttachmentName = attachment.getKey();
      
      val outputAttachment = Pdf2Tiff
        .convertPdf2Tiff(inputAttachment, inputAttachmentName);
      val outputAttachmentName = inputAttachmentName.split("\\.")(0) + ".tiff"
      val outputDataHandler = new DataHandler(new InputStreamDataSource(outputAttachment))
      
      convertedItems.add((outputAttachmentName, outputDataHandler))

      //outputAttachment.close();
      inputAttachment.close();
      in.removeAttachment(inputAttachmentName)
    }
    
    for (attachment <- convertedItems) {
      in.addAttachment(attachment._1, attachment._2)
    }
    

  }

}