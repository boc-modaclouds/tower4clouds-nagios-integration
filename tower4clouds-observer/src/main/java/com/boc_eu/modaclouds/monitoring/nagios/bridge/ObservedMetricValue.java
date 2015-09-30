/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import com.google.gson.JsonObject;
import it.polimi.modaclouds.monitoring.metrics_observer.model.Variable;
import java.util.Date;

/**
 *
 * @author sseycek
 */
public class ObservedMetricValue extends Variable {
    
    private final MetricKey aKey;
    private final Date aTimestamp;
    
    public ObservedMetricValue (final String sHost, final String sMetric,
            final Variable aValue) {
        this.aKey = new MetricKey(sHost, sMetric);
        this.aTimestamp = new Date();
        setDatatype(aValue.getDatatype());
        setType(aValue.getType());
        setValue(aValue.getValue());
    }

    public MetricKey getKey() {
        return aKey;
    }
    
    public String getHost() {
        return getKey().getHost();
    }
    
    public String getMetric() {
        return getKey().getMetric();
    }
    
    public Date getTimestamp() {
        return aTimestamp;
    }
    
    @Override
    public String toString() {
        StringBuilder aSB = new StringBuilder();
        aSB.append("[timestamp:").append(getTimestamp().toString()).append(", host:").
                append(getHost()).append(", metric:").append(getMetric()).append(", value:").
                append(getValue());
        return aSB.toString();
    }
    
    public JsonObject toJson() {
        final JsonObject aRet = new JsonObject();
        aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_TIMESTAMP, getTimestamp().toString());
        aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_HOST, getHost());
        aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_METRIC, getMetric());
        aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_VALUE, getValue());
        return aRet;
    }
}
