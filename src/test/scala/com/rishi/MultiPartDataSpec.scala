package com.rishi

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, Multipart, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{FlatSpec, Matchers}


class MultiPartDataSpec extends FlatSpec with Matchers with ScalatestRouteTest with MultiPartDataHandler {
  override def testConfigSource = "akka.loglevel = WARNING"

  val firstName = Multipart.FormData.BodyPart.Strict("FirstName", "Rishi")
  val lastName = Multipart.FormData.BodyPart.Strict("LastName", "Khandelwal")

  "MultiPart Data Handler" should "not be able to save multipart data when there is error" in {
    val fileData = Multipart.FormData.BodyPart.Strict("file", HttpEntity(ContentTypes.`text/plain(UTF-8)`, "this is test file"), Map())
    val formData = Multipart.FormData(firstName, lastName, fileData)
    Post(s"/user/save/multipart/data", formData) ~> routes ~> check {
      status shouldBe StatusCodes.InternalServerError
      responseAs[String] shouldBe "Error in processing the multi part data"
    }
  }

  it should "be able to save multipart data when file has invalid key" in {
    val fileData = Multipart.FormData.BodyPart.Strict("invalid", HttpEntity(ContentTypes.`text/plain(UTF-8)`, "this is test file"), Map())
    val expectedOutput = Map("FirstName" -> "Rishi", "LastName" -> "Khandelwal", "invalid" -> "this is test file")
    val formData = Multipart.FormData(firstName, lastName, fileData)
    Post(s"/user/save/multipart/data", formData) ~> routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe s"""Data : $expectedOutput successfully saved."""
    }
  }

  it should "be able to save multipart data " in {
    val fileData = Multipart.FormData.BodyPart.Strict("file", HttpEntity(ContentTypes.`text/plain(UTF-8)`, "this is test file"), Map("fileName" -> "rishi.txt"))
    val expectedOutput = Map("FirstName" -> "Rishi", "LastName" -> "Khandelwal", "file" -> "rishi.txt")
    val formData = Multipart.FormData(firstName, lastName, fileData)
    Post(s"/user/save/multipart/data", formData) ~> routes ~> check {
      status shouldBe StatusCodes.OK
      responseAs[String] shouldBe s"""Data : $expectedOutput successfully saved."""
    }
  }
}
