package com.exploringakka.dao

import com.exploringakka.DatabaseConnectionFactory
import com.exploringakka.model.Tables
import Tables.profile.api._
import com.exploringakka.model.DomainObjects.MovieScreening

import scala.concurrent.Future


class MovieScreeningDao {

  val db = DatabaseConnectionFactory.getDatabase

  def createMovieScreening(screenId:String,imdbId:String,availableSeats:Int): Future[Int] = {
    db.run(Tables.MovieScreeningTable += MovieScreening(screenId,imdbId,availableSeats))
  }

  def getMovieScreeningByIdAndMovieId(screenId:String,imdbId:String): Future[Seq[MovieScreening]] = {
    db.run(Tables.MovieScreeningTable.filter(s => s.screenId === screenId && s.imdbId === imdbId).result)
  }

  def updateReservedSeats(screenId:String,imdbId:String): Future[Int] = {
    val updateQuery =
      sqlu"""UPDATE "reservations"
            SET
              "reservedSeats" = "reservedSeats" + 1
            WHERE
              "screenId" = $screenId AND
              "imdbId" = $imdbId AND
              "reservedSeats" < "availableSeats""""

    db.run(updateQuery)

  }

}
