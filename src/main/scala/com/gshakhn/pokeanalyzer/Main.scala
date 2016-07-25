package com.gshakhn.pokeanalyzer

import ammonite.ops._

import scala.util.control.NonFatal

object Main extends App {
  val trainerLevel = args(0).toInt
  val inputDirectory = Path(args(1))
  ls ! inputDirectory foreach { inputFile =>
    try {
      val pokemon = new ScreenshotParser(inputFile, trainerLevel).parse
      val perfectionRange = new PokeIVCalculator(trainerLevel).perfectionRange(pokemon)
      val min = perfectionRange._1.formatted("%.2f")
      val max = perfectionRange._2.formatted("%.2f")
      println(s"${pokemon.name},${pokemon.cp},${pokemon.hp},${pokemon.dust},${pokemon.level}, $min - $max")
    } catch {
      case NonFatal(e) => println(s"Could not parse ${inputFile.last}")
    }
  }
}