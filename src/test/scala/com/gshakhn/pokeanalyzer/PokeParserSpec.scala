package com.gshakhn.pokeanalyzer

import ammonite.ops._
import org.scalatest.{FunSpec, Matchers}

class PokeParserSpec extends FunSpec with Matchers {
  val inputFiles = ls! cwd/"src"/"test"/"resources"
  inputFiles.foreach { inputFile =>
    it(s"should parse ${inputFile.last}") {
      val expectedPokemon = parseExpectedData(inputFile)
      val parser = new PokeParser(inputFile)
      parser.parse shouldBe expectedPokemon
    }
  }

  def parseExpectedData(inputFile: Path): PokeInfo = {
    val data = inputFile.last.replace(".png", "").split("-")
    val expectedName = data(0)
    val expectedCp = data(1)
    val expectedHp = data(2)
    val expectedDust = data(3)
    PokeInfo(expectedName, expectedCp.toInt, expectedHp.toInt, expectedDust.toInt, 0)
  }
}
