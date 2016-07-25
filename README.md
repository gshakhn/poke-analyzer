This project analyzes screenshots from Pokemon Go to parse out the various stats on a Pokemon. It can then also calculate an IV range.

# Prereqs

* Java, tesseract-ocr, and ImageMagick have to be installed. You'll also need sbt if you want to build it yourself.
* iPhone 6s. The code is based on the screenshots from this device. It's probably adaptable to other devices/resolutions, but I need sample screenshots to figure that out.

# Steps

1. Download the latest jar from https://gitlab.com/gshakhn/poke-analyzer/pipelines or build it yourself via `sbt assembly`
2. View a Pokemon in Pokemon Go on your phone. Make sure you're fully scrolled up. Also, the Pokemon name must be the original name.
3. Take a screenshot.
4. Repeat 2-3 for all the Pokemon you want to analyze.
5. Put screenshot on a folder somewhere on your computer. Make sure the folder only has the screenshot(s)
6. Run `java -jar JAR_FILE YOUR_TRAINER_LEVEL FULL_PATH_TO_SCREENSHOT_FOLDER`

Output will be:
Pokemon Name, CP, HP, Dust Upgrade Cost, Level, Min Potential IV % - Max Potential IV %

# Limitations

Figuring out the level from the Pokemon is semi-hacky based on some magic constants I found on the internet. It might be wrong. In general, analyzing the screenshot is very error prone. There are some Pokemon (Geodude, Gengar) whose animations can cover the CP bar or even the CP text. In order to get more accurate results, you can sniff the wire and get the actual IV numbers that are passed on the server. But that may potentially break Niantic's ToS. Plus learning about OCR and image analysis was a fun Sunday project.

# Future Ideas

* Support more devices
* Server app that can accept screenshots
* Automatically pick up screenshots from Dropbox uploads for IV analysis on the go

# Credit

I didn't come up with the IV equations myself. Reference material, non-exhaustive, and in no particular order:
* https://www.reddit.com/r/TheSilphRoad/comments/4tzcmk/faq_on_ivs_info_megathread/
* https://www.reddit.com/r/pokemongodev/comments/4t7xb4/exact_cp_formula_from_stats_and_cpm_and_an_update/
* https://gist.github.com/anonymous/540700108cf0f051e11f70273e9e2590
* https://gist.github.com/noxwyll/8c821408b1d943c3e500d91d77266368/f264e42f2e83bbe845841717f74b276c9801ba7d
* https://www.reddit.com/r/TheSilphRoad/comments/4t7r4d/exact_pokemon_cp_formula/
* https://thesilphroad.com/research
* https://jackhumbert.github.io/poke-rater
* https://docs.google.com/spreadsheets/d/1wbtIc33K45iU1ScUnkB0PlslJ-eLaJlSZY47sPME2Uk/edit