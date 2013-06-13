package uk.kch.nhs.geneva.kjeragbolten.route

import org.apache.camel.Exchange
import org.apache.camel.scala.dsl.builder.RouteBuilder

class MtaRouteBuilder extends RouteBuilder {

  "james-smtp:localhost:25" ==> {
    process(new MailExchangeProcessor)
    to("smtp:10.148.129.237:25")
  }

}