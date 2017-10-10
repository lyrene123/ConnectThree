
package com.threestones.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ehugh
 */

// TODO merge packet class into server to be able to receive and send requests
public class ThreeStonesServer {
    
    private ThreeStonesServerSession session;

    public ThreeStonesServer() {
        this.session = new ThreeStonesServerSession();        
    }
    
    public void runServer() {
        
        /*
            s is a socket 
        then call s.accept() receives any inbound connections
        
        
        instantiate a new session and passes the socket 
        server.playSession();
        all of this is in a loop    
        */
        
        
        this.session.playSession();
    }
    
}
