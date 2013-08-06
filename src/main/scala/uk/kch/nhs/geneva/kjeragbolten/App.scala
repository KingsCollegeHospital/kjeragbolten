package uk.kch.nhs.geneva.kjeragbolten

import org.apache.camel.main.Main
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport
import uk.kch.nhs.geneva.kjeragbolten.route.MtaRouteBuilder

/**
 * @author ${user.name}
 */
object App extends RouteBuilderSupport {

  def main(args: Array[String]) {

    /*val props = System.getProperties()
    props.setProperty("mail.mime.multipart.ignoremissingboundaryparameter", "true")
    props.setProperty("mail.mime.multipart.allowempty", "true")*/
    val main = new Main
    val mteRouteBuilder = new MtaRouteBuilder

    main enableHangupSupport ()
    main addRouteBuilder (mteRouteBuilder)
    main run
  }

}
