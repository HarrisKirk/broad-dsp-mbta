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
      expectedStops: [P: ['G', 'R'].toSet() ]
    ]

    // Map expectedStopsWithMultiRoutes = [P: ['G', 'R'].toSet() ]
    Map expectedStopsWithMultiRoutes = testCase1.expectedStops
    def actualStopsWithMultiRoutes = MbtaReporter.getStopsWithMultiRoutes ( testCase1.routeList )

    assertEquals ( expectedStopsWithMultiRoutes.keySet(),  actualStopsWithMultiRoutes.keySet() )
    assertEquals ( expectedStopsWithMultiRoutes.get('P').toSet().toString(), actualStopsWithMultiRoutes.get('P').toSet().toString() )
  }

}