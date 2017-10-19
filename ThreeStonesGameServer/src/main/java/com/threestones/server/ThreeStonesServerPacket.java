/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.server;

import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeStonesServerPacket {

    // Real programmers use logging
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    public void send(List<Byte> list) throws IOException {
        // Server name or IP address
        String server = "someIp";

        //build byte buffer for packet
        byte[] byteBuffer = new byte[list.size()];

        // loop through collection 
        for (byte i = 0; i < list.size(); i++) {
            byteBuffer[i] = list.get(i);
        }
        // initalize port for server
        int servPort = 50000;

        // Create socket that is connected to server on specified port
        Socket socket = new Socket(server, servPort);
        log.info("Connected to server...on port ");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        out.write(byteBuffer);						// Send the encoded string to the server

        // Receive the same string back from the server
        int totalBytesRcvd = 0;						// Total bytes received so far
        int bytesRcvd;								// Bytes received in last read
        while (totalBytesRcvd < byteBuffer.length) {
            if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                    byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection close prematurely");
            }
            totalBytesRcvd += bytesRcvd;
        }

        for (byte b : byteBuffer) {
            System.out.println("received bytefuffer " + b);
        }

        socket.close();								// Close the socket and its streams
    }

    public List<Byte> receive(String[] args) throws IOException {

        if (args.length != 1) // Test for correct # of args
        {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int recvMsgSize;						// Size of received message
        byte[] byteBuffer = new byte[0];	// Receive buffer

        // Run forever, accepting and servicing connections
        for (;;) {
            Socket clntSock = servSock.accept();	// Get client connection

            System.out.println("Handling client at "
                    + clntSock.getInetAddress().getHostAddress() + " on port "
                    + clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                out.write(byteBuffer, 0, recvMsgSize);
            }
            // Close the socket. This client is finished.
            clntSock.close();
            return null;
        }
        /* NOT REACHED */
    }

}
