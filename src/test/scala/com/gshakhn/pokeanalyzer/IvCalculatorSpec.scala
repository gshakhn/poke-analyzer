package com.gshakhn.pokeanalyzer

import org.scalatest.{FunSpec, Matchers}

class IvCalculatorSpec extends FunSpec with Matchers {
  it("should calculate IVs for this Zubat") {
    val info = IndividualPokemon("ZUBAT", 83, 27, 600, 5)
    val potentialIVs: Seq[CalculatedIv] = new IvCalculator(23).potentialIVs(info)
    potentialIVs should contain(CalculatedIv(5.0, 15, 8, 14))
  }

  it("should limit IVs to the level of the IndividualPokemon") {
    val info = IndividualPokemon("DROWZEE", 665, 83, 3500, 24)
    val potentialIVs: Seq[CalculatedIv] = new IvCalculator(23).potentialIVs(info)
    potentialIVs.foreach(_.level shouldBe 24)
  }
}
