package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporter {

/*
    API Documentation
    https://api-v3.mbta.com/docs/swagger/index.html
*/

  public static final String APP_NAME = "MBTA_REPORTER"

  public static void main(String [] args ) {
    /* 
      Question 1
    */
    //String MBTA_ROUTES_METHOD_1 = 'https://api-v3.mbta.com/routes'
    String MBTA_ROUTES_METHOD_2 = 'https://api-v3.mbta.com/routes?filter[type]=0,1'

    println "${APP_NAME} Start..."
    println ""
    def jsonSlurper = new JsonSlurper()
    String json = new URL(MBTA_ROUTES_METHOD_2).text

    def object = jsonSlurper.parseText(json)

    List<Route> routes = []
    object.data.each { route ->
      routes << new Route( longName: route.attributes.long_name.toString(), id: route.id.toString())
    } 
    println "------------------"
    println "Question 1: Routes"
    println "------------------"
    routes.each {
      println it.longName
    }

    /* 
      Question 2
    */
    println ""
    println "-----------------"
    println "Question 2: Stops"
    println "-----------------"
    String MBTA_STOPS_URL = 'https://api-v3.mbta.com/stops?filter[route]='
    routes.each { route ->
      json = new URL(MBTA_STOPS_URL + route.id ).text
      List stops = []
      object = jsonSlurper.parseText(json)
      object.data.each {
        stops << it.attributes.name.toString()
      }    
      route.stops = stops
    }

    List sortedRoutesByStops = routes.sort { it.stops.size() } 
    
    def routeFewestStops = sortedRoutesByStops.first()
    def routeMostStops   = sortedRoutesByStops.last()
    println "(1) Route with fewest stops is: " + routeFewestStops.longName.padRight(25, '.') + ' (' + routeFewestStops.stops.size() + ' stops)'
    println "(2) Route with most stops is:   " + routeMostStops.longName.padRight(25, '.') +  ' (' + routeMostStops.stops.size() + ' stops)'
    println "(3) Stops with multiple routes going through it."
    Map multiRouteStops = getStopsWithMultiRoutes(routes)
    multiRouteStops.each { k, v ->
      println k.padRight(25) + v
    }

    println ""
    println "----------------------------"
    println "Question 3: Stop A to Stop B"
    println "----------------------------"
    String beginStop  = System.properties['BEGIN_STOP'] // todo input system property
    String endStop    = System.properties['END_STOP']

    println 'BEGIN_STOP = ' + beginStop
    println 'END_STOP   = ' + endStop

    String beginRoute = routes.find { it.stops.contains(beginStop) } // What if stop on >1 route?
    String endRoute   = routes.find { it.stops.contains(endStop) } // what if stop on >1 route?
    // println beginRoute + ' on ' + beginRoute + ' ' + beginRoute.getClass().getName()
    // println endRoute + ' on ' + endRoute + ' ' + beginRoute.getClass().getName()

    List journeyRoutes = [] // routes that can be taken from beginStop - endStop

    // Case 1 - no transfer needed
    if (beginRoute == endRoute) {
      journeyRoutes = [beginRoute]
    }

    // Case 2 - 1 transfer (ie, there is a stop having both routes)
    multiRouteStops.each { k, connectingRoutes ->
      // println connectingRoutes  
      // connectingRoutes.each { println '   ' + it + ' ' + it.getClass().getName() }
      if (connectingRoutes.find{ it.id == beginRoute} && connectingRoutes.find{ it.id == endRoute})  {
        journeyRoutes = [beginRoute, endRoute]
      }
    }

    println "journey routes: " + journeyRoutes.unique()


    println ""
    println "...${APP_NAME} [OK]"
  }

  static Map getStopsWithMultiRoutes(List routes) {
    Map stopsWithRoutes = [:] // map of stop names and list of route names that pass thru that stop
    for (int i=0; i<routes.size-1; i++ ) {
      for (int j=i+1; j<routes.size(); j++ ) {
        def commonStops = routes[i].stops.intersect(routes[j].stops) // stops in common to both routes
        commonStops.each {
          def routesFound = []
          def temp = stopsWithRoutes.get(it)
          if (temp) {
            routesFound = temp
          }
          if (!routesFound || !routesFound.contains(it)) {
            routesFound << routes[i]
            routesFound << routes[j]
            stopsWithRoutes.put( it, routesFound.toSet())
          }
        }
      }
    }

    return stopsWithRoutes
  }

}