package org.liamjd.spark.caisson

import org.liamjd.spark.caisson.controllers.ComplexController
import org.liamjd.spark.caisson.controllers.HomeController
import org.slf4j.LoggerFactory
import spark.kotlin.port
import spark.kotlin.staticFiles
import spark.servlet.SparkApplication
import java.util.*

class Server : SparkApplication {
	val logger = LoggerFactory.getLogger(Server::class.java)
	val thisPackage = this.javaClass.`package`

	constructor(args: Array<String>) {
		val portNumber: String? = System.getProperty("server.port")
		port(number = portNumber?.toInt() ?: 4569)

		staticFiles.location("/public")

		displayStartupMessage(portNumber?.toInt())

		// initialize controllers
		HomeController()
		ComplexController()

	}

	override fun init() {
		TODO("Not really necessary; the work is done in the constructor")
	}

	private fun displayStartupMessage(portNumber: Int?) {
		logger.info("=============================================================")
		logger.info("Kotlin Spark Route Tester Started")
		logger.info("Date: " + Date().toString())
		logger.info("OS: " + System.getProperty("os.name"))
		logger.info("Port: " + if (portNumber != null) portNumber else "4569")
		logger.info("JDBC URL: " + System.getenv("JDBC_DATABASE_URL"))
		logger.info("=============================================================")
	}
}