package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.extensions.bind
import org.liamjd.spark.caisson.models.ComplexForm
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post

class ComplexController : AbstractController("/complex") {
	val resultView = "fragments/results"

	init {
		get(path) {
			engine.render(ModelAndView(model,"complex"))
		}

		post("complexForm") {
//			val form = WebForm(request,ComplexForm::class)
//			val result = form.get() as ComplexForm
			val result = request.bind<ComplexForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model,resultView))
		}
	}

}