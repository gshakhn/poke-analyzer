package com.gshakhn.pokeanalyzer

import ammonite.ops.ImplicitWd._
import ammonite.ops._

import scala.util.control.NonFatal

object Main extends App {
  val inputDirectory = cwd / "input"
  ls ! inputDirectory foreach { inputFile =>
    try {
      val pokemon = new PokeParser(inputFile).parse
      val perfectionRange = new PokeIVCalculator(23).perfectionRange(pokemon)
      write.append(cwd/"pokemon-info.txt", s"${pokemon.name},${pokemon.cp},${pokemon.hp},${pokemon.dust},${perfectionRange._1} - ${perfectionRange._2}\n")
    } catch {
      case NonFatal(e) => println(s"Could not parse ${inputFile.last}")
    }
  }
}