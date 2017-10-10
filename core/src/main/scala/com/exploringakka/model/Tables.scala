package com.exploringakka.model

import DomainObjects._



object Tables {

   val profile = slick.jdbc.H2Profile

   import profile.api._

   class MovieScreeningTable(tag : Tag) extends Table[MovieScreening](tag,"movieScreening") {

    def screenId = column[String]("screenId",O.PrimaryKey, O.Length(200,varying=true))
    def imdbId = column[String]("imdbId")
    def availableSeats = column[Int]("availableSeats")
    def reservedSeats = column[Int]("reservedSeats")

    def * = (screenId,imdbId,availableSeats,reservedSeats).mapTo[MovieScreening]

  }

  lazy val MovieScreeningTable = TableQuery(tag => new MovieScreeningTable(tag))


  class MovieTable(tag: Tag) extends Table[Movie](tag,"movie") {
    def imdbId = column[String]("imdbId",O.PrimaryKey, O.Length(200,varying=true))
    def movieTitle = column[String]("movieTitle")

    def * = (imdbId,movieTitle).mapTo[Movie]
  }

  lazy val MovieTable = TableQuery(tag => new MovieTable(tag))

  class ReservationTable(tag: Tag) extends Table[Reservation](tag,"reservation") {

    def screenId = column[String]("screenId",O.PrimaryKey, O.Length(200,varying=true))
    def imdbId = column[String]("imdbId")
    def availableSeats = column[Int]("availableSeats")
    def reservedSeats = column[Int]("reservedSeats")

    def * = (screenId,imdbId,availableSeats,reservedSeats).mapTo[Reservation]
  }

  lazy val ReservationTable = TableQuery(tag => new ReservationTable(tag))

}


