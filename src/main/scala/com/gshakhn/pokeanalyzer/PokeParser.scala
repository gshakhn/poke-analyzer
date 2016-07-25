package com.gshakhn.pokeanalyzer

import ammonite.ops._
import ammonite.ops.ImplicitWd._
import com.sksamuel.scrimage.Image

import scala.collection.immutable.TreeMap

class PokeParser(inputFile: Path) {
  def parse: PokeInfo = PokeInfo(name, cp, hp, dust, level)

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

  lazy val level: Double = {
    degreesToLevel.filterKeys(_ < degree).last._2
  }

  private lazy val degree: Double = {
    val outputDir = tmp.dir()
    % convert(inputFile,
      "-crop", "520x260+60+145",
      outputDir / "powerbar.png")
    val image = Image.fromPath((outputDir/"powerbar.png").toNIO)
    val degreeAtEnd = 1.to(180).find { degree =>
      val (x, y) = cartesian(degree)
      val color = image.color(x, y)
      color.red < 225 && color.green < 225 && color.blue < 225
    }
    degreeAtEnd.get
  }

  private lazy val degreesToLevel: TreeMap[Double, Double] = {
    val data = 2.to(49).map { doubleLevel =>
      val realLevel = doubleLevel / 2.0
      val levelMultiplier = GameData.multipliers(realLevel)
      val maxMultiplier = GameData.multipliers(23)
      // These magic constants come from looking at the source of
      // https://jackhumbert.github.io/poke-rater and https://thesilphroad.com/research
      // No idea where they come from
      (levelMultiplier - 0.094) * 202.037116 / maxMultiplier -> realLevel
    }
    TreeMap(data.toArray: _*)
  }

  private def cartesian(degrees: Double): (Int, Int) = {
    val x = Math.cos((1 - (degrees / 180.0)) * Math.PI) * 260
    val y = Math.sin((1 - (degrees / 180.0)) * Math.PI) * 260
    val xMoved = x.toInt + 260
    val yFlipped = 260 - y.toInt
    (Math.min(xMoved, 519), Math.min(yFlipped, 259))
  }
}

case class PokeInfo(name: String, cp: Int, hp: Int, dust: Int, level: Double)
