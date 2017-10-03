/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.server.packet;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

public class ThreeStonesServerPacket
{

  private static final int BUFSIZE = 32;	// Size of receive buffer

  public static void main(String[] args) throws IOException
  {

    if (args.length != 1)					// Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Port>");

    int servPort = Integer.parseInt(args[0]);

    // Create a server socket to accept client connection requests
    ServerSocket servSock = new ServerSocket(servPort);

    int recvMsgSize;						// Size of received message
    byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer

	// Run forever, accepting and servicing connections
    for (;;)
    {
      Socket clntSock = servSock.accept();	// Get client connection

      System.out.println("Handling client at " +
        clntSock.getInetAddress().getHostAddress() + " on port " +
        clntSock.getPort());

      InputStream in = clntSock.getInputStream();
      OutputStream out = clntSock.getOutputStream();

      // Receive until client closes connection, indicated by -1 return
      while ((recvMsgSize = in.read(byteBuffer)) != -1)
        out.write(byteBuffer, 0, recvMsgSize);

      clntSock.close();						// Close the socket. This client is finished.
    }
    /* NOT REACHED */
  }
}
