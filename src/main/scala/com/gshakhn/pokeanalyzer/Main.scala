package com.gshakhn.pokeanalyzer

import ammonite.ops._
import ammonite.ops.ImplicitWd._

object Main extends App {
  val inputDirectory = cwd / "input"
  val inputToOutput: Seq[InputToOutput] = ls ! inputDirectory map { inputFile =>
    val pngName = inputFile.last
    val outputName = pngName.replace(".png", "")
    val outputPath: Path = cwd / "output" / outputName
    InputToOutput(inputFile, outputPath)
  }
  val pokemon = inputToOutput.map {
    case InputToOutput(input, output) =>
      mkdir ! output
      % convert(input,
        "-crop", "175x50+200+70",
        "-fuzz", "10%", "-fill", "black", "-opaque", "#1F6894",
        output / "cp.png")
      % convert(input,
        "-crop", "150x25+250+600",
        output / "hp.png")
      % convert(input,
        "-crop", "75x30+350+900",
        output / "powerupCost.png")
      % convert(input,
        "-crop", "400x75+125+500",
        output / "name.png")

      val cpResult = %% tesseract(output / "cp.png", "stdout")
      val hpResult = %% tesseract(output / "hp.png", "stdout")
      val powerupCostResult = %% tesseract(output / "powerupCost.png", "stdout")
      val nameResult = %% tesseract(output / "name.png", "stdout")

      val cp = cpResult.out.string.trim.replace("CP", "").toInt
      val hp = hpResult.out.string.trim.replace("HP", "").split("/")(1).toInt
      val powerupCost = powerupCostResult.out.string.trim.toInt
      val name = nameResult.out.string.trim
      Unit
      PokemonStats(
        cp,
        hp,
        powerupCost,
        name
      )
  }

  pokemon.foreach(poke =>
    write.append(cwd/"pokemon-info.txt", s"${poke.name},${poke.cp},${poke.hp},${poke.powerupCost}\n")
  )
}

case class InputToOutput(input: Path, output: Path)

case class PokemonStats(cp: Int, hp: Int, powerupCost: Int, name: String)