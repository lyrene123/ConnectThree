/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.threestones.client.packet;

import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeStonesClientPacket {
    // Real programmers use logging

    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private final String server = "10.172.16.25";
    private final int port = 50000;
    private Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private boolean isConnected = false;
    private final int BUFF_SIZE = 4;

    public void sendMove(int x, int y) {
        log.debug("position x " + x + "position y " + y);

        // byteFueer has to be assigned
        // only make socket connection once
        if (!isConnected) {
            isConnected = true;
            //  1 is move
            // 0 is want to start game
            //2 players last move makes gameOver = true
            //
            byte[] byteBuffer = {(byte) 1, (byte) x, (byte) y,};
            // Create socket that is connected to server on specified port
            try {
                //Socket socket = new Socket(server, port);

                //sends byte to server
                this.outStream.write(byteBuffer);						// Send the encoded string to the server

                //receives server response
                int totalBytesRcvd = 0;						// Total bytes received so far
                int bytesRcvd;								// Bytes received in last read
                while (totalBytesRcvd < byteBuffer.length) {
                    if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd,
                            byteBuffer.length - totalBytesRcvd)) == -1) {
                        throw new SocketException("Connection close prematurely");
                    }
                    totalBytesRcvd += bytesRcvd;
                }

                log.debug("Received: " + new String(byteBuffer));

                socket.close();

            } catch (IOException ex) {
                log.debug(ex.getMessage());
            }
            System.out.println("Connected to server...sending echo string");
            // Close the socket and its streams
        }
    }

    public boolean connectToServer() throws IOException {
        log.debug("inside connectToServer begin creation of socket");
        socket = new Socket("localhost", port);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
        byte[] byteBuffer = new byte[BUFF_SIZE];
        byteBuffer[0] = 0;
        outStream.write(byteBuffer);

        byte[] b = receivePacket(byteBuffer);
        log.debug("buildConnection and bytebuffer for each " + Arrays.toString(byteBuffer));
        return b[0] == 0;
    }

    public byte[] receivePacket(byte[] byteBuffer) throws SocketException, IOException {
        int totalBytesRcvd = 0;						// Total bytes received so far
        int bytesRcvd;
        while (totalBytesRcvd < byteBuffer.length) {
            if ((bytesRcvd = inStream.read(byteBuffer, totalBytesRcvd,
                    byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection close prematurely");
            }
            totalBytesRcvd += bytesRcvd;

        }

        return byteBuffer;

    }

}
