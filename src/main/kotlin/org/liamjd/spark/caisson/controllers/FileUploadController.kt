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
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.Part
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class MultiPartForm(raw: HttpServletRequest, multiPartModel: KClass<*>) {
	private lateinit var modelObject: Any

	init {

//		val caissonSuperclass: KClass<*>? = multiPartModel.superclasses.find { it.qualifiedName == "org.liamjd.spark.caisson.controllers.CaissonMultipartForm" }
//		if(caissonSuperclass == null) {
//			// abort! throw exception
//			throw Exception("${multiPartModel.qualifiedName} does not extend org.liamjd.spark.caisson.controllers.CaissonMultipartForm")
//		}

		if (raw.getAttribute("org.eclipse.jetty.multipartConfig") == null) {
			val multipartConfigElement = MultipartConfigElement(System.getProperty("java.io.tmpdir"))
			raw.setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement)
		}

		val primaryConstructor = multiPartModel.primaryConstructor
		val constructorParams: MutableMap<KParameter, Any?> = mutableMapOf()

		if(primaryConstructor != null) {

			for (constructorKParam in primaryConstructor.parameters) {
				println("MultiPartForm constructor param: ${constructorKParam.name} -> ${constructorKParam.type}")



			}

//			primaryConstructor.parameters.find {  }

		}




		val partNames = raw.parts.map { it.name }

		// need to map the part names with the parameter names
		// but what about the implicit values such as contentType, name, size, submittedFileName
		// and headers such as content-disposition and content-type

		println("raw part names: $partNames")

//		raw.parts.forEach {
//			println("raw part: ${it.name}")
//		}

		if(primaryConstructor != null) {
			modelObject = primaryConstructor.callBy(constructorParams)
		}
	}

	/**
	 * Return the generated model object of the form data, or null
	 */
	fun get(): Any? {
		if (!::modelObject.isInitialized || modelObject == null) {
			return null
		}
		return modelObject
	}

}

class CaissonMultipartContent(val contentType: String, val size: Long, val bytes: ByteArray, val originalFileName: String) {

}

data class MyFileUpload(val uploaded_file: String,
				   val upload_description: String,
				   val fileContents: CaissonMultipartContent) {

}

class FileUploadController : AbstractController(path = "/upload") {

	val uploadDir = File("upload")

	init {
		uploadDir.mkdir() // create the upload directory if it doesn't exist


		get(path) {
			engine.render(ModelAndView(model, "fileUpload"))
		}

		post(path) {

			val myMultiPartForm = MultiPartForm(request.raw(), MyFileUpload::class)

			val myUploadedFile = myMultiPartForm.get() as MyFileUpload
			val bytes = myUploadedFile.fileContents.bytes









			val tempFile = Files.createTempFile(uploadDir.toPath(), "", "")

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

			val readBytes: ByteArray = request.raw().getPart("uploaded_file").inputStream.readBytes()

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



