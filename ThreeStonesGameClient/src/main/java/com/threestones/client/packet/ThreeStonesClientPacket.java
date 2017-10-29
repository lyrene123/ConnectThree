package com.threestones.client.packet;

import java.net.*;  
import java.io.*;   
import java.util.Arrays;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreeStonesClientPacket {
    
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    private final String server = "10.172.16.25";
    private final int port = 50000;
    private Socket socket;
    private InputStream inStream;
    private OutputStream outStream;
    private final int BUFF_SIZE = 5;

    public void sendMove(int x, int y) {
        log.debug("position x " + x + "position y " + y);
        byte[] byteBuffer = {(byte) 1, (byte) x, (byte) y, (byte)0, (byte) 0};
        try {
            this.outStream.write(byteBuffer);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.getName()).log(Level.SEVERE, null, ex);
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

        byte[] b = receivePacket();
        log.debug("buildConnection and bytebuffer for each " + Arrays.toString(byteBuffer));
        return b[0] == 0;
    }

    public byte[] receivePacket() throws SocketException, IOException {
        byte[] byteBuffer = new byte[BUFF_SIZE];
        int totalBytesRcvd = 0;						// Total bytes received so far
        int bytesRcvd;
        log.debug("receivePacket on Client");
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
