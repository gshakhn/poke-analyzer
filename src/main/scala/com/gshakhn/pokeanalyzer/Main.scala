package com.gshakhn.pokeanalyzer

import ammonite.ops._
import ammonite.ops.ImplicitWd._

object Main extends App {
  val inputDirectory = cwd / "input"
  val pokemon = ls ! inputDirectory map  { inputFile =>
    new PokeParser(inputFile).parse
  }

  pokemon.foreach(poke =>
    write.append(cwd/"pokemon-info.txt", s"${poke.name},${poke.cp},${poke.hp},${poke.dust}\n")
  )
}

case class InputToOutput(input: Path, output: Path)