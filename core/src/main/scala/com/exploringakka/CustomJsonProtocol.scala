package com.exploringakka

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import model.DomainObjects._

object CustomJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val registerMovieAndReservationFormat = jsonFormat3(RegisterMovieAndReservation)
  implicit val reservedMovieFormat = jsonFormat2(ReservedMovie)

  implicit object MovieAndReservationFormat {
    def write(movie: Movie, reservation: Reservation): JsValue = {
      s"""{
        |"imdbId": "${movie.imdbId}",
        |"screenId": "${reservation.screenId}",
        |"movieTitle": "${movie.movieTitle}",
        |"availableSeats": ${reservation.availableSeats},
        |"reservedSeats" : ${reservation.reservedSeats}
        |}""".stripMargin.parseJson
    }
  }
}

