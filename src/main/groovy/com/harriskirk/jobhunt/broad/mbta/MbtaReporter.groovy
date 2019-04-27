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
    def jsonSlurper = new JsonSlurper()
    String json = new URL(MBTA_ROUTES_METHOD_2).text

    def object = jsonSlurper.parseText(json)

    List<Route> routes = []
    object.data.each { route ->
      routes << new Route( longName: route.attributes.long_name.toString(), id: route.id.toString())
    } 
    routes.each {
      println 'Long name: ' + it.id
    }

    /* 
      Question 2
    */
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
    
    sortedRoutesByStops.each { println it.id + ' ' + it.stops.size() }

    println "Route with fewest stops is " + sortedRoutesByStops.first().longName.padRight(25, '.') + '(' + sortedRoutesByStops.first().stops.size() + ' stops)'
    println "Route with most stops is   " + sortedRoutesByStops.last().longName.padRight(25, '.') + '(' + sortedRoutesByStops.last().stops.size() + ' stops)'

    println ""
    println "...${APP_NAME} [OK]"
  }


}