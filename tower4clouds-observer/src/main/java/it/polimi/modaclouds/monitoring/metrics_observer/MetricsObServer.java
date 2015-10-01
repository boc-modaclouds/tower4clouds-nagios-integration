package it.polimi.modaclouds.monitoring.metrics_observer;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MetricsObServer extends Component
{
  
  private final Class <? extends MonitoringDatumHandler> resultHandler;
  private static final Logger logger = LoggerFactory.getLogger (MetricsObServer.class);
  
  public MetricsObServer (final int listeningPort,
                          final Class <? extends MonitoringDatumHandler> resultHandler)
  {
    super ();
    getServers ().add (Protocol.HTTP, listeningPort);
    getClients ().add (Protocol.FILE);
    this.resultHandler = resultHandler;
  }
  
  public class ObServerApp extends Application
  {
    private final String observerPath;
    
    public ObServerApp (final String observerPath)
    {
      this.observerPath = observerPath;
    }
    
    @Override
    public Restlet createInboundRoot ()
    {
      final Router router = new Router (getContext ());
      router.setDefaultMatchingMode (Template.MODE_EQUALS);
      
      logger.debug ("Attaching handlare to path {}", this.observerPath);
      router.attach (this.observerPath, MetricsObServer.this.resultHandler);
      
      return router;
    }
  }
  
}
