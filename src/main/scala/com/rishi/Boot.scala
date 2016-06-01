package com.rishi

import akka.http.scaladsl.Http

object Boot extends App with MultiPartData {

  Http().bindAndHandle(routes, "0.0.0.0", 9000)
}
