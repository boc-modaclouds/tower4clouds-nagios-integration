# Tower4clouds Nagios Integration
This repository hosts software components - a Tower 4Clouds observer and a Nagios plugin - to be used for the integration of the advanced MODAClouds monitoring framework with the state-of-the-art open-source monitoring tool Nagios.

## The challenge
If you are operating a growing application landscape and the maintenance of your monitoring solution results in significant efforts the approach described here might be of interest for you. While you may use DevOps tools such as Puppet or Chef for automatically keeping the monitoring configuration up-to-date while you are spawning new and deleting outdated cloud resources, **extending the coverage** of various application specific metrics often results in a **serious amount of work**. Taking in account the growing count of application components, chances are high that you need to write many specialized monitoring agents for your favored monitoring system.

## The tools

### [Nagios](https://www.nagios.org/projects/nagios-core/) -  a state-of-the-art monitoring framework
In many companies Nagios is the tool of choice for IT infrastructure monitoring. It provides a solid framework and a very comprehensive coverage for monitoring of infrastructure KPIs. Plugins are available for tracking availability and performance of hardware and software components by means of various protocols such as SNMP, ICMP, SMTP and others. A large community of open source software developers is permanently extending the portfolio of plugins for the retrieval of metrics from popular software components such as databases and web servers.

In a nutshell a Nagios based setup consists of a **central service** that polls for monitoring information by invoking the aforementioned **plugins** according to its **configuration**. By defining thresholds for the metrics the correlation between value ranges and service states is established which allows for triggering actions on state transitions. A **web application** is available for visualizing the state of the monitored environment in various levels of detail.

While it is a widely used, reliable and extensible monitoring framework it has a limitation in thes sense that, except of the definition of dependencies, a correlation of metrics from different sources is not supported out of the box. It can be implemented in separate plugins but these plugins must take care of the data collection and data correlation at the same time which makes them a bit hard to maintain.

### [Tower 4Clouds](http://deib-polimi.github.io/tower4clouds/) - a MODAClouds runtime component

[MODAClouds](http://multiclouddevops.com/), an EU funded FP7 research project executed 2012-2015 has created methodology and tools for supporting the development and operation of multi-cloud applications. With its model driven approach it lets the stakeholders
* graphically model their architecture,
* choose the appropriate cloud resources to deploy to,
* enact the cloud resource provisioning and deployment,
* *monitor* the availability and performance of the application and the underlying resources
* and define corrective actions for QoS violations

Part of the MODAClouds run-time tools is Tower 4Clouds - an **RDF based monitoring system** that provides components for collection, transport and processing of monitoring data. **Custom [collectors](http://deib-polimi.github.io/tower4clouds/docs/data-collectors/)** can be created that fit best the applications and resources to be monitored. **[Monitoring rules](http://deib-polimi.github.io/tower4clouds/docs/rules/)** specify how data is processed by applying the **pipes and filters** pattern on one or more metric value streams. **Observers** can be registered to receive final and intermediate results of the processing stages.

As the complete MODAClouds methodology focuses on automation of the runtime part there is no fancy user interface available for Tower 4Clouds, however, an API is available to support integration with other systems.


### The glue - a Tower 4Clouds observer and a Nagios plugin

[BOC](https://www.boc-group.com), a member of the MODAClouds consortium has created the building blocks of an **integration middleware** between Tower 4Clouds and Nagios. The motivation for this step has been the fact, that Nagios was already well established in the operations teams of the company and the MODAClouds monitoring platform has proved to be a scalable and easy-to-extend supplement, especially when it comes to adding new metrics to be monitored.

The gateway functionality between Tower 4Clouds' **push** based **RDF** transport and the **poll** mechanisms of Nagios has been implemented by means of a custom observer that is registered for the relevant metrics at Tower 4Clouds (see [T4C documentation](http://deib-polimi.github.io/tower4clouds/docs/observers/) for details) and a Nagios plugin that accesses the observer's REST API to pick up the latest metric values. [Figure 1](#Figure1) below depicts the interaction between the involved components.
<a name="Figure1"></a>![Figure 1: Integrated monitoring solution](https://raw.githubusercontent.com/boc-modaclouds/tower4clouds-nagios-integration/master/img/architecture.png)

The observer is portable **Java application** that exposes two REST interfaces, one for receiving the observed metric values and one for serving the poll requests invoked through the Nagios plugin. The plugin itself is a **Python script** that is implemented in accordance with the Nagios API specification. Its command line syntax is:
```
check_t4c_metric.py <-w|-W> <warning-threshold> <-c|-C> <critical-threshold> <-h host> <-m metric>
       -c reports critical state if metric value is above threshold
       -w reports warning state if metric value is above threshold
       -C reports critical state if metric value is below threshold
       -W reports warning state if metric value is below threshold
```

## LICENCE
If you intend to reuse parts of it, please read [LICENSE.txt](./LICENCSE.TXT) first.