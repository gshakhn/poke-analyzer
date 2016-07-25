package com.gshakhn.pokeanalyzer

import ammonite.ops.{Path, _}
import com.github.tototoshi.csv.{CSVReader, TSVFormat}

/**
  * Created by gshakhnazaryan on 7/24/16.
  */
object GameData {
  lazy val data = {
    val baseDataStream = getClass.getResourceAsStream("/GAME_MASTER_POKEMON_v0_2.tsv")
    val baseDataSource = scala.io.Source.fromInputStream( baseDataStream )
    val csvReader = CSVReader.open(baseDataSource)(new TSVFormat() {})
    val allPokemon: List[Map[String, String]] = csvReader.allWithHeaders()
    csvReader.close()
    allPokemon.map { row =>
      PokemonBaseStats(row("Identifier"), row("BaseAttack").toInt, row("BaseDefense").toInt, row("BaseStamina").toInt)
    }
  }

  lazy val cpMultiplierByLevel = Map(
    1 -> 0.0940000,
    1.5 -> 0.1351374,
    2 -> 0.1663979,
    2.5 -> 0.1926509,
    3 -> 0.2157325,
    3.5 -> 0.2365727,
    4 -> 0.2557201,
    4.5 -> 0.2735304,
    5 -> 0.2902499,
    5.5 -> 0.3060574,
    6 -> 0.3210876,
    6.5 -> 0.3354450,
    7 -> 0.3492127,
    7.5 -> 0.3624578,
    8 -> 0.3752356,
    8.5 -> 0.3875924,
    9 -> 0.3995673,
    9.5 -> 0.4111936,
    10 -> 0.4225000,
    10.5 -> 0.4335117,
    11 -> 0.4431076,
    11.5 -> 0.4530600,
    12 -> 0.4627984,
    12.5 -> 0.4723361,
    13 -> 0.4816850,
    13.5 -> 0.4908558,
    14 -> 0.4998584,
    14.5 -> 0.5087018,
    15 -> 0.5173940,
    15.5 -> 0.5259425,
    16 -> 0.5343543,
    16.5 -> 0.5426358,
    17 -> 0.5507927,
    17.5 -> 0.5588306,
    18 -> 0.5667545,
    18.5 -> 0.5745692,
    19 -> 0.5822789,
    19.5 -> 0.5898879,
    20 -> 0.5974000,
    20.5 -> 0.6048188,
    21 -> 0.6121573,
    21.5 -> 0.6194041,
    22 -> 0.6265671,
    22.5 -> 0.6336492,
    23 -> 0.6406530,
    23.5 -> 0.6475810,
    24 -> 0.6544356,
    24.5 -> 0.6612193,
    25 -> 0.6679340,
    25.5 -> 0.6745819,
    26 -> 0.6811649,
    26.5 -> 0.6876849,
    27 -> 0.6941437,
    27.5 -> 0.7005429,
    28 -> 0.7068842,
    28.5 -> 0.7131691,
    29 -> 0.7193991,
    29.5 -> 0.7255756,
    30 -> 0.7317000,
    30.5 -> 0.7377735,
    31 -> 0.7377695,
    31.5 -> 0.7407856,
    32 -> 0.7437894,
    32.5 -> 0.7467812,
    33 -> 0.7497610,
    33.5 -> 0.7527291,
    34 -> 0.7556855,
    34.5 -> 0.7586304,
    35 -> 0.7615638,
    35.5 -> 0.7644861,
    36 -> 0.7673972,
    36.5 -> 0.7702973,
    37 -> 0.7731865,
    37.5 -> 0.7760650,
    38 -> 0.7789328,
    38.5 -> 0.7817901,
    39 -> 0.7846370,
    39.5 -> 0.7874736,
    40 -> 0.7903000,
    40.5 -> 0.7931164
  )
}

case class PokemonBaseStats(name: String, baseAttack: Int, baseDefense: Int, baseStamina: Int)