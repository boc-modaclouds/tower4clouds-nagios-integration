package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class MetricObserverApp 
{
    private static final Logger aLog = LoggerFactory.getLogger(MetricObserverApp.class);
    
    public static void main( String[] aArgs )
    {
        aLog.info("starting MetricObserverApp ...");
        final int nObserverPort = Config.getInstance().getObserverPort();
        MetricsObServer aObserver = new BocMetricsObserver(nObserverPort);
        final int nNagiosPort = Config.getInstance().getNagiosPort();
        NagiosApi aNatiosApi = new NagiosApi(nNagiosPort);
        try {
            aNatiosApi.start();
            aObserver.start();
            // now wait for the command to stop
            try {
                while (true) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException aEx) {
                aLog.info("caught interrupt request, shutting down ...");
                aNatiosApi.interrupt();
                aObserver.stop();
                aNatiosApi.join();
            }
        } catch (Exception aEx) {
            aLog.error("caught exception {}, exiting ...", aEx);
        } finally {
            aLog.info("done.");
        }
    }
}
