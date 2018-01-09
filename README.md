# ThreeStonesGame

Data Communication and Networking Project during 3rd year CompSci.

A client-server TCP-based system written in Java that will allow a person using the client to play a game or multiple games of 3 Stones with the computer server. The system consists of two applications: The client-side application and the server-side application

## Server-side Application
* At initiation, the server software will display its IP address.
* The machines will then connect to begin the session.
* The server will continue servicing the client that began the session until the client terminates the session by closing the socket, at which point the server will resume listening for new clients. 
* A new feature added is multithreading. The server can handle multiple clients at the same time.
* The server is responsible for all of the scoring logic.

## Client-side Application
* At initiation, the client will allow the user to input the IP address of the server
* The client program will allow the user to start a game, or to quit the client. The client will allow the user the same two choices at the end of each game.
* The client will display the game board while a game is in progress
* Does not handle game logic, simply displays the game board, appropriate messages, and accepts user input.

## About the game
* 3 Stones is a strategy board game akin to tic-tac-toe. 
* Client plays the first stone onto the board and then the server and client will alternate playing stones. Each will try to score more points than the opponent by the end of the game, which occurs when all stones have been used. 
* The client starts with 15 white stones and the server starts with 15 black stones. 
* At the end of the game the client must display a message showing the 2 scores and who the winner is, or whether a draw occurred. 

## Developed by:
Lyrene Labor <br>
Eric Hughes <br>
Jacob Riendeau
