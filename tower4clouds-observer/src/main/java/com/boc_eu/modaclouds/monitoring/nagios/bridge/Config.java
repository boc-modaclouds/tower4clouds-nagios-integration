/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boc_eu.modaclouds.monitoring.nagios.bridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Representation of the application's configuration. It has some default values and shall be
 * extended to parse a properties file and maybe also read the command line arguments.
 *
 * @author sseycek
 */
public class Config
{
  
  private static final Logger aLog = LoggerFactory.getLogger (Config.class);
  
  private final int nObserverPort = 8080;
  private final int nNagiosPort = 8088;
  private static Config aInstance;
  
  private Config ()
  {}
  
  public int getObserverPort ()
  {
    return this.nObserverPort;
  }
  
  public int getNagiosPort ()
  {
    return this.nNagiosPort;
  }
  
  public static synchronized Config getInstance ()
  {
    if (null == aInstance)
    {
      aInstance = new Config ();
      aLog.debug ("created Config singleton instance");
    }
    return aInstance;
  }
  
}
