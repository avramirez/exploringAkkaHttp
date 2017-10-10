package com.exploringakka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.exploringakka.dao._
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext.Implicits.global

object Init extends App {

  private val log = LoggerFactory.getLogger(getClass)

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val movieDao = new MovieDao
  val reservationDao = new ReservationDao

  val movieRestApi = new MovieRestApi(movieDao,reservationDao)

  DatabaseConnectionFactory.createSchema

  for {
    httpBinding <- Http().bindAndHandle(movieRestApi.routes, "localhost", 8080)
    dbCheck <- DatabaseConnectionFactory.liveliness
  } yield {
    log.debug("Application started successfully")
  }

}
