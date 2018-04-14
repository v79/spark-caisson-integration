package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.extensions.bind
import org.liamjd.spark.caisson.models.ComplexForm
import spark.ModelAndView
import spark.kotlin.get

data class GetStringForm(val just_a_string: String)
data class GetIntForm(val just_an_int: Int)

class GetController : AbstractController("/get"){
	init {
		get(path) {
			engine.render(ModelAndView(model,"get"))
		}

		get("getString") {
			val result = request.bind<GetStringForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model, "fragments/results"))
		}

		get("getComplex") {
			val result = request.bind<ComplexForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model, "fragments/results"))

		}

		get("getIntForm") {
			val result = request.bind<GetIntForm>()
			model.put("resultingString",result.toString())
			engine.render(ModelAndView(model,"fragments/results"))
		}
	}
}