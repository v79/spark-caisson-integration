package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.annotations.CController
import org.liamjd.caisson.annotations.CConverter
import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.WebForm
import org.liamjd.spark.caisson.converters.CheckboxConverter
import org.liamjd.spark.caisson.converters.GenderConverter
import spark.ModelAndView
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
data class CheckboxListForm(val colour: List<String>)

@CController
class HomeController : AbstractController("/") {

	val resultView = "fragments/results"

	init {
		get(path) {
			engine.render(ModelAndView(model,"home"))
		}

		post("stringForm") {
			val result = WebForm(request,StringForm::class).get() as StringForm
			model.put("resultingString",result.toString())

			render(resultView)
		}

		post("intForm") {
			val form = WebForm(request, IntForm::class)
			val result = form.get() as IntForm
			model.put("resultingString", result.toString())
			render(resultView)
		}

		post("personForm") {
			debugQueryMap(request)
			val form = WebForm(request, Person::class)
			val result = form.get() as Person
			model.put("resultingString", result.toString())
			render(resultView)
		}

		post("checkboxForm") {
			// request is empty when checkbox is unset
			val form = WebForm(request, Checkbox::class)
			val result = form.get() as Checkbox
			model.put("resultingString", result.toString())
			render(resultView)
		}

		post("radioButtonForm") {
			val form = WebForm(request, GenderForm::class)
			val result = form.get() as GenderForm
			model.put("resultingString", result.toString())
			render(resultView)
		}

		post("checkboxGroupForm") {
			// 0,1 or more items. Possible repeats? Must be a list
			val form = WebForm(request,CheckboxListForm::class)
			val result = form.get() as CheckboxListForm
			model.put("resultingString",result.toString())
			render(resultView)
		}

	}

	private fun render(viewName: String): String {
		return engine.render(ModelAndView(model,viewName))
	}
}



