package com.boc_eu.modaclouds.monitoring.nagios.bridge;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricObserverApp
{
  private static final Logger aLog = LoggerFactory.getLogger (MetricObserverApp.class);
  
  public static void main (final String [] aArgs)
  {
    aLog.info ("starting MetricObserverApp ...");
    final int nObserverPort = Config.getInstance ().getObserverPort ();
    final ObserverApi aObserverApi = new ObserverApi (nObserverPort);
    final int nNagiosPort = Config.getInstance ().getNagiosPort ();
    final NagiosApi aNatiosApi = new NagiosApi (nNagiosPort);
    try
    {
      aNatiosApi.start ();
      aObserverApi.start ();
      // now wait for the command to stop
      
      try
      {
        while (true)
        {
          Thread.sleep (1000);
        }
      }
      catch (final InterruptedException aEx)
      {
        aLog.info ("caught interrupt request, shutting down ...");
        aNatiosApi.interrupt ();
        aObserverApi.interrupt();
        aNatiosApi.join ();
        aObserverApi.join ();
      }
      
    }
    catch (final Exception aEx)
    {
      aLog.error ("caught exception {}, exiting ...", aEx);
    }
    finally
    {
      aLog.info ("done.");
    }
  }
}
