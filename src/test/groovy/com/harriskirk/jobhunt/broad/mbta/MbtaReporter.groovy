package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporterTest extends GroovyTestCase {

  void testStopsWithTwoOrMoreRoutes() {
    
    def testCase1 = [
      routeList: [
        new Route(id:'Red',     stops:['A', 'P', 'Q']),
        new Route(id:'Orange',  stops:['C', 'H', 'O']),
        new Route(id:'Green',   stops:['L', 'P', 'Y']),
        new Route(id:'Blue',    stops:['X', 'U', 'M', 'S']),
      ],
      expectedStops: [P: ['G', 'R']]
    ]

    def testCase2 = [
      routeList: [
        new Route(id:'Red',     stops:['A', 'P', 'Q']),
        new Route(id:'Orange',  stops:['C', 'H', 'O']),
        new Route(id:'Green',   stops:['L', 'P', 'Y']),
        new Route(id:'Yellow',  stops:['D', 'E', 'H']),
        new Route(id:'Blue',    stops:['X', 'U', 'M', 'S']),
      ],
      expectedStops: [P: ['Green', 'Red'], H: ['Orange', 'Yellow']]
    ]

    def testCases = [testCase1, testCase2]
    testCases.each { testCase ->
      Map expectedStopsWithMultiRoutes = testCase.expectedStops
      def actualStopsWithMultiRoutes = MbtaReporter.getStopsWithMultiRoutes ( testCase.routeList )

      Set expectedStopList = expectedStopsWithMultiRoutes.keySet()
      Set actualStopList   = actualStopsWithMultiRoutes.keySet()
      // List of stops as expected?
      assertEquals ( expectedStopList, actualStopList )

      expectedStopList.each {
        String expectedRoutes = new TreeSet (expectedStopsWithMultiRoutes.get(it))
        String actualRoutes   = new TreeSet (actualStopsWithMultiRoutes.get(it))
        // List of routes per stop as expected?
        assertEquals ( expectedRoutes, actualRoutes ) 
      }
    }
  }
}

