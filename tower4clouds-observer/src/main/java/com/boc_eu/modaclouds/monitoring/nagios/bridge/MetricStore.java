/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

/**
 *
 * @author sseycek
 */
public interface MetricStore {
    
    public void put (final ObservedMetricValue aValue);
    
    public ObservedMetricValue get (final String sHost, final String sMetric);
    
}
