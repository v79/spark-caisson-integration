package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.annotations.CController
import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.Form
import spark.ModelAndView
import spark.kotlin.get
import spark.kotlin.post


data class StringForm(val just_a_string: String)
data class IntForm(val just_an_int: Int)
data class Person(val name: String, val age: Int)

@CController
class HomeController : AbstractController("/") {
	init {
		get(path) {
			engine.render(ModelAndView(model,"home"))
		}

		post("stringForm") {

			val form: Form = Form(request.toMap, StringForm::class)
			val result: StringForm = form.get() as StringForm
			model.put("resultingString",result.toString())

			render()
		}

		post("intForm") {
			val form = Form(request.toMap, IntForm::class)
			val result = form.get() as IntForm
			model.put("resultingString", result.toString())
			render()
		}

		post("personForm") {
			request.toMap.forEach {
				println(it)
			}
			val form = Form(request.toMap, Person::class)
			val result = form.get() as Person
			model.put("resultingString", result.toString())
			render()

		}
	}

	fun render(): String {
		return engine.render(ModelAndView(model, "fragments/results"))
	}
}


