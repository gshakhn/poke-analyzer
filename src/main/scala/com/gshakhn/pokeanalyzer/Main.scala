package com.gshakhn.pokeanalyzer

import ammonite.ops.ImplicitWd._
import ammonite.ops._

import scala.util.control.NonFatal

object Main extends App {
  val inputDirectory = cwd / "input"
  ls ! inputDirectory foreach { inputFile =>
    try {
      val pokemon = new PokeParser(inputFile).parse
      write.append(cwd/"pokemon-info.txt", s"${pokemon.name},${pokemon.cp},${pokemon.hp},${pokemon.dust}\n")
    } catch {
      case NonFatal(e) => println(s"Could not parse ${inputFile.last}")
    }
  }
}