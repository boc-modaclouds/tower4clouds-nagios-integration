package it.polimi.modaclouds.monitoring.metrics_observer;

public class MonitoringDatum
{
  
  String resourceId;
  String metric;
  String timestamp;
  String value;
  
  public String getResourceId ()
  {
    return this.resourceId;
  }
  
  public void setResourceId (final String resourceId)
  {
    this.resourceId = resourceId;
  }
  
  public String getMetric ()
  {
    return this.metric;
  }
  
  public void setMetric (final String metric)
  {
    this.metric = metric;
  }
  
  public String getTimestamp ()
  {
    return this.timestamp;
  }
  
  public void setTimestamp (final String timestamp)
  {
    this.timestamp = timestamp;
  }
  
  public String getValue ()
  {
    return this.value;
  }
  
  public void setValue (final String value)
  {
    this.value = value;
  }
  
  @Override
  public String toString ()
  {
    return "MonitoringDatum [resourceId=" +
           this.resourceId +
           ", metric=" +
           this.metric +
           ", timestamp=" +
           this.timestamp +
           ", value=" +
           this.value +
           "]";
  }
  
}
