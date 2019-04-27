package com.harriskirk.jobhunt.broad.mbta

import groovy.json.*

public class MbtaReporter {

/*
    Documentation
    https://api-v3.mbta.com/docs/swagger/index.html
*/

    public static void main(String [] args ) {
      String MBTA_ROUTES = 'https://api-v3.mbta.com/routes'

      println "MBTA Reporter Start..."
      def jsonSlurper = new JsonSlurper()
      String json = new URL(MBTA_ROUTES).text
      println json

      def object = jsonSlurper.parseText(json)
      def routes = object.data

      routes.each { println "\nroute=$it" } 


      // BBBaseMap.each { proj, projObj ->
      //   println ''
      //   println '-' * 120
      //   println proj.padRight(10) + ',' + projObj.name.padRight(30) + "," + 'Admins: ' + projObj.admins.join('; ')
      //   println '-' * 120
      //   projObj.repos.each { repo ->
      //     println '   ' + repo.name.padRight(50) + 'branches: ' + repo.branches.size()
      //   }
      //   println ''
      // }

      // println ''
      // println ''

      // println '=' * 100
      // println 'USER REPO REPORT'
      // println '=' * 100
      // def usersToIgnore = ['~SSHANKAR', '~MHERZOG', '~JVITORINO', '~DBRANDON', '~BJI', '~CCONNELL', 
      //         '~ASULAIMAN', '~ZCHARTER', '~JPEREIRA', '~LPRABHAKAR', '~JLU', '~PSUSARLA' ]
      // def repos = bbr.getAllRepos()
      // def userRepos = repos.findAll { it.project.key.startsWith('~') && !usersToIgnore.contains(it.project.key) }
      // def sortedUsers = userRepos.sort { a,b ->
      //   a.project.key <=> b.project.key
      // }
      // sortedUsers.each {
      //   println it.name.padRight(40) + it.project.key
      // }
      // println "Size = " + sortedUsers.size()
      println "...ENDED OK"
    }

    private def getAllRepos() {
        def ri = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/repos?limit=800", this.username, this.password)
        def json = ri.getRESTResponse()
        def jsonSlurper = new JsonSlurper()
        def object = jsonSlurper.parseText(json)
        def repos = object.values        
    }

    private def getAdmins() {

        def ri = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/projects", this.username, this.password)
        def json = ri.getRESTResponse()

        def permissionMap = [:]

        def jsonSlurper = new JsonSlurper()
        def object = jsonSlurper.parseText(json)
        def projList = object.values
        projList.each {
          json = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/projects/${it.key}/permissions/users", this.username, this.password).getRESTResponse()
          def repoObject = jsonSlurper.parseText(json)
          def permissionsList = repoObject.values
          def adminNames = permissionsList.findAll {it.permission == "PROJECT_ADMIN"}.collect {it.user.emailAddress - '@wolterskluwer.com' }

          permissionMap.put (it.key, adminNames)
        }
        return permissionMap
    }

    private def getProjects(def adminMap) {
      def bbmap = [:]
      def ri = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/projects", this.username, this.password)
      def json = ri.getRESTResponse()

      def jsonSlurper = new JsonSlurper()
      def object = jsonSlurper.parseText(json)
      def projList = object.values

      projList.each { proj ->
        json = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/projects/${proj.key}/repos", this.username, this.password).getRESTResponse()
        def repoObject = jsonSlurper.parseText(json)
        def repoList = repoObject.values
        def repositoryList = []

        repoList.each { repo ->
          def repoMap = [:]
          repoMap.name = repo.slug
          repoMap.branches = getBranchesInRepo(proj, repo)
          repositoryList << repoMap
        }
        def projMap = [:]
        projMap.name = proj.name
        projMap.description = proj.description
        projMap.admins = adminMap.get(proj.key)
        projMap.repos = repositoryList

        bbmap.put (proj.key, projMap)
      }
      return bbmap

    }

    private def getBranchesInRepo(def proj, def repo) {
		  def json = new RESTInvoker ("${BB_BASE_URL}/rest/api/1.0/projects/${proj.key}/repos/${repo.slug}/branches", this.username, this.password).getRESTResponse()
      def jsonSlurper = new JsonSlurper()
			def branchObject = jsonSlurper.parseText(json)
			def branchList = branchObject.values.collect{it.displayId}
      return branchList
    }
}