package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import spark.ModelAndView
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

		post(path) {
			println("In /upload POST")

			val tempFile = Files.createTempFile(uploadDir.toPath(), "", "")
			println("tempFile: $tempFile")

			// This part seems really important!
			if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
				val multipartConfigElement = MultipartConfigElement(System.getProperty("java.io.tmpdir"))
				request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement)
			}


			val file = request.raw().getPart("uploaded_file")

			file.run {
				println("ContentType: " + this.contentType)
				println("Name: " + this.name)
				println("Size: " + this.size)
				println("submittedFileName: " + this.submittedFileName)
				this.headerNames.forEach {
					// content-disposition -> form-data; name="uploaded_file"; filename="D:\Development\Android\FuelRecorder\app\src\main\res\mipmap-hdpi\ic_launcher.png"
					// content-type -> image/png
					println("\t header: ${it} -> " + this.getHeader(it))
				}
			}


			request.raw().getPart("uploaded_file").inputStream.use {
				inputStream: InputStream? -> Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
				println(tempFile.fileName.toString())  }

			model.put("resultingString", getFileName(request.raw().getPart("uploaded_file")).toString())
			engine.render(ModelAndView(model,"fragments/results"))


			/*println("\trequest: ${request} .raw() ${request.raw()} .getPart()" + request.raw().getPart("uploaded_file"))
			debugQueryMap(request)
			val tempFile = Files.createTempFile(uploadDir.toPath(), "", "")
			request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))

			request.raw().getPart("uploaded_file").inputStream.use { inputStream: InputStream? -> Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING)  }
			logger.info(getFileName(request.raw().getPart("uploaded_file")) + "' saved as '" + tempFile.toAbsolutePath() + "'")

			model.put("resultingString", getFileName(request.raw().getPart("uploaded_file")).toString())
			engine.render(ModelAndView(model,"fileUpload"))*/
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
}



