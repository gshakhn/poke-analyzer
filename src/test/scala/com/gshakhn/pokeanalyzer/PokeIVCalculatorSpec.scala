package com.gshakhn.pokeanalyzer

import org.scalatest.{FunSpec, Matchers}

class PokeIVCalculatorSpec extends FunSpec with Matchers {
  it("should calculate IVs for this Zubat") {
    val info = PokeInfo("ZUBAT", 83, 27, 600)
    val potentialIVs: Seq[PokeIV] = new PokeIVCalculator(23).calculate(info)
    potentialIVs should contain(PokeIV(5.0, 15, 8, 14))
  }
}
