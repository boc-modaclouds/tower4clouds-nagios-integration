package it.polimi.modaclouds.monitoring.metrics_observer;

import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public abstract class MonitoringDatumHandler extends ServerResource
{
  
  private static final Logger logger = LoggerFactory.getLogger (MonitoringDatumHandler.class.getName ());
  private static Gson gson = new Gson ();
  
  public abstract void getData (List <MonitoringDatum> monitoringData);
  
  @Post
  public void getData (final Representation entity)
  {
    try
    {
      getData (JSONMonitoringDataParser.fromJson (entity));
      this.getResponse ().setStatus (Status.SUCCESS_OK, "Monitoring datum succesfully received");
      this.getResponse ().setEntity (gson.toJson ("Monitoring datum succesfully received"),
                                     MediaType.APPLICATION_JSON);
      
    }
    catch (final Exception e)
    {
      logger.error ("Error while receiving monitoring data", e);
      this.getResponse ().setStatus (Status.SERVER_ERROR_INTERNAL,
                                     "Error while receiving monitoring data");
      this.getResponse ().setEntity (gson.toJson ("Error while receiving monitoring data"),
                                     MediaType.APPLICATION_JSON);
    }
    finally
    {
      this.getResponse ().commit ();
      this.commit ();
      this.release ();
    }
  }
}
