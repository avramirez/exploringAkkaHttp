package com.exploringakka

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import spray.json._
import CustomJsonProtocol._
import akka.http.scaladsl.Http
import com.exploringakka.dao._


class MovieRestApiSpec extends WordSpec with Matchers with ScalatestRouteTest {



  "Movie Rest API" should {

    //GIVEN h2Db
    DatabaseConnectionFactory.createSchema
    val movieDao = new MovieDao
    val reservationDao = new ReservationDao


    //AND mock api source to get movieTitle
    val mockImdbSource = new MockImdbSource()
    Http().bindAndHandle(mockImdbSource.routes, "localhost", 10555)


    val movieRestApi = new MovieRestApi(movieDao,reservationDao)

    val IMDB_ID = "tt0111161"
    val AVAILABLE_SEATS = 100
    val SCREEND_ID = "screen_123456"

    "be able to register a movie in path /movie/register" in {
      Post("/movie/register",HttpEntity(ContentTypes.`application/json`,
        s"""
           |{
           |"imdbId": "$IMDB_ID",
           |"availableSeats" : $AVAILABLE_SEATS ,
           |"screenId" : "$SCREEND_ID"
           |}""".stripMargin)) ~> movieRestApi.routes ~> check {

        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "Success"
      }
    }

    "be able to retrieve registered movie in path movie/$imdbId/$screenId" in {

      Get(s"/movie/$IMDB_ID/$SCREEND_ID") ~> movieRestApi.routes ~> check {
        status shouldEqual StatusCodes.OK

        val jsonResponse = responseAs[String].parseJson.asJsObject

        jsonResponse.fields("imdbId").convertTo[String] shouldEqual IMDB_ID
        jsonResponse.fields("screenId").convertTo[String] shouldEqual SCREEND_ID
        jsonResponse.fields("movieTitle").convertTo[String] shouldEqual mockImdbSource.movieTitle
        jsonResponse.fields("availableSeats").convertTo[Int] shouldEqual AVAILABLE_SEATS
        jsonResponse.fields("reservedSeats").convertTo[Int] shouldEqual 0
      }
    }

    "be able to reserve a seat in path movie/reserve" in {

      Post("/movie/reserve",HttpEntity(ContentTypes.`application/json`,
        s"""
           |{ "imdbId": "$IMDB_ID",
           |  "screenId" : "$SCREEND_ID"} """.stripMargin)) ~> movieRestApi.routes ~> check {

        status shouldEqual StatusCodes.OK

        responseAs[String] shouldEqual "Success"
      }

    }

    "after 1 Reservation reserveSeats should Increase by 1" in {

      Get(s"/movie/$IMDB_ID/$SCREEND_ID") ~> movieRestApi.routes ~> check {
        status shouldEqual StatusCodes.OK

        val jsonResponse = responseAs[String].parseJson.asJsObject

        jsonResponse.fields("imdbId").convertTo[String] shouldEqual IMDB_ID
        jsonResponse.fields("screenId").convertTo[String] shouldEqual SCREEND_ID
        jsonResponse.fields("movieTitle").convertTo[String] shouldEqual mockImdbSource.movieTitle
        jsonResponse.fields("availableSeats").convertTo[Int] shouldEqual AVAILABLE_SEATS
        jsonResponse.fields("reservedSeats").convertTo[Int] shouldEqual 1
      }
    }
  }

}