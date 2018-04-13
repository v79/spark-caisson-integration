package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.extensions.bind
import org.liamjd.spark.caisson.models.MultipleFileUpload
import org.liamjd.spark.caisson.models.SimpleFileUpload
import spark.ModelAndView
import spark.Request
import spark.kotlin.get
import spark.kotlin.post
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.servlet.MultipartConfigElement
import javax.servlet.http.Part


class FileUploadController : AbstractController(path = "/upload") {

	val uploadDir = File("upload")

	init {
		uploadDir.mkdir() // create the upload directory if it doesn't exist


		get(path) {
			engine.render(ModelAndView(model, "fileUpload"))
		}

		post("/upload2") {

			if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
				val multipartConfigElement = MultipartConfigElement(System.getProperty("java.io.tmpdir"))
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement)
			}

			println("----- upload 2")
			request.raw().parts.forEach {
				println("\tpart: ${it.name}")
				println("\t\t ${it.submittedFileName}")
			}

			val part = request.raw().getPart("upload2")
			println("getPart(upload2) gives us ${part}")
			val part2 = request.raw().getPart("upload2")
			println("getPart(upload2) again gives us ${part2}")

			val files = request.bind<MultipleFileUpload>(arrayListOf("upload2"))

			engine.render(ModelAndView(model, "fragments/results"))
		}

		post(path) {
			val uploadName = "upload"

			val myUploadedFile = request.bind<SimpleFileUpload>(arrayListOf(uploadName))
			val contentType = myUploadedFile?.upload?.contentType
			val stream = myUploadedFile?.upload?.stream
			val tempFile = Files.createTempFile(uploadDir.toPath(), "", "")

			println("------- File uploaded ------")

			println("-- debugQueryMap")
			debugQueryMap(request)
			println("-- debugParams")
			debugParams(request)
			debugRawParts(request)

			myUploadedFile?.upload.run {
				println("ContentType: " + this?.contentType)
				println("OriginalFileName: " + this?.originalFileName)
				println("Size: " + this?.size)
				println("Bytes size: " + this?.stream?.readBytes(this.size.toInt())?.size)
			}

			myUploadedFile?.upload?.stream?.use { inputStream: InputStream? ->
				Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
				println(tempFile.fileName.toString())
			}

			model.put("resultingString", myUploadedFile?.upload?.originalFileName!!)

			engine.render(ModelAndView(model, "fragments/results"))
		}
	}

	private fun getFileName(part: Part): String? {
		for (cd in part.getHeader("content-disposition").split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
			if (cd.trim { it <= ' ' }.startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim { it <= ' ' }.replace("\"", "")
			}
		}
		return null
	}

	private fun debugRawParts(request: Request) {
		println("---- debugRawParts")
		/*println("\t headers:")
		request.raw().headerNames.toList().forEach {
			println("\t\t $it")
		}*/
		println("\t parts:")
		request.raw().parts.forEach {
			println("\t\t name: ${it.name}, submittedFileName: ${it.submittedFileName}, contentType: ${it.contentType}, size: ${it.size}")
			println("\t\t\t headers: ${it.headerNames}")
		}
	}
}



