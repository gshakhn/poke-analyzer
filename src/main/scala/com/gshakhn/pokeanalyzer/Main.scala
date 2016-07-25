package com.gshakhn.pokeanalyzer

import ammonite.ops.ImplicitWd._
import ammonite.ops._

import scala.util.control.NonFatal

object Main extends App {
  val inputDirectory = cwd / "input"
  ls ! inputDirectory foreach { inputFile =>
    try {
      val pokemon = new PokeParser(inputFile, 23).parse
      val perfectionRange = new PokeIVCalculator(23).perfectionRange(pokemon)
      val min = perfectionRange._1.formatted("%.2f")
      val max = perfectionRange._2.formatted("%.2f")
      write.append(cwd/"pokemon-info.txt", s"${pokemon.name},${pokemon.cp},${pokemon.hp},${pokemon.dust},${pokemon.level}, $min - $max\n")
    } catch {
      case NonFatal(e) => println(s"Could not parse ${inputFile.last}")
    }
  }
}