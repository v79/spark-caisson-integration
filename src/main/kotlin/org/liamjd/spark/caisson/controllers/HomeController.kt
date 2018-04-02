package org.liamjd.spark.caisson.controllers

import org.liamjd.caisson.annotations.CController
import org.liamjd.caisson.annotations.CConverter
import org.liamjd.caisson.controllers.AbstractController
import org.liamjd.caisson.webforms.Form
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
	init {
		get(path) {
			engine.render(ModelAndView(model,"home"))
		}

		post("stringForm") {
			val result = Form(request.queryMap().toMap(),StringForm::class).get() as StringForm
			model.put("resultingString",result.toString())

			render()
		}

		post("intForm") {
			val form = Form(request.queryMap().toMap(), IntForm::class)
			val result = form.get() as IntForm
			model.put("resultingString", result.toString())
			render()
		}

		post("personForm") {
			val form = Form(request.queryMap().toMap(), Person::class)
			val result = form.get() as Person
			model.put("resultingString", result.toString())
			render()
		}

		post("checkboxForm") {
			// request is empty when checkbox is unset
			val form = Form(request.queryMap().toMap(), Checkbox::class)
			val result = form.get() as Checkbox
			model.put("resultingString", result.toString())
			render()
		}

		post("radioButtonForm") {
			debugRequestMap(request)
			val form = Form(request.queryMap().toMap(), GenderForm::class)
			val result = form.get() as GenderForm
			model.put("resultingString", result.toString())
			render()
		}

		post("checkboxGroupForm") {
			// 0,1 or more items. Possible repeats? Must be a list
			println("------------checkboxGroupForm")
			val form = Form(request.queryMap().toMap(),CheckboxListForm::class)
			val result = form.get() as CheckboxListForm
			model.put("resultingString",result.toString())
			debugQueryMap(request)
			render()
		}

	}

	fun render(): String {
		return engine.render(ModelAndView(model, "fragments/results"))
	}

}


