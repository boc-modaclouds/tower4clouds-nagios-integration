/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public/**
 * @author sseycek
 */
class InMemoryMetricStore implements MetricStore
{
  
  private static final Logger aLog = LoggerFactory.getLogger (InMemoryMetricStore.class);
  
  private final ConcurrentMap <MetricKey, ObservedMetricValue> aMap = new ConcurrentHashMap <MetricKey, ObservedMetricValue> ();
  
  public InMemoryMetricStore ()
  {
    final MonitoringDatum aVar = new MonitoringDatum ();
    aVar.setValue ("42");
    final ObservedMetricValue aTestValue = new ObservedMetricValue ("test", "test", aVar);
    put (aTestValue);
  }
  
  @Override
  public void put (final ObservedMetricValue aValue)
  {
    if (null != aValue)
    {
      this.aMap.put (aValue.getKey (), aValue);
      aLog.debug ("value {} has been stored in the MetricStore", aValue.toString ());
    }
  }
  
  @Override
  public ObservedMetricValue get (final String sHost, final String sMetric)
  {
    return get (new MetricKey (sHost, sMetric));
  }
  
  public ObservedMetricValue get (final MetricKey aMetricKey)
  {
    if (null == aMetricKey)
    {
      return null;
    }
    final ObservedMetricValue aVal = this.aMap.get (aMetricKey);
    aLog.debug ("value {} has been retrieved from the MetricStore",
                null == aVal ? "(null)" : aVal.toString ());
    return aVal;
  }
  
}
