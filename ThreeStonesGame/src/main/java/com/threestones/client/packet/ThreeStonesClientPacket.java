/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.packet;

import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeStonesClientPacket {
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
}
