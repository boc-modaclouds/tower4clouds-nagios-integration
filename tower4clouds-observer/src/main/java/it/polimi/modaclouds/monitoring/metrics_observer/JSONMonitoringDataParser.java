package it.polimi.modaclouds.monitoring.metrics_observer;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.representation.Representation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class JSONMonitoringDataParser
{
  
  private static final Logger logger = LoggerFactory.getLogger (JSONMonitoringDataParser.class);
  private static Gson gson = new Gson ();

  private static final String METRIC = "metric";
  private static final String VALUE = "value";
  private static final String RESOURCEID = "resourceId";
  private static final String TIMESTAMP = "timestamp";  
  
  private static List <Map <String, String>> nullable (final List <Map <String, String>> list)
  {
    if (list != null)
      return list;
    else
    {
      final List <Map <String, String>> emptyValueList = new ArrayList <Map <String, String>> ();
      final Map <String, String> emptyValueMap = new HashMap <String, String> ();
      emptyValueMap.put ("value", "");
      emptyValueList.add (emptyValueMap);
      return emptyValueList;
    }
  }
  
  public static List <MonitoringDatum> jsonToMonitoringDatum (final String json) throws IOException
  {
    JsonReader reader = null;
    reader = new JsonReader (new StringReader (json));
    final Type type = new TypeToken <Map <String, Map <String, List <Map <String, String>>>>> ()
    {}.getType ();
    final Map <String, Map <String, List <Map <String, String>>>> jsonMonitoringData = gson.fromJson (reader,
                                                                                                      type);
    final List <MonitoringDatum> monitoringData = new ArrayList <MonitoringDatum> ();
    if (jsonMonitoringData.isEmpty ())
    {
      logger.warn ("Empty monitoring data json object received");
      return monitoringData;
    }
    for (final Map <String, List <Map <String, String>>> jsonMonitoringDatum : jsonMonitoringData.values ())
    {
      final MonitoringDatum datum = new MonitoringDatum ();
      datum.setMetric (nullable (jsonMonitoringDatum.get (METRIC)).get (0).get ("value"));
      datum.setTimestamp (nullable (jsonMonitoringDatum.get (TIMESTAMP)).get (0).get ("value"));
      datum.setValue (nullable (jsonMonitoringDatum.get (VALUE)).get (0).get ("value"));
      datum.setResourceId (nullable (jsonMonitoringDatum.get (RESOURCEID)).get (0).get ("value"));
      monitoringData.add (datum);
    }
    return monitoringData;
  }
  
  public static List <MonitoringDatum> fromJson (final Representation json) throws IOException
  {
    final String jsonText = json.getText ();
    return jsonToMonitoringDatum (jsonText);
  }
  
}
