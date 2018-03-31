package org.liamjd.spark.caisson.converters

import org.liamjd.caisson.convertors.Converter

class CheckboxConverter : Converter {
	override fun convert(from: String): Boolean? {
		if(from.equals("on",true)) {
			return true
		}
		return false
	}
}