package com.exploringakka

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.exploringakka.model.DomainObjects._
import CustomJsonProtocol._
import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.Source
import com.exploringakka.dao.{MovieDao, ReservationDao}
import com.typesafe.config.{Config, ConfigFactory}
import spray.json.JsonParser

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class MovieRestApi(movieDao:MovieDao,reservationDao: ReservationDao)(implicit val system : ActorSystem, mat: ActorMaterializer, ec: ExecutionContext){

  val config: Config = ConfigFactory.load()
  val imdbSourceUrl = config.getString("imdbSource.url")
  val apiKey = config.getString("imdbSource.apiKey")


  val routes : Route = {
    pathPrefix("movie") {
      path("register") {
        post {
          entity(as[RegisterMovieAndReservation]) { registerParam =>
            val responseFuture: Future[HttpResponse] = {
              Http().singleRequest(HttpRequest(HttpMethods.GET,Uri(s"$imdbSourceUrl${registerParam.imdbId}?api_key=$apiKey")))
            }

            onComplete {
              responseFuture.flatMap {
                case HttpResponse(StatusCodes.OK, headers, entity, _) =>
                  val getMovieTitle = Unmarshal(entity).to[String].map { jsonString =>
                    JsonParser(jsonString).asJsObject.fields("title").convertTo[String]
                  }

                  for {
                    movieTitle <- getMovieTitle
                    movie <- movieDao.createMovie(registerParam.imdbId,movieTitle)
                    reservation <- reservationDao.createReservation(registerParam.screenId,registerParam.imdbId,registerParam.availableSeats)
                  }yield Done


                case failure =>
                  throw new Exception("Something went wrong in getting movie details")

              }

            }{ registerStatus =>
              registerStatus match {
                case Success(s) => complete("Success")
                case Failure(e) => complete(e.getMessage)
              }
            }

          }
        }
      } ~
      path(Segment / Segment) { (imdbId, screenId) =>
        get {
          onComplete {
            for {
              movie <- movieDao.getMovieById(imdbId)
              reservation <- reservationDao.getReservationByIdAndMovieId(screenId,imdbId)
            } yield MovieAndReservationFormat.write(movie.head,reservation.head)
          }{
            jsonResponse =>
              jsonResponse match {
                case Success(s) => complete(s)
                case Failure(e) => complete(e.getMessage)
              }
          }
        }
      } ~
      path("reserve") {
        post {
          entity(as[ReservedMovie]) { case ReservedMovie(imdbId, screendId) =>
            onComplete(reservationDao.updateReservedSeats(screendId,imdbId)) { reserveStatus =>
              reserveStatus match {
                case Success(s) => complete("Success")
                case Failure(e) => complete(e.getMessage)
              }
            }
          }
        }
      }
    }
  }

}
