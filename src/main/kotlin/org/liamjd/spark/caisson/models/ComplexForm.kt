package org.liamjd.spark.caisson.models

class ComplexForm(val complexText: String, val complexNumber: Int, val complexPassword: String, val complexHidden: String, val complexColour: List<String>) {

	override fun toString(): String {
		val stringBuilder = StringBuilder()
		stringBuilder.append("Text: $complexText, Number: $complexNumber, Password: $complexPassword, Hidden: $complexHidden, ")
		stringBuilder.append("Colours: [")
		complexColour.forEach { stringBuilder.append("$it").append(", ") }
		stringBuilder.append("]")

		return stringBuilder.toString()
	}
}