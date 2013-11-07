package uk.kch.nhs.geneva.kjeragbolten.util;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

object Pdf2Tiff {

  val logger = LoggerFactory.getLogger(Pdf2Tiff.getClass);

  /**
   * @param pdfFile
   * @return
   * @throws IOException
   * @throws IM4JavaException
   * @throws InterruptedException
   * @throws Exception
   *             - Note: It is expected that the next level will fail when
   *             trying to convert It could be that the convert function is
   *             missing and an empty tiff file is produced.
   */
  @throws(classOf[IOException])
  @throws(classOf[InterruptedException])
  @throws(classOf[IM4JavaException])
  def convertPdf2Tiff(fis: InputStream, fileName: String): InputStream = {

    val tiffFile = File.createTempFile("output", ".tiff");
    logger.info("Generating tiff file:" + tiffFile.getPath());

    val inputFile = File.createTempFile("input", fileName);
    val inputFileStream = new FileOutputStream(inputFile)
    Iterator
      .continually(fis.read)
      .takeWhile(-1 !=)
      .foreach(inputFileStream.write)

    val op = new IMOperation();
    op.density(300);
    if (fileName.toLowerCase().endsWith("doc") || fileName.toLowerCase().endsWith("docx")) {
      op.addImage("doc:" + inputFile.getCanonicalPath()); // read from stdin

    } else {
      op.addImage(inputFile.getCanonicalPath()); // read from stdin
    }
    // op.trim(); // trim transparent edges
    op.background("white"); // some viewers have a default white background
    op.alpha("remove"); // flatten layers
    op.compress("lzw"); // compress
    op.`type`("bilevel"); // reduce colour space to save file size
    op.addImage("tif:-"); // write to stdout in tif-format

    logger.info("Creating I/O streams..");

    val fos = new FileOutputStream(tiffFile);
    //val pipeIn = new Pipe(fis, null);
    val pipeOut = new Pipe(null, fos);

    logger.info("Preparing conversion..");

    // set up command and run
    val convert = new ConvertCmd();
    //convert.setInputProvider(pipeIn);
    convert.setOutputConsumer(pipeOut);
    convert.run(op);

    logger.info("Cleaning up..");

    // clean up
    fis.close();
    fos.close();

    logger.info("Tiff construction completed");

    return new FileInputStream(tiffFile);
  }
}