package com.exploringakka.dao

import com.exploringakka.DatabaseConnectionFactory
import com.exploringakka.model.Tables
import Tables.profile.api._
import com.exploringakka.model.DomainObjects._

import scala.concurrent.Future


class MovieDao {

  val db = DatabaseConnectionFactory.getDatabase

  def createMovie(imdbId:String,movieTitle:String): Future[Int] = {
    db.run(Tables.MovieTable += Movie(imdbId,movieTitle))
  }

  def getMovieById(imdbId:String): Future[Seq[Movie]] = {
    db.run(Tables.MovieTable.filter(_.imdbId === imdbId).result)
  }

}
