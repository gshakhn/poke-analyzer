package com.gshakhn.pokeanalyzer

import ammonite.ops.ImplicitWd._
import ammonite.ops._
import com.sksamuel.scrimage.Image

import scala.collection.immutable.TreeMap

class ScreenshotParser(inputFile: Path, trainerLevel: Int) {
  def parse: IndividualPokemon = IndividualPokemon(name, cp, hp, dust, level)

  private val intRegex = """(\d+)""".r

  private lazy val name: String = {
    val outputDir = tmp.dir()
    % convert(inputFile,
      "-crop", "400x75+125+500",
      outputDir / "name.png")
    val nameResult = %% tesseract("-psm", "7", outputDir / "name.png", "stdout")
    nameResult.out.string.trim.toUpperCase
  }

  private lazy val cp: Int = {
    val outputDir = tmp.dir()
    % convert(inputFile,
      "-crop", "175x50+200+70",
      "-fuzz", "15%", "-fill", "black", "+opaque", "white",
      "-negate",
      outputDir / "cp.png")
    val cpResult = %% tesseract("-psm", "7", outputDir / "cp.png", "stdout", "-c", "tessedit_char_whitelist=CP0123456789")
    intRegex.findFirstIn(cpResult.out.string.trim).get.toInt
  }

  private lazy val hp: Int = {
    val outputDir = tmp.dir()
    % convert(inputFile,
      "-crop", "150x25+250+600",
      outputDir / "hp.png")
    val hpResult = %% tesseract("-psm", "7", outputDir / "hp.png", "stdout")
    intRegex.findFirstIn(hpResult.out.string.trim).get.toInt
  }

  private lazy val dust: Int = {
    val outputDir = tmp.dir()
    % convert(inputFile,
      "-crop", "75x30+350+900",
      outputDir / "dust.png")
    val dustResult = %% tesseract("-psm", "7", outputDir / "dust.png", "stdout")
    dustResult.out.string.trim.toInt
  }

  private lazy val level: Double = {
    degreesToLevel.filterKeys(_ < degreeOnCpBar).last._2
  }

  private lazy val degreeOnCpBar: Double = {
    val outputDir = tmp.dir()
    val cpBarFile = outputDir / "powerbar.png"
    % convert(inputFile,
      "-crop", s"${imageHeight*2}x$imageHeight+60+145",
      cpBarFile)
    val image = Image.fromPath(cpBarFile.toNIO)
    1.to(180).filter { degree =>
      val (x, y) = cartesian(degree)
      val color = image.color(x, y)
      val whiteThreshold = 225
      color.red >= whiteThreshold && color.green >= whiteThreshold && color.blue >= whiteThreshold
    }.last + 1
  }

  private lazy val degreesToLevel: TreeMap[Double, Double] = {
    val maxPokemonLevel: Double = trainerLevel + 1.5
    val potentialPokemonLevels = 2.to((maxPokemonLevel * 2).toInt).map(_ / 2.0)
    val data = potentialPokemonLevels.map { potentialLevel =>
      val levelMultiplier = GameData.cpMultiplierByLevel(potentialLevel)
      val maxMultiplier = GameData.cpMultiplierByLevel(trainerLevel)
      // This magic constant come from looking at the source of
      // https://jackhumbert.github.io/poke-rater and https://thesilphroad.com/research
      // No idea where it came from, but it seems to work
      (levelMultiplier - GameData.cpMultiplierByLevel(1)) * 202.037116 / maxMultiplier -> potentialLevel
    }
    TreeMap(data.toArray: _*)
  }

  private val imageHeight: Int = 260

  private def cartesian(degrees: Double): (Int, Int) = {
    val x = Math.cos((1 - (degrees / 180.0)) * Math.PI) * imageHeight
    val y = Math.sin((1 - (degrees / 180.0)) * Math.PI) * imageHeight
    val xMoved = x.toInt + imageHeight
    val yFlipped = imageHeight - y.toInt
    (Math.min(xMoved, 2 * imageHeight - 1), Math.min(yFlipped, imageHeight - 1))
  }
}

case class IndividualPokemon(name: String, cp: Int, hp: Int, dust: Int, level: Double)
