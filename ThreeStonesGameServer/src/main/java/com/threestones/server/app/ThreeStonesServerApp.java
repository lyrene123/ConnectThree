
package com.threestones.server.app;

import com.threestones.server.ThreeStonesServer;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class ThreeStonesServerApp {
    public static void main(String[] args) {
        ThreeStonesServer server = new ThreeStonesServer();
        
        try {
            server.runServer();
        } catch (IOException ex) {
            Logger.getLogger(ThreeStonesServerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
