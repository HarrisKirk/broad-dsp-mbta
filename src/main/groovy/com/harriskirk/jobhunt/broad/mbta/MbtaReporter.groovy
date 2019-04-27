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
      println "ROUTE: " + route.id
      json = new URL(MBTA_STOPS_URL + route.id ).text
      object = jsonSlurper.parseText(json)
      object.data.each {
        println "       STOP: $it.attributes.name"
      }    
    }

    println ""

    println "...${APP_NAME} [OK]"
  }


}