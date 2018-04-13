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
//			val result = WebForm(request,GetStringForm::class).get() as GetStringForm
			val result = request.bind<GetStringForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model, "fragments/results"))
		}

		get("getComplex") {
//			val form = WebForm(request, modelClass = ComplexForm::class)
//			val getComplexForm = form.get() as ComplexForm
			val result = request.bind<ComplexForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model, "fragments/results"))

		}

		get("getIntForm") {
//			val intForm = WebForm(request, GetIntForm::class).get() as GetIntForm
			val result = request.bind<GetIntForm>()
			model.put("resultingString",result.toString())
			engine.render(ModelAndView(model,"fragments/results"))
		}
	}
}