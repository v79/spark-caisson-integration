package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.Form
import org.liamjd.spark.caisson.models.ComplexForm
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post

class ComplexController : AbstractController("/complex") {
	init {
		get(path) {
			engine.render(ModelAndView(model, "complex"))
		}

		post("complexForm") {
			println("---- Complex form ----")

			val form = Form(request.queryMap().toMap(),ComplexForm::class)
			val result = form.get() as ComplexForm
			model.put("resultingString",result.toString())

			 engine.render(ModelAndView(model, "fragments/results"))
		}
	}

}