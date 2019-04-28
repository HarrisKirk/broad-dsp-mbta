package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporterTest extends GroovyTestCase {

  void testStopsWithTwoOrMoreRoutes() {
    
    def masterRouteList = [
      new Route(id:'R', stops:['A', 'P', 'Q']),
      new Route(id:'O', stops:['C', 'H', 'O']),
      new Route(id:'G', stops:['L', 'P', 'H']),
      new Route(id:'B', stops:['X', 'U', 'M', 'S']),
    ]

    // Test Case 1
    def expectedStopsWithMultiRoutes = [ new Stop(id:'P', routes:[new Route(id:'G'), new Route(id:'R')]) ]
    def actualStopsWithMultiRoutes = MbtaReporter.getStopsWithMultiRoutes ( masterRouteList )

    assertEquals ( expectedStopsWithMultiRoutes, actualStopsWithMultiRoutes )
  }

}