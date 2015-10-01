/**
 * @note Copyright\n
 *     	ADONIS:cloud infrastructure\n
 *      (C) COPYRIGHT BOC - Business Objectives Consulting 1996-2013\n
 *      All Rights Reserved\n
 *      Use, duplication or disclosure restricted by BOC Information Systems\n
 *      Vienna, 2013
 * @brief 
 * @author Core Development, 2013
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;
import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatumHandler;

import java.util.Date;
import java.util.List;

/**
 * @author stepan
 */
public class BocMetricsResultHandler extends MonitoringDatumHandler
{
  private final MetricStoreFactory aMetricStoreFactory = new MetricStoreFactory ();
  
  @Override
  public void getData (final List <MonitoringDatum> monitoringData)
  {
    final String observerTimestamp = Long.toString (new Date ().getTime ());
    for (final MonitoringDatum monitoringDatum : monitoringData)
    {
      System.out.println (observerTimestamp +
                          "," +
                          monitoringDatum.getResourceId () +
                          "," +
                          monitoringDatum.getMetric () +
                          "," +
                          monitoringDatum.getValue () +
                          "," +
                          monitoringDatum.getTimestamp ());
      final ObservedMetricValue obsVal = new ObservedMetricValue (monitoringDatum.getResourceId (),
                                                                  monitoringDatum.getMetric (),
                                                                  monitoringDatum);
      final MetricStore aStore = this.aMetricStoreFactory.getMetricStore ();
      aStore.put (obsVal);
    }
    
  }
  
}
