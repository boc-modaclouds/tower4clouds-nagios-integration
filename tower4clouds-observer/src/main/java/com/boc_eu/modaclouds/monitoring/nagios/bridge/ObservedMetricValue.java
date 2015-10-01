/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;

import java.util.Date;

import com.google.gson.JsonObject;

/**
 * @author sseycek
 */
public class ObservedMetricValue extends MonitoringDatum
{
  
  private final MetricKey aKey;
  private final Date aTimestamp;
  
  public ObservedMetricValue (final String sHost, final String sMetric, final MonitoringDatum aValue)
  {
    this.aKey = new MetricKey (sHost, sMetric);
    this.aTimestamp = new Date ();
    setResourceId (aValue.getResourceId ());
    setMetric (aValue.getMetric ());
    setValue (aValue.getValue ());
    setTimestamp (aValue.getTimestamp ());
  }
  
  public MetricKey getKey ()
  {
    return this.aKey;
  }
  
  public String getHost ()
  {
    return getKey ().getHost ();
  }
  
  @Override
  public String getMetric ()
  {
    return getKey ().getMetric ();
  }
  
  @Override
  public String getTimestamp()
  {
      if (null != aTimestamp) {
          return aTimestamp.toString();
      } else {
          return "";
      }
  }
  
  @Override
  public String toString ()
  {
    final StringBuilder aSB = new StringBuilder ();
    aSB.append ("[timestamp:")
       .append (getTimestamp ())
       .append (", host:")
       .append (getHost ())
       .append (", metric:")
       .append (getMetric ())
       .append (", value:")
       .append (getValue ());
    return aSB.toString ();
  }
  
  public JsonObject toJson ()
  {
    final JsonObject aRet = new JsonObject ();
    aRet.addProperty (Constants.NAGIOS_JSON_PROPERTY_TIMESTAMP, getTimestamp ().toString ());
    aRet.addProperty (Constants.NAGIOS_JSON_PROPERTY_HOST, getHost ());
    aRet.addProperty (Constants.NAGIOS_JSON_PROPERTY_METRIC, getMetric ());
    aRet.addProperty (Constants.NAGIOS_JSON_PROPERTY_VALUE, getValue ());
    return aRet;
  }
}
