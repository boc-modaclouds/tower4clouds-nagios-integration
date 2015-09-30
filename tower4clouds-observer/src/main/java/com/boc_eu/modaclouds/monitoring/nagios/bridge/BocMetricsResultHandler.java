/**
 * @note Copyright\n
 *     	ADONIS:cloud infrastructure\n
 *      (C) COPYRIGHT BOC - Business Objectives Consulting 1996-2013\n
 *      All Rights Reserved\n
 *      Use, duplication or disclosure restricted by BOC Information Systems\n
 *      Vienna, 2013
 * @brief 
 * @author Core Development, 2013
 */


package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import it.polimi.modaclouds.monitoring.metrics_observer.ResultsHandler;
import it.polimi.modaclouds.monitoring.metrics_observer.model.Variable;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author stepan
 */
public class BocMetricsResultHandler extends ResultsHandler {
    
    private static final Logger aLog = LoggerFactory.getLogger(BocMetricsResultHandler.class);

    @Override
    public void getData(List<String> varNames, List<Map<String, Variable>> bindings) {
    
        // let's see what we get in here
        final boolean bDebug = true;
        if (bDebug) {
            aLog.debug("VAR NAMES:");
            for (final String sVarName : varNames) {
                aLog.debug(" -> {}", sVarName);
            }
            aLog.debug("BINDINGS:");
            for (final Map<String, Variable> aBinding : bindings) {
                for (final String sKey : aBinding.keySet()) {
                    aLog.debug(" -> {}: {}", sKey, aBinding.get(sKey).getValue());
                }
            }
        }
        
        aLog.warn("implementation for the addition of observed metric values to the store is missing ...");
    }
    
}
