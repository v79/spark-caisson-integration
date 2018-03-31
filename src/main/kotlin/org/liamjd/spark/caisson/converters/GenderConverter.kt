package org.liamjd.spark.caisson.converters

import org.liamjd.caisson.convertors.Converter
import org.liamjd.spark.caisson.controllers.Gender


class GenderConverter : Converter {
	override fun convert(from: String): Gender? {
		return Gender.valueOf(from)
	}
}