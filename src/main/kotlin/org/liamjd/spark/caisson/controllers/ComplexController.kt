package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.extensions.bind
import org.liamjd.spark.caisson.models.ComplexForm
import org.liamjd.spark.caisson.models.DefaultForm
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
			val result = request.bind<ComplexForm>()
			model.put("resultingString",result.toString())

			engine.render(ModelAndView(model,resultView))
		}

		post("defaultsForm") {
			val result = request.bind<DefaultForm>()
			model.put("resultingString",result.toString())
			engine.render(ModelAndView(model,resultView))
		}
	}

}