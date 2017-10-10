package com.exploringakka

import com.exploringakka.model.Tables.profile
import model.Tables.profile.api._

import scala.concurrent.Future

object DatabaseConnectionFactory {

  private val db = Database.forConfig("database")

  def getDatabase = db

  val ddl = Seq(model.Tables.MovieTable.schema,model.Tables.ReservationTable.schema)

  def createSchema: Unit = ddl.map(table => db.run(table.create))

  def liveliness: Future[Seq[Int]] = db.run(Query(1).result)
}

