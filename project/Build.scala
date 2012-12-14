import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "mswordproto"
    val appVersion      = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		  "org.apache.poi" % "poi-scratchpad" % "3.8"
     )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
          resolvers ++= Seq("mvnrepository.com" at "http://mvnrepository.com/artifact/")
    )

}
