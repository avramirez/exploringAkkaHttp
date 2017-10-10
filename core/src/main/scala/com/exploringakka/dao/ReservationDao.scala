package com.exploringakka.dao

import com.exploringakka.DatabaseConnectionFactory
import com.exploringakka.model.Tables
import Tables.profile.api._
import com.exploringakka.model.DomainObjects._

import scala.concurrent.Future


class ReservationDao {

  val db = DatabaseConnectionFactory.getDatabase

  def createReservation(screenId:String,imdbId:String,availableSeats:Int): Future[Int] = {
    db.run(Tables.ReservationTable += Reservation(screenId,imdbId,availableSeats))
  }

  def getReservationByIdAndMovieId(screenId:String,imdbId:String): Future[Seq[Reservation]] = {
    db.run(Tables.ReservationTable.filter(s => s.screenId === screenId && s.imdbId === imdbId).result)
  }

  def updateReservedSeats(screenId:String,imdbId:String): Future[Int] = {
    val updateQuery =
      sqlu"""UPDATE "reservation"
            SET
              "reservedSeats" = "reservedSeats" + 1
            WHERE
              "screenId" = $screenId AND
              "imdbId" = $imdbId AND
              "reservedSeats" < "availableSeats""""

    db.run(updateQuery)

  }

}
