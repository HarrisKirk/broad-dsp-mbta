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
    def routes = object.data

    routes.each { route ->
      println "Route's long name = $route.attributes.long_name" 
    } 

    /* 
      Question 2
    */
    String MBTA_ROUTES_URL = 'https://api-v3.mbta.com/stops?filter[route]=Red'
    json = new URL(MBTA_ROUTES_URL).text

    object = jsonSlurper.parseText(json)
    object.data.each {
      println "STOP: $it.attributes.name"
    }    

    println "...${APP_NAME} [OK]"
  }


}