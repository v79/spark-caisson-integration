package org.liamjd.spark.caisson.controllers

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import org.liamjd.caisson.annotations.CController
import org.liamjd.caisson.annotations.CConverter
import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.convertors.Converter
import org.liamjd.caisson.extensions.bind
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post


class JsonPersonConverter : Converter {
	val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
	override fun convert(from: String): JsonPerson? {
		val jsonAdapter = moshi.adapter<Any>(JsonPerson::class.java)

		val modelObject = jsonAdapter.fromJson(from)
		return modelObject as JsonPerson
	}

}


data class JsonPerson(val fname: String, val surname: String)

data class JsonPersonWrapper(val jpersonID: Int, @CConverter(converterClass = JsonPersonConverter::class) val jpersonJson: JsonPerson)



@CController
class JsonController : AbstractController("/json") {
	val resultView = "fragments/results"
	init {
		get(path) {
			engine.render(ModelAndView(model,"json"))
		}

		post("jsonPersonForm") {
			val result = request.bind<JsonPersonWrapper>()

			model.put("resultingString",result.toString())
			engine.render(ModelAndView(model,resultView))
		}
	}

}