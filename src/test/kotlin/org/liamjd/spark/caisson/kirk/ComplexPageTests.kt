package org.liamjd.spark.caisson.kirk

import com.automation.remarks.kirk.Browser
import com.automation.remarks.kirk.conditions.visible
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.liamjd.spark.caisson.Server
import org.liamjd.spark.caisson.models.ComplexForm
import org.liamjd.spark.caisson.models.DefaultForm
import org.openqa.selenium.By
import kotlin.test.assertEquals

class ComplexPageTests : Spek({
	val url = "http://localhost:4569/complex"
	val args: Array<String> = emptyArray()
	val browser = Browser()

	lateinit var server: Server
	server = Server(args)


//	beforeEachTest { server = Server(args) }
//	afterEachTest { server.destroy() }

	describe("Navigate to the complex page") {
		browser.open(url)
		val resultContainer = browser.element(".modelResult")

		on("Filling in the complex object form") {
			val colourList = listOf("green","red")
			val expectedForm = ComplexForm("Grapefruit",127,
					"secret password",complexHidden = "hidden value", complexColour = colourList)
			it("will return the expected string from the complex object") {

				val submit = browser.element("#complextFormSubmit")
				browser.element(By.name("complexText")).sendKeys("Grapefruit")
				browser.element(By.name("complexNumber")).sendKeys("127")
				browser.element(By.name("complexPassword")).sendKeys("secret password")
				val colourCheckboxes = browser.all(By.name("complexColour"))
				colourCheckboxes.forEach {
					when(it.attr("value")) {
						"blue" -> it.click()
						"red" -> it.click()
						"green" -> it.click()
					}
				}

				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expectedForm.toString(),browser.element(".modelResult").text)
			}
		}

		on("Not filling in the default form and still getting values") {
			val expected = DefaultForm()
			it("it will return the the default values for the form, Bob and 25") {
				val submit = browser.element("#defaultFormSubmit")
				browser.element(By.name("defaultText")).clear()
				browser.element(By.name("defaultNumber")).clear()

				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected.toString(),browser.element(".modelResult").text)
			}
		}
	}


})