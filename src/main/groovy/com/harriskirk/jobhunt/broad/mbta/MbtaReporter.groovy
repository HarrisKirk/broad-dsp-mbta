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
    println "Question 1: Routes"
    println "------------------"
    routes.each {
      println it.longName
    }

    /* 
      Question 2
    */
    println ""
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
    println "...${APP_NAME} [OK]"
  }

  static Map getStopsWithMultiRoutes(List routes) {
    Map stopsWithRoutes = [:] // map of stop names and list of route names that pass thru that stop
    for (int i=0; i<routes.size-1; i++ ) {
      // println "ROUTE " + routes[i].toString().padRight(20) + ' with stops ' + routes[i].stops
      for (int j=i+1; j<routes.size(); j++ ) {
        // println "    route " + routes[j].toString().padRight(20) + ' with stops ' + routes[j].stops

        def commonStops = routes[i].stops.intersect(routes[j].stops) // stops in common to both routes
        // println ('     common stops are: ' + commonStops)
        commonStops.each {
          def routesFound = []
          def temp = stopsWithRoutes.get(it)
          if (temp) {
            routesFound = temp
          }
          // println "    Routes already existed for $it : " + routesFound
          if (!routesFound || !routesFound.contains(it)) {
            // print '            will add routes ' + routes[i] + ' and ' + routes[j]
            routesFound << routes[i]
            routesFound << routes[j]
            stopsWithRoutes.put( it, routesFound.toSet())
          }
          // println "    Now stopsWithRoutes is: " + stopsWithRoutes
        }
      }
    }
    // println "stopsWithRoutes: " + stopsWithRoutes

    return stopsWithRoutes
  }

  // recursive

}