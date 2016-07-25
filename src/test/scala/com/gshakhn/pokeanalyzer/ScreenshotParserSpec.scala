package com.gshakhn.pokeanalyzer

import ammonite.ops._
import org.scalatest.{FunSpec, Matchers}

class ScreenshotParserSpec extends FunSpec with Matchers {
  val inputFiles = ls! cwd/"src"/"test"/"resources"
  inputFiles.foreach { inputFile =>
    it(s"should parse ${inputFile.last}") {
      val expectedPokemon = parseExpectedData(inputFile)
      val parser = new ScreenshotParser(inputFile, 23)
      parser.parse shouldBe expectedPokemon
    }
  }

  def parseExpectedData(inputFile: Path): IndividualPokemon = {
    val data = inputFile.last.replace(".png", "").split("-")
    val expectedName = data(0)
    val expectedCp = data(1)
    val expectedHp = data(2)
    val expectedDust = data(3)
    val expectedLevel = data(4)
    IndividualPokemon(expectedName, expectedCp.toInt, expectedHp.toInt, expectedDust.toInt, expectedLevel.toDouble)
  }
}
