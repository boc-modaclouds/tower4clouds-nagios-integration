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

import it.polimi.modaclouds.monitoring.metrics_observer.MetricsObServer;

/**
 * @author stepan
 */
public class BocMetricsObserver extends MetricsObServer
{
  
  public BocMetricsObserver (final int nPort)
  {
    super (nPort, BocMetricsResultHandler.class);
  }
  
}
