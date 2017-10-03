/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestone.client;
import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream

public class ThreeStonesClientPacket
{

  public static void main(String[] args) throws IOException
  {

    if ((args.length < 2) || (args.length > 3))	// Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

    String server = args[0];					// Server name or IP address
    byte x = 1;
    byte y = 7;
    
    // Convert input String to bytes using the default character encoding
    //byte[] byteBuffer = args[1].getBytes();
    byte[] byteBuffer = new byte[10];
      for (byte i = 0; i < 10; i++) {
        byteBuffer[i] = i;
      }
    
    

    int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

    // Create socket that is connected to server on specified port
    Socket socket = new Socket(server, servPort);
    System.out.println("Connected to server...sending echo string");

    InputStream in = socket.getInputStream();
    OutputStream out = socket.getOutputStream();

    out.write(byteBuffer);						// Send the encoded string to the server

    // Receive the same string back from the server
    int totalBytesRcvd = 0;						// Total bytes received so far
    int bytesRcvd;								// Bytes received in last read
    while (totalBytesRcvd < byteBuffer.length)
    {
      if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                        byteBuffer.length - totalBytesRcvd)) == -1)
        throw new SocketException("Connection close prematurely");
      totalBytesRcvd += bytesRcvd;
    }

      for (byte b : byteBuffer) {
          System.out.println("received bytefuffer " + b);
      }

    socket.close();								// Close the socket and its streams
  }
}


