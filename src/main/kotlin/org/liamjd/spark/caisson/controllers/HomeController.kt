package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.annotations.CController
import org.liamjd.caisson.annotations.CConverter
import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.Form
import org.liamjd.spark.caisson.converters.CheckboxConverter
import org.liamjd.spark.caisson.converters.GenderConverter
import spark.ModelAndView
import spark.Request
import spark.kotlin.get
import spark.kotlin.post


data class StringForm(val just_a_string: String)
data class IntForm(val just_an_int: Int)
data class Person(val name: String, val age: Int)
data class Checkbox(@CConverter(converterClass = CheckboxConverter::class) val agree: Boolean)
enum class Gender(val gender: String) {
	male("male"),
	female("female"),
	other("other")
}
data class GenderForm(@CConverter(converterClass = GenderConverter::class) val gender: Gender)

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
			val form = Form(request.toMap, Person::class)
			val result = form.get() as Person
			model.put("resultingString", result.toString())
			render()
		}

		post("checkboxForm") {
			// request is empty when checkbox is unset
			val form = Form(request.toMap, Checkbox::class)
			val result = form.get() as Checkbox
			model.put("resultingString", result.toString())
			render()
		}

		post("radioButtonForm") {
			debugRequestMap(request)
			val form = Form(request.toMap, GenderForm::class)
			val result = form.get() as GenderForm
			model.put("resultingString", result.toString())
			render()
		}

		post("checkboxGroupForm") {
			debugParams(request)
			debugRequestMap(request)

			render()
		}
	}

	fun render(): String {
		return engine.render(ModelAndView(model, "fragments/results"))
	}


	private fun debugRequestMap(request: Request) {
		request.toMap.forEach {
			println("\t$it")
		}
	}
}


