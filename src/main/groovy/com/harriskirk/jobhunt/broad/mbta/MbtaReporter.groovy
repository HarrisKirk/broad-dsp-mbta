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

    println "ROUTES per STOP"
    List routesPerStop = getStopsWithMultiRoutes(routes)
    routesPerStop.each { k, v ->
      println k
    }

    println ""
    println "...${APP_NAME} [OK]"
  }

  static List getStopsWithMultiRoutes(List masterRouteList) {
    // def routesPerStop = [:] // map of stop names and list of route names that pass thru that stop
    // for (int i=0; i<routes.size-1; i++ ) {
    //   println "ROUTE " + routes[i].toString().padRight(30) //+ ' with stops ' + routes[i].stops
    //   for (int j=i+1; j<routes.size(); j++ ) {
    //     println "    route " + routes[j].toString().padRight(30)// + ' with stops ' + routes[j].stops

    //     def commonStops = routes[i].stops.intersect(routes[j].stops)
    //     println ('     common stops are: ' + commonStops)
    //     commonStops.each {
    //       def routesFound = routesPerStop.get(it)
    //       if (routesFound && !routesFound.contains(it)) {
    //         routesFound << routes[i]
    //         routesFound << routes[j]
    //         routesPerStop[it] = routesFound
    //       }
    //     }
    //     //commonStops.each { routesPerStop[it] = }
    //   }
    // }
    return []
  }

  // recursive

}