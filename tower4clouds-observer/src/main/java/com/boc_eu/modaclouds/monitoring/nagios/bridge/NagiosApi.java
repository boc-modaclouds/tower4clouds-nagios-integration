/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sseycek
 */
public class NagiosApi extends Thread {

    private static final Logger aLog = LoggerFactory.getLogger(NagiosApi.class);
    private final int nPort;
    
    public NagiosApi(final int nPort) {
        super();
        this.nPort = nPort;
    }
    
    @Override
    public void run() {
        try {
            NagiosResource aNew = new NagiosResource();
            Server aServer = new Server(Protocol.HTTP, nPort, NagiosResource.class);
            aServer.start();
            try {
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException aEx) {
                aLog.info("caught interrupt request, shutting down NagiosApi ...");
                aServer.stop();
            }
        } catch (Exception ex) {
            aLog.error("exception caught in NagiosApi: {}", ex);
        } finally {
            aLog.info("NagiosApi shut down.");
        }
    }

}
