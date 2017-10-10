package com.exploringakka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import spray.json.DefaultJsonProtocol

class MockImdbSource(implicit val system : ActorSystem, mat: ActorMaterializer)  extends DefaultJsonProtocol with SprayJsonSupport{

  val movieTitle = "testTitle"
  val routes : Route = {
    path("movie" / Segment ) { (imdbId) =>
      get {
        parameter('api_key ?) { apiKey:Option[String] =>
          complete(s"""{"title": "${movieTitle}"}""")
        }
      }
    }
  }
}

