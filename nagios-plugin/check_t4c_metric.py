#!/usr/bin/env python
# 
# Nagios check script for querying data collected by MODAClouds monitoring platform
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
import requests
import sys
from urllib import quote_plus as urlencode

OBSERVER_URL = 'http://localhost:8889/?host=%s&metric=%s'

def usage(msg):
    print "Usage: %s <-w|-W> <warning-threshold> <-c|-C> <critical-threashold> <-h host> <-m metric>" % sys.argv[0]
    print "       -c reports critical state if metric value is above threshold"
    print "       -w reports warning state if metric value is above threshold"
    print "       -C reports critical state if metric value is below threshold"
    print "       -W reports warning state if metric value is below threshold"
    print ""
    print "       Note: you must not mix upper-bound and lower-bound thresholds"
    print "       Error: %s" % msg
    print ""
    sys.exit(1)

class Thresholds(object):
    def __init__(self, w, c, W, C):
        self.w = w
        self.c = c
        self.W = W
        self.C = C
    def getStatus(self, value):
        if self.c and value > self.c: return 2
        elif self.C and value < self.C: return 2
        elif self.w and value > self.w: return 1
        elif self.W and value < self.W: return 1
        else: return 0

def parseCommandLine():
    w = c = W = C = host = metric = None
    try:
        optlist, args = getopt.getopt(sys.argv[1:], 'w:c:W:C:h:m:')
        if len(args): usage("arguments not expected")
        for opt in optlist:
            if len(opt) != 2: usage("unexpected option %s" % str(opt))
            if '-w' == opt[0]: w = float(opt[1])
            elif '-c' == opt[0]: c = float(opt[1])
            elif '-W' == opt[0]: W = float(opt[1])
            elif '-C' == opt[0]: C = float(opt[1])
            elif '-m' == opt[0]: metric = opt[1]
            elif '-h' == opt[0]: host = opt[1]
            else: usage("unexpected option %s" % str(opt))
        if (w and C) or (W and c):
            usage("mixing upper-bound and lower-bound thresholds")
        if not ((w and c) or (W and C)):
            usage("thresholds missing")
        if not (host and metric):
            usage("host or metric missing")
        if (w and w > c):
            usage("warning threshold must be less than critical threshold")
        if (W and W < C):
            usage("warning threshold must be greater than critical threshold")
        return [host, metric, Thresholds(w, c, W, C)]
    except Exception, ex:
        usage(str(ex))

def main():
    try :
        host, metric, thresholds = parseCommandLine()
        metric_json = requests.get(OBSERVER_URL % (urlencode(host), urlencode(metric))).json()
        if metric_json['error']:
            print 'UNKNOWN: error occured during check - %s ' % metric_json['errorDetail']
            return 3
        value = float(metric_json['value'])
        rv = thresholds.getStatus(value)
        messages = {
            0: 'OK: %s value is %f' % (metric, value),
            1: 'WARNING: %s value is %f' % (metric, value),
            2: 'CRITICAL: %s value is %f' % (metric, value)
        }
        print messages[rv]
        return rv
    except Exception, ex:
        print 'UNKNOWN: caught exception - %s ' % str(ex)
        return 3
        

if __name__ == '__main__':
    sys.exit(main())

