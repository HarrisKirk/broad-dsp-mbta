package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporterTest extends GroovyTestCase {

  void testStopsWithTwoOrMoreRoutes() {
    
    def masterRouteList = [
      new Route(id:'R', stops:['A', 'P', 'Q']),
      new Route(id:'O', stops:['C', 'H', 'O']),
      new Route(id:'G', stops:['L', 'P', 'Y']),
      new Route(id:'B', stops:['X', 'U', 'M', 'S']),
    ]

    Map expectedStopsWithMultiRoutes = [P: ['G', 'R'].toSet() ]
    def actualStopsWithMultiRoutes = MbtaReporter.getStopsWithMultiRoutes ( masterRouteList )

    assertEquals ( expectedStopsWithMultiRoutes.keySet(),  actualStopsWithMultiRoutes.keySet() )
    assertEquals ( expectedStopsWithMultiRoutes.get('P').toSet().toString(), actualStopsWithMultiRoutes.get('P').toSet().toString() )
  }

}