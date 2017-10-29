package com.threestones.client.packet;

import java.net.*;
import java.io.*;
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

    public void sendClientPacketToServer(int x, int y) {
        log.debug("position x " + x + "position y " + y);
        byte[] byteBuffer = {(byte) 1, (byte) x, (byte) y, (byte) 0, (byte) 0};
        try {
            this.outStream.write(byteBuffer);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean sendStartGameRequestToServer() {
        byte[] receivedPacket = new byte[BUFF_SIZE];
        receivedPacket[0] = -1;
        try {
            log.debug("inside sendStartGameRequestToServer");

            byte[] byteBuffer = new byte[BUFF_SIZE];
            byteBuffer[0] = 0;
            outStream.write(byteBuffer);

            receivedPacket = receiveClientPacket();

        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return receivedPacket[0] == 0;
    }

    public void createConnectionWithServer(String address) throws IOException{
        socket = new Socket(address, port);
        inStream = socket.getInputStream();
        outStream = socket.getOutputStream();
    }

    public boolean sendPlayAgainRequestToServer() {
        byte[] receivedPacket = new byte[BUFF_SIZE];
        try {
            byte[] playAgainRequestPacket = new byte[BUFF_SIZE];
            playAgainRequestPacket[0] = 2;
            outStream.write(playAgainRequestPacket);
            receivedPacket = receiveClientPacket();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
        return receivedPacket[0] == 2;
    }

    public void sendQuitGameRequestToServer() {
        try {
            byte[] quitRequestPacket = new byte[BUFF_SIZE];
            quitRequestPacket[0] = 3;
            outStream.write(quitRequestPacket);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(ThreeStonesClientPacket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] receiveClientPacket() throws SocketException, IOException {
        byte[] byteBuffer = new byte[BUFF_SIZE];
        int totalBytesRcvd = 0;
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
