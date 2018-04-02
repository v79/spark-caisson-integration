package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.Form
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
			val result = Form(request.queryMap().toMap(),GetStringForm::class).get() as GetStringForm
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model, "fragments/results"))
		}

		get("getComplex") {
			val form = Form(request.queryMap().toMap(), modelClass = ComplexForm::class)
			val getComplexForm = form.get() as ComplexForm
			model.put("resultingString",getComplexForm.toString())

			engine.render(ModelAndView(model, "fragments/results"))

		}

		get("getIntForm") {
			val intForm = Form(request.queryMap().toMap(), GetIntForm::class).get() as GetIntForm
			model.put("resultingString",intForm.toString())
			engine.render(ModelAndView(model,"fragments/results"))
		}
	}
}