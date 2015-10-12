#!/usr/bin/env python
# 
# Registration with all available T4C metrics
#
# (c)2015 BOC Information Systems GmbH
# All rights reserved.
# 
# Redistribution and use in source and binary forms, with or
# without modification, are permitted provided that the following
# conditions are met:
# 
#  1. Redistributions of source code must retain the above
#     copyright notice, this list of conditions and the
#     following disclaimer.
#  2. Redistributions in binary form must reproduce the above
#     copyright notice, this list of conditions and the following
#     disclaimer in the documentation and/or other materials
#     provided with the distribution.
#  3. Neither the name of the copyright holder nor the names of its
#     contributors may be used to endorse or promote products derived
#     from this software without specific prior written permission.
# 
# THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
# AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
# THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
# EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
# PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
# OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
# WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
# OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
# OF THE POSSIBILITY OF SUCH DAMAGE.

import getopt
import json
import requests
import sys
#from urllib import quote_plus as urlencode

# TODO:
# GET available metrics
# POST observer to metrics

# default values
OBSERVER_URL = 'http://109.231.126.191:8080/'
MM_URL = 'http://109.231.126.51:8170/v1/'
METRICS = 'BpmsMetricCollected'

def usage(msg):
    print "Usage: %s [-u|--manager <mm-url>] [-o|--observer <observer-url>] [-m|--metrics <list-of-metrics>]" % sys.argv[0]
    print "       Error: %s" % msg
    print ""
    sys.exit(1)

def parseCommandLine():
    mm_url = observer_url = metrics = None
    try:
        optlist, args = getopt.getopt(sys.argv[1:], 'u:o:m:', ['manager=', 'observer=', 'metrics='])
        if len(args): usage("arguments not expected")
        for opt in optlist:
            if len(opt) != 2: usage('unexpected command line arguments %s' % str(opt))
            elif opt[0] in ('-u', '--manager'): mm_url = opt[1]
            elif opt[0] in ('-o', '--observer'): observer = opt[1]
            elif opt[0] in ('-m', '--metrics'): metrics = opt[1]
            else: usage("unexpected option %s" % str(opt))
        if not mm_url: mm_url = MM_URL
        while len(mm_url) > 0 and mm_url[len(mm_url)-1] == '/':
            mm_url = mm_url[:len(mm_url)-1]
        if not observer_url: observer_url = OBSERVER_URL
        if not metrics: metrics = METRICS
        return [mm_url, observer_url, metrics.split(',')]
    except Exception, ex:
        usage(str(ex))

class Registrator(object):
    def __init__(self, mm_url, observer_url, metrics):
        self.__mm_url = mm_url
        self.__observer_url = observer_url
        self.__metrics = metrics

    def register(self):
        available_metrics = self.__getAvailableMetrics()
        for metric in self.__metrics:
            if unicode(metric.strip()) in available_metrics:
                observers = self.__getObservers(metric)
                already_registered = False
                for observer in observers:
                    if self.__observer_url in observer['callbackUrl']:
                        already_registered = True
                        break
                if not already_registered:
                    registration = self.__registerObserver(metric)
                    print registration

    def __getAvailableMetrics(self):
        metrics = requests.get('%s/metrics' % self.__mm_url).json()
        print 'metrics obtained from T4C manager : %s' % str(metrics)
        return metrics

    def __getObservers(self, metric):
        observers = requests.get('%s/metrics/%s/observers' % (self.__mm_url, metric)).json()
        print 'observers obtained from T4C manager : %s' % str(observers)
        return observers

    def __registerObserver(self, metric):
        print 'registering for metric %s' % metric
        payload = { 'format': 'TOWER/JSON',
                    'protocol': 'HTTP',
                    'callbackUrl': self.__observer_url
                    }
        return requests.post('%s/metrics/%s/observers' % (self.__mm_url, metric), data = json.dumps(payload))

def main():
    try :
        mm_url, observer_url, metrics = parseCommandLine()
        reg = Registrator(mm_url, observer_url, metrics)
        reg.register()
    except Exception, ex:
        print 'Caught exception - %s ' % str(ex)
        return 1
        
if __name__ == '__main__':
    sys.exit(main())

