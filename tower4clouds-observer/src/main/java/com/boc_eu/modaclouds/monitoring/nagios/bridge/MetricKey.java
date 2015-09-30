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
class MetricKey {

    private final String sHost;
    private final String sMetric;

    public MetricKey(final String sHost, final String sMetric) {
        this.sHost = sHost;
        this.sMetric = sMetric;
    }

    public String getHost() {
        return sHost;
    }

    public String getMetric() {
        return sMetric;
    }

    @Override
    public String toString() {
        StringBuilder aSB = new StringBuilder();
        aSB.append(getMetric()).append("@").append(getHost());
        return aSB.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MetricKey other = (MetricKey) obj;
        if ((this.sHost == null) ? (other.sHost != null) : !this.sHost.equals(other.sHost)) {
            return false;
        }
        if ((this.sMetric == null) ? (other.sMetric != null) : !this.sMetric.equals(other.sMetric)) {
            return false;
        }
        return true;
    }
}
