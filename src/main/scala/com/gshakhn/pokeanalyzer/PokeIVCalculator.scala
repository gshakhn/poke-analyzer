package com.gshakhn.pokeanalyzer

import ammonite.ops.{Path, _}
import com.github.tototoshi.csv.{CSVReader, TSVFormat}

class PokeIVCalculator(trainerLevel: Int) {
  def calculate(info: PokeInfo): Seq[PokeIV] = {
    val zubat = GameData.data.find(_.name == "Zubat").get
    println(zubat)
    for {
      doublePokeLevel <- 2 to (trainerLevel * 2)
      pokeLevel = doublePokeLevel / 2.0
      potentialAttack <- 1 to 15
      potentialDefense <- 1 to 15
      potentialStamina <- 1 to 15
      iv = PokeIV(pokeLevel, potentialAttack, potentialDefense, potentialStamina)
      potentialHp = hp(iv, zubat) if potentialHp == info.hp
      potentialCp = cp(iv, zubat) if potentialCp == info.cp
    } yield iv
  }

  def hp(iv: PokeIV, base: PokeBaseData): Int = Math.floor((base.baseStamina + iv.stamina) * cpMultiplier(iv)).toInt

  def cp(iv: PokeIV, base: PokeBaseData): Int = Math.floor(
    (base.baseAttack + iv.attack) *
      Math.sqrt(base.baseStamina + iv.stamina) *
      Math.sqrt(base.baseDefense + iv.defense) *
      Math.pow(cpMultiplier(iv), 2) / 10
  ).toInt

  def cpMultiplier(iv: PokeIV): Double = GameData.multipliers(iv.level)
}


case class PokeBaseData(name: String, baseAttack: Int, baseDefense: Int, baseStamina: Int)

case class PokeIV(level: Double, attack: Int, defense: Int, stamina: Int) {
  lazy val perfection: Double = (attack + defense + stamina) / 45.0
}