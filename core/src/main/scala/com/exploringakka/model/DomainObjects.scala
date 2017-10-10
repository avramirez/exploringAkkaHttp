package com.exploringakka.model

object DomainObjects {

  case class Movie(imdbId:String, movieTitle:String)
  case class Reservation(screenId:String,imdbId:String, availableSeats:Int, reservedSeats:Int = 0)


  case class RegisterMovieAndReservation(imdbId:String, screenId:String, availableSeats:Int)
  case class ReservedMovie(imdbId:String,screenId:String)

  case class MovieScreening(screenId:String,imdbId:String,availableSeats:Int,reservedSeats:Int = 0)


}
