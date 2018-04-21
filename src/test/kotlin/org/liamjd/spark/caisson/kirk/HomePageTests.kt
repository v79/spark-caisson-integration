package org.liamjd.spark.caisson.kirk

import com.automation.remarks.kirk.Browser
import com.automation.remarks.kirk.conditions.visible
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.liamjd.spark.caisson.Server
import org.openqa.selenium.By
import kotlin.test.assertEquals


class HomePageTests : Spek( {
	val url = "http://localhost:4569/"
	val args: Array<String> = emptyArray()
	val browser = Browser()

	lateinit var server: Server
//	server = Server(args)


	beforeEachTest { server = Server(args) }
	afterEachTest { server.destroy() }

	describe("Launch a browser and navigate to the server home page") {
		it("Opens the browser to the correct address") {
			browser.open(url)
				assertEquals("Spark-Caisson Integration Tests",browser.title)
		}
	}

	describe("Launch browser and test each form element in turn") {
		val resultContainer = browser.element(".modelResult")
		on("Just a string") {
			it("Will construct a string 'Selenium'") {
				browser.open(url)
				val expected = "StringForm(just_a_string=Selenium)"
				val submit = browser.element("#just_a_stringSubmit")
				browser.element(By.name("just_a_string")).sendKeys("Selenium")
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
		on("Just a number") {
			it("Will construct an Integer 123456") {
				browser.open(url)
				val expected = "IntForm(just_an_int=123456)"
				val submit = browser.element("#just_an_intSubmit")
				browser.element(By.name("just_an_int")).sendKeys("123456")
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
		on("Person") {
			it("Will construct Person Billy Bob 74") {
				browser.open(url)
				val expected = "Person(name=Billy Bob, age=74)"
				val submit = browser.element("#personSubmit")
				browser.element(By.name("name")).sendKeys("Billy Bob")
				browser.element(By.name("age")).sendKeys("74")
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
		on("Agree checkbox") {
			it("Will set checkbox to false") {
				browser.open(url)
				val expected = "Checkbox(agree=false)"
				val submit = browser.element("#checkboxSubmit")
				browser.element(By.name("agree")).click()
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
		on("Gender radiobutton") {
			it("Will set gender to Female") {
				browser.open(url)
				val expected = "GenderForm(gender=female)"
				val submit = browser.element("#radioButtonSubmit")
				val genderRadioButton = browser.all(By.name("gender"))
				genderRadioButton.forEach {
					if(it.attr("value").equals("female")) {
						it.click()
					}
				}
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
		on("Check box group") {
			it("Will set colours to green and red") {
				browser.open(url)
				val expected = "CheckboxListForm(colour=[green, red])"
				val submit = browser.element("#checkboxGroupFormSubmit")
				val colourCheckboxes = browser.all(By.name("colour"))
				colourCheckboxes.forEach {
					when(it.attr("value")) {
						"blue" -> it.click()
						"red" -> it.click()
						"green" -> it.click()
					}
				}
				submit.click()
				resultContainer.waitUntil(visible,6000)
				assertEquals(expected,browser.element(".modelResult").text)
			}
		}
	}
})