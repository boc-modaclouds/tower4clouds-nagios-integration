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
public class ObserverApi extends Thread {

    private static final Logger aLog = LoggerFactory.getLogger(ObserverApi.class);
    private final int nPort;
    
    public ObserverApi(final int nPort) {
        super();
        this.nPort = nPort;
    }
    
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        try {
            Server aServer = new Server(Protocol.HTTP, nPort, MetricResource.class);
            aServer.start();
            try {
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException aEx) {
                aLog.info("caught interrupt request, shutting down ObserverApi ...");
                aServer.stop();
            }
        } catch (Exception ex) {
            aLog.error("exception caught in ObserverApi: {}", ex);
        } finally {
            aLog.info("ObserverApi shut down.");
        }
    }

}
