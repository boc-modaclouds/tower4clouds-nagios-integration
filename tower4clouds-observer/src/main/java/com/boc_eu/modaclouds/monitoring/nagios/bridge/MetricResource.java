/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.MonitoringDatum;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sseycek
 */
public class MetricResource extends ServerResource {
    
    private static final MetricStoreFactory aMetricStoreFactory = new MetricStoreFactory();
    private static final Logger logger = LoggerFactory.getLogger(MetricResource.class);
    private static final Gson gson = new Gson();
    
    private static final String METRIC = "metric";
    private static final String VALUE = "value";
    private static final String RESOURCEID = "resourceId";
    private static final String TIMESTAMP = "timestamp";    
    
    @Post
    public String storeMetricValue(Representation entity) {
        try {
            final String jsonText = entity.getText();
            JsonReader reader = new JsonReader(new StringReader(jsonText));
            final Type type = new TypeToken<List<Map<String, String>>>(){}.getType();
            final List<Map<String, String>> jsonMonitoringData = gson.fromJson(reader, type);
            if (jsonMonitoringData.isEmpty()) {
                logger.warn("Empty monitoring data json object received");
                setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return "{\"error\": \"Empty monitoring data json object received\"}";
            }
            
            MetricStore aStore = aMetricStoreFactory.getMetricStore();
            for (final Map<String, String> jsonMonitoringDatum : jsonMonitoringData) {
                final MonitoringDatum datum = new MonitoringDatum();
                datum.setMetric(jsonMonitoringDatum.get(METRIC));
                datum.setTimestamp(jsonMonitoringDatum.get(TIMESTAMP));
                datum.setValue(jsonMonitoringDatum.get(VALUE));
                datum.setResourceId(jsonMonitoringDatum.get(RESOURCEID));
                
                logger.debug("Processing monitoring datum {}, {}, {}, {}",
                        datum.getResourceId(),
                        datum.getMetric(),
                        datum.getValue(),
                        datum.getTimestamp());
                final ObservedMetricValue obsVal = new ObservedMetricValue(datum.getResourceId(),
                        datum.getMetric(),
                        datum);
                aStore.put(obsVal);
            }
            return "{\"message\":\"processed monitoring data, thank you\"}";
        } catch (IOException ex) {
            setStatus(Status.SERVER_ERROR_INTERNAL);
            return "{\"error\":\"parsing monitoring data failed\"}";
        }
    }
}
