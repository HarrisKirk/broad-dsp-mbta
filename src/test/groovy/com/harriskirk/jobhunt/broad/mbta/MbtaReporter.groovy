package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporterTest extends GroovyTestCase {

  void testStopsWithTwoOrMoreRoutes() {
    
    def testCase1 = [
      routeList: [
        new Route(id:'R', stops:['A', 'P', 'Q']),
        new Route(id:'O', stops:['C', 'H', 'O']),
        new Route(id:'G', stops:['L', 'P', 'Y']),
        new Route(id:'B', stops:['X', 'U', 'M', 'S']),
      ],
      expectedStops: [P: ['G', 'R']]
    ]

    Map expectedStopsWithMultiRoutes = testCase1.expectedStops
    def actualStopsWithMultiRoutes = MbtaReporter.getStopsWithMultiRoutes ( testCase1.routeList )

    Set expectedStopList = expectedStopsWithMultiRoutes.keySet()
    Set actualStopList   = actualStopsWithMultiRoutes.keySet()
    assertEquals ( expectedStopList.toSet().toString(), actualStopList.toSet().toString() )

    expectedStopList.each {
     String expectedRoutes = new TreeSet (expectedStopsWithMultiRoutes.get(it))
     String actualRoutes   = new TreeSet (actualStopsWithMultiRoutes.get(it))
     assertEquals ( expectedRoutes, actualRoutes ) 
    }
  }
}

