package com.rishi

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.FileIO

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContextExecutor, Future}

trait MultiPartDataHandler {

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  /**
   * Route for process multipart data
   */
  def processMultiPartData: Route = {
    path("process" / "multipart" / "data") {
      (post & entity(as[Multipart.FormData])) { formData =>
          complete {
      val extractedData: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {

        case file: BodyPart if file.name == "file" =>
          val tempFile = File.createTempFile("user", "image")
          file.entity.dataBytes.runWith(FileIO.toFile(tempFile)).map(_ => file.name -> file.getFilename().get())

        case data: BodyPart => data.toStrict(2.seconds).map(strict => data.name -> strict.entity.data.utf8String)

      }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)
       extractedData.map (data =>   HttpResponse(StatusCodes.OK, entity = s"Data : $data successfully saved.") )
       .recover{
        case ex: Exception => HttpResponse(StatusCodes.InternalServerError, entity = "Error in processing the multi part data")
       }
          }
      }
    }
  }

  val routes = processMultiPartData
}
