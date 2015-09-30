/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import com.google.gson.JsonObject;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 *
 * @author sseycek
 */
public class NagiosResource extends ServerResource {

    private final MetricStoreFactory aMetricStoreFactory = new MetricStoreFactory();
    
    @Get("json")
    public String getMetricValue(){
        final String sHost = getQueryValue(Constants.NAGIOS_API_QUERY_PARAM_HOST);
        final String sMetric = getQueryValue(Constants.NAGIOS_API_QUERY_PARAM_METRIC);
        if (null != sHost && null != sMetric) {
            final MetricStore aStore = aMetricStoreFactory.getMetricStore();
            final ObservedMetricValue aValue = aStore.get(sHost, sMetric);
            if (null != aValue) {
                final JsonObject aRet = aValue.toJson();
                aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_ERROR, Boolean.FALSE);
                return aRet.toString();
            } else {
                StringBuilder aDetail = new StringBuilder();
                aDetail.append("you have asked for ").append(sMetric).append(" on ").
                        append(sHost).append(" but we have no values for that combinations");
                final JsonObject aRet = new JsonObject();
                aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_ERROR, Boolean.TRUE);
                aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_ERROR_DETAIL, aDetail.toString());
                setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return aRet.toString();
            }
        } else {
            StringBuilder aDetail = new StringBuilder();
            aDetail.append("you need to provide ").append(Constants.NAGIOS_API_QUERY_PARAM_HOST).
                    append(" and ").append(Constants.NAGIOS_API_QUERY_PARAM_METRIC);
            final JsonObject aRet = new JsonObject();
            aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_ERROR, Boolean.TRUE);
            aRet.addProperty(Constants.NAGIOS_JSON_PROPERTY_ERROR_DETAIL, aDetail.toString());
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return aRet.toString();
        }
    }
     
}
