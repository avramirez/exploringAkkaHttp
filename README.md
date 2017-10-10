# exploringAkkaHttp
Exploring Akka Http

Register

curl -H "Content-Type: application/json" -X POST -d '{
"imdbId": "tt0111161",
"availableSeats" : 100 ,
"screenId" : "screen_123456"
}' http://localhost:8080/movie/register



Get

curl -X GET http://localhost:8080/movie/tt0111161/screen_123456


Reserve

curl -H "Content-Type: application/json" -X POST -d '{
"imdbId": "tt0111161",
"screenId" : "screen_123456"
}' http://localhost:8080/movie/reserve
