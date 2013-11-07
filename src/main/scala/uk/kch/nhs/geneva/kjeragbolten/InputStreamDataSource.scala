package uk.kch.nhs.geneva.kjeragbolten

import javax.activation.DataSource
import java.io.InputStream
import java.io.IOException
import java.io.OutputStream

class InputStreamDataSource(val inputStream: InputStream) extends DataSource {
    @Override
    @throws(classOf[IOException])
    def getInputStream() : InputStream = {
        inputStream;
    }

    @Override
    @throws(classOf[IOException])
    def getOutputStream() : OutputStream = {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    def getContentType() : String = {
        "*/*";
    }

    @Override
    def getName() : String = {
        "InputStreamDataSource";
    }
}