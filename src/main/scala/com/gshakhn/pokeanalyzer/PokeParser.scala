package com.gshakhn.pokeanalyzer

import ammonite.ops._
import ammonite.ops.ImplicitWd._

class PokeParser(inputFile: Path) {
  def parse: PokeInfo = PokeInfo(name, cp, hp, dust)

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
}

case class PokeInfo(name: String, cp: Int, hp: Int, dust: Int)
