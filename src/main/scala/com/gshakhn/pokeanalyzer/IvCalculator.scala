package com.gshakhn.pokeanalyzer

class IvCalculator(trainerLevel: Int) {
  def perfectionRange(info: IndividualPokemon): (Double, Double) = {
    val ivs = potentialIVs(info)
    val perfection = ivs.map(_.perfection)
    (perfection.min, perfection.max)
  }

  def potentialIVs(info: IndividualPokemon): Seq[CalculatedIv] = {
    val dataForPokemon = GameData.data.find(_.name.equalsIgnoreCase(info.name)).get
    for {
      potentialAttack <- 1 to 15
      potentialDefense <- 1 to 15
      potentialStamina <- 1 to 15
      iv = CalculatedIv(info.level, potentialAttack, potentialDefense, potentialStamina)
      potentialHp = hp(iv, dataForPokemon) if potentialHp == info.hp
      potentialCp = cp(iv, dataForPokemon) if potentialCp == info.cp
    } yield iv
  }

  def hp(iv: CalculatedIv, base: PokemonBaseStats): Int = Math.floor((base.baseStamina + iv.stamina) * cpMultiplier(iv)).toInt

  def cp(iv: CalculatedIv, base: PokemonBaseStats): Int = Math.floor(
    (base.baseAttack + iv.attack) *
      Math.sqrt(base.baseStamina + iv.stamina) *
      Math.sqrt(base.baseDefense + iv.defense) *
      Math.pow(cpMultiplier(iv), 2) / 10
  ).toInt

  def cpMultiplier(iv: CalculatedIv): Double = GameData.cpMultiplierByLevel(iv.level)
}

case class CalculatedIv(level: Double, attack: Int, defense: Int, stamina: Int) {
  lazy val perfection: Double = (attack + defense + stamina) / 45.0
}