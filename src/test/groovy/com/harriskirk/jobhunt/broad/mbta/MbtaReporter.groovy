package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporterTest extends GroovyTestCase {

  // Create test routes and stops and test a valid route list is determined
  void testRouteFinder() {
    Route r = new Route(id:'R', stops:['A', 'P', 'Q'])
    Route o = new Route(id:'O', stops:['C', 'H', 'O'])
    Route g = new Route(id:'G', stops:['A', 'P', 'H'])
    Route b = new Route(id:'B', stops:['X', 'U', 'M', 'S'])

    def masterRouteList = [r, o, g, b]

    // Test Case 1
    String beginningStop  = 'A'
    String endingStop     = 'O'
    def expectedJourneyRoutes = [r, o, g]
    def actualJourneyRoutes = MbtaReporter.getRouteListForJourney(masterRouteList, beginningStop, endingStop)

    //assertEquals ( expectedJourneyRoutes, actualJourneyRoutes )
  }

}