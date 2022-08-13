package github.nwn.jubilife.tools.psra.tests

import github.nwn.jubilife.tools.psra.FileReplayStream
import github.nwn.jubilife.tools.psra.protocol.GameState
import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.MutableReplay
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import org.junit.jupiter.api.Test
import java.io.File

class BattleInitializationTests {

    companion object {
        const val SAMPLE = """
|inactive|Battle timer is ON: inactive players will automatically lose when time's up.
|J|Reymedy
|J|Leftiez~
|player|p1|Reymedy|#reymedy
|player|p2|Leftiez~|#leftiez
|gametype|singles
|gen|5
|tier|[Gen 5] OU
|rule|Evasion Abilities Clause: Evasion abilities are banned
|clearpoke
|poke|p1|Politoed, M
|poke|p1|Jirachi
|poke|p1|Breloom, F
|poke|p1|Garchomp, M
|poke|p1|Zapdos
|poke|p1|Tentacruel, M
|poke|p2|Stoutland, F
|poke|p2|Hippowdon, F
|poke|p2|Rotom-Wash
|poke|p2|Ferrothorn, F
|poke|p2|Latios, M
|poke|p2|Metagross
|rule|Sleep Clause Mod: Limit one foe put to sleep
|rule|Species Clause: Limit one of each Pokémon
|rule|OHKO Clause: OHKO moves are banned
|rule|Moody Clause: Moody is banned
|rule|Evasion Moves Clause: Evasion moves are banned
|rule|Endless Battle Clause: Forcing endless battles is banned
|rule|HP Percentage Mod: HP is shown in percentages
|teampreview
|J|McMeghan
|J|iconic
|J|MegaStarUniverse
|J|zdrup
|J|H-C
|J|Zenadark
|J|Twin Citiez
|J|Lusso.
|J|Lord Mur
|J|Vinc2612
|J|fant
|c|&iconic|down to dis . . .
|J|Gei
|J|Espaa
|J|Yogiras
|c|&iconic|vive la france . . .
|J|littlelucario
|c|★Reymedy|...
|J|TewMew
|c|★Leftiez~|:p
|L|Lord Mur
|c|+littlelucario|is thiis for a tour?
|J|boudouche
|c|&iconic|reymedy that looks like my frontier team from 2 yrs ago
|J|Jimmy Turtwig
|c|&iconic|its v rain stall weak :(
|J|SoulWind
|J|BahaWin
|c|★Reymedy|:P
|c|&iconic|screw phil and his rain stal
|
|start
|switch|p1a: Gengen|Breloom, F|307\/307
|switch|p2a: Rotom-Wash|Rotom-Wash|271\/271
|turn|1
|J|WcJay
|
|move|p2a: Rotom-Wash|Hidden Power|p1a: Gengen
|-supereffective|p1a: Gengen
|-damage|p1a: Gengen|69\/307
|move|p1a: Gengen|Spore|p2a: Rotom-Wash
|-status|p2a: Rotom-Wash|slp
|-enditem|p2a: Rotom-Wash|Chesto Berry|[eat]
|-curestatus|p2a: Rotom-Wash|slp
|
|-status|p1a: Gengen|tox|[from] item: Toxic Orb
|turn|2
|c|+littlelucario|not
|c|+littlelucario|bad
|J|LJDarkrai
|L|MegaStarUniverse
|J|Korby
|J|Tom0410
|J|davidTheMaster
|J|Vinc608
|J|Bowlii
|
|switch|p1a: Nanami|Jirachi|403\/403
|move|p2a: Rotom-Wash|Hydro Pump|p1a: Nanami
|-damage|p1a: Nanami|253\/403
|
|-heal|p1a: Nanami|278\/403|[from] item: Leftovers
|turn|3
|J|pholovvers
|
|move|p2a: Rotom-Wash|Volt Switch|p1a: Nanami
|-damage|p1a: Nanami|193\/403
|
|switch|p2a: Hippowdon|Hippowdon, F|420\/420
|-weather|Sandstorm|[from] ability: Sand Stream|[of] p2a: Hippowdon
|move|p1a: Nanami|Wish|p1a: Nanami
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|218\/403|[from] item: Leftovers
|turn|4
|J|xastify
|L|pholovvers
|J|Marshall.Law
|J|Stratos
|
|switch|p1a: Rikimaru|Garchomp, M|420\/420
|move|p2a: Hippowdon|Earthquake|p1a: Rikimaru
|-damage|p1a: Rikimaru|294\/420
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Rikimaru|420\/420|[from] move: Wish|[wisher] Nanami
|turn|5
|
|move|p1a: Rikimaru|Stealth Rock|p2a: Hippowdon
|-sidestart|p2: Leftiez~|move: Stealth Rock
|move|p2a: Hippowdon|Ice Fang|p1a: Rikimaru
|-supereffective|p1a: Rikimaru
|-damage|p1a: Rikimaru|236\/420
|-damage|p2a: Hippowdon|368\/420|[from] ability: Rough Skin|[of] p1a: Rikimaru
|-damage|p2a: Hippowdon|298\/420|[from] item: Rocky Helmet|[of] p1a: Rikimaru
|
|-weather|Sandstorm|[upkeep]
|-heal|p2a: Hippowdon|324\/420|[from] item: Leftovers
|turn|6
|
|move|p1a: Rikimaru|Toxic|p2a: Hippowdon
|-status|p2a: Hippowdon|tox
|move|p2a: Hippowdon|Stealth Rock|p1a: Rikimaru
|-sidestart|p1: Reymedy|move: Stealth Rock
|
|-weather|Sandstorm|[upkeep]
|-heal|p2a: Hippowdon|350\/420 tox|[from] item: Leftovers
|-damage|p2a: Hippowdon|324\/420 tox|[from] psn
|turn|7
|L|Zenadark
|J|Fairy Peak
|J|Zenadark
|
|switch|p1a: Bonaparte|Politoed, M|323\/323
|-damage|p1a: Bonaparte|283\/323|[from] Stealth Rock
|-weather|RainDance|[from] ability: Drizzle|[of] p1a: Bonaparte
|move|p2a: Hippowdon|Earthquake|p1a: Bonaparte
|-damage|p1a: Bonaparte|108\/323
|
|-weather|RainDance|[upkeep]
|-heal|p2a: Hippowdon|350\/420 tox|[from] item: Leftovers
|-damage|p2a: Hippowdon|298\/420 tox|[from] psn
|turn|8
|
|switch|p2a: Rotom-Wash|Rotom-Wash|271\/271
|-damage|p2a: Rotom-Wash|238\/271|[from] Stealth Rock
|move|p1a: Bonaparte|Scald|p2a: Rotom-Wash
|-resisted|p2a: Rotom-Wash
|-damage|p2a: Rotom-Wash|159\/271
|-status|p2a: Rotom-Wash|brn
|
|-weather|RainDance|[upkeep]
|-damage|p2a: Rotom-Wash|126\/271 brn|[from] brn
|turn|9
|
|switch|p1a: Riou|Zapdos|383\/383
|-damage|p1a: Riou|288\/383|[from] Stealth Rock
|-ability|p1a: Riou|Pressure
|move|p2a: Rotom-Wash|Hydro Pump|p1a: Riou
|-damage|p1a: Riou|35\/383
|
|-weather|RainDance|[upkeep]
|-heal|p1a: Riou|58\/383|[from] item: Leftovers
|-damage|p2a: Rotom-Wash|93\/271 brn|[from] brn
|turn|10
|
|move|p1a: Riou|Thunderbolt|p2a: Rotom-Wash
|-damage|p2a: Rotom-Wash|0 fnt
|faint|p2a: Rotom-Wash
|
|-weather|RainDance|[upkeep]
|-heal|p1a: Riou|81\/383|[from] item: Leftovers
|
|switch|p2a: Latios|Latios, M|304\/304
|-damage|p2a: Latios|266\/304|[from] Stealth Rock
|turn|11
|
|switch|p1a: Zamza|Tentacruel, M|363\/363
|-damage|p1a: Zamza|318\/363|[from] Stealth Rock
|move|p2a: Latios|Draco Meteor|p1a: Zamza|[miss]
|-miss|p2a: Latios|p1a: Zamza
|
|-weather|RainDance|[upkeep]
|-heal|p1a: Zamza|340\/363|[from] ability: Rain Dish
|-heal|p1a: Zamza|362\/363|[from] item: Black Sludge
|turn|12
|J|Steelphoenix
|L|xastify
|
|move|p2a: Latios|Draco Meteor|p1a: Zamza
|-damage|p1a: Zamza|35\/363
|-unboost|p2a: Latios|spa|2
|move|p1a: Zamza|Rapid Spin|p2a: Latios
|-damage|p2a: Latios|253\/304
|-sideend|p1: Reymedy|Stealth Rock|[from] move: Rapid Spin|[of] p1a: Zamza
|
|-weather|RainDance|[upkeep]
|-heal|p1a: Zamza|57\/363|[from] ability: Rain Dish
|-heal|p1a: Zamza|79\/363|[from] item: Black Sludge
|turn|13
|
|switch|p2a: Hippowdon|Hippowdon, F|298\/420 tox
|-damage|p2a: Hippowdon|272\/420 tox|[from] Stealth Rock
|-weather|Sandstorm|[from] ability: Sand Stream|[of] p2a: Hippowdon
|move|p1a: Zamza|Scald|p2a: Hippowdon
|-supereffective|p2a: Hippowdon
|-damage|p2a: Hippowdon|60\/420 tox
|
|-weather|Sandstorm|[upkeep]
|-damage|p1a: Zamza|57\/363|[from] sandstorm
|-heal|p1a: Zamza|79\/363|[from] item: Black Sludge
|-heal|p2a: Hippowdon|86\/420 tox|[from] item: Leftovers
|-damage|p2a: Hippowdon|60\/420 tox|[from] psn
|turn|14
|
|switch|p2a: Ferrothorn|Ferrothorn, F|352\/352
|-damage|p2a: Ferrothorn|330\/352|[from] Stealth Rock
|move|p1a: Zamza|Scald|p2a: Ferrothorn
|-resisted|p2a: Ferrothorn
|-damage|p2a: Ferrothorn|304\/352
|-status|p2a: Ferrothorn|brn
|
|-weather|Sandstorm|[upkeep]
|-damage|p1a: Zamza|57\/363|[from] sandstorm
|-heal|p1a: Zamza|79\/363|[from] item: Black Sludge
|-heal|p2a: Ferrothorn|326\/352 brn|[from] item: Leftovers
|-damage|p2a: Ferrothorn|282\/352 brn|[from] brn
|turn|15
|
|switch|p1a: Bonaparte|Politoed, M|108\/323
|-weather|RainDance|[from] ability: Drizzle|[of] p1a: Bonaparte
|move|p2a: Ferrothorn|Explosion|p1a: Bonaparte
|-damage|p1a: Bonaparte|0 fnt
|faint|p2a: Ferrothorn
|faint|p1a: Bonaparte
|
|-weather|RainDance|[upkeep]
|
|switch|p1a: Rikimaru|Garchomp, M|236\/420
|switch|p2a: Hippowdon|Hippowdon, F|60\/420 tox
|-damage|p2a: Hippowdon|34\/420 tox|[from] Stealth Rock
|-weather|Sandstorm|[from] ability: Sand Stream|[of] p2a: Hippowdon
|turn|16
|
|switch|p2a: Latios|Latios, M|253\/304
|-damage|p2a: Latios|215\/304|[from] Stealth Rock
|move|p1a: Rikimaru|Earthquake|p2a: Latios
|-immune|p2a: Latios|[msg]
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|196\/304|[from] sandstorm
|turn|17
|
|switch|p1a: Gengen|Breloom, F|69\/307 tox
|move|p2a: Latios|Draco Meteor|p1a: Gengen
|-damage|p1a: Gengen|0 fnt
|-unboost|p2a: Latios|spa|2
|faint|p1a: Gengen
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|177\/304|[from] sandstorm
|
|switch|p1a: Nanami|Jirachi|218\/403
|turn|18
|
|switch|p2a: Metagross|Metagross|344\/344
|-damage|p2a: Metagross|323\/344|[from] Stealth Rock
|move|p1a: Nanami|Wish|p1a: Nanami
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|243\/403|[from] item: Leftovers
|turn|19
|
|move|p1a: Nanami|Protect|p1a: Nanami
|-singleturn|p1a: Nanami|Protect
|move|p2a: Metagross|Explosion|p1a: Nanami
|-activate|p1a: Nanami|Protect
|faint|p2a: Metagross
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|403\/403|[from] move: Wish|[wisher] Nanami
|c|★Leftiez~|oops
|c|★Reymedy|lol
|
|switch|p2a: Latios|Latios, M|177\/304
|-damage|p2a: Latios|139\/304|[from] Stealth Rock
|turn|20
|c|&iconic|dun dun dun
|c|★Leftiez~|i forgot
|c|★Leftiez~|it has protect Oo
|
|switch|p1a: Riou|Zapdos|81\/383
|-ability|p1a: Riou|Pressure
|move|p2a: Latios|Trick|p1a: Riou
|-activate|p2a: Latios|move: Trick|[of] p1a: Riou
|-item|p1a: Riou|Choice Specs|[from] move: Trick
|-item|p2a: Latios|Leftovers|[from] move: Trick
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|120\/304|[from] sandstorm
|-damage|p1a: Riou|58\/383|[from] sandstorm
|-heal|p2a: Latios|139\/304|[from] item: Leftovers
|turn|21
|
|switch|p1a: Nanami|Jirachi|403\/403
|move|p2a: Latios|Psyshock|p1a: Nanami
|-resisted|p1a: Nanami
|-damage|p1a: Nanami|368\/403
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|120\/304|[from] sandstorm
|-heal|p2a: Latios|139\/304|[from] item: Leftovers
|-heal|p1a: Nanami|393\/403|[from] item: Leftovers
|turn|22
|
|move|p2a: Latios|Hidden Power|p1a: Nanami
|-supereffective|p1a: Nanami
|-damage|p1a: Nanami|275\/403
|move|p1a: Nanami|Body Slam|p2a: Latios
|-damage|p2a: Latios|64\/304
|-status|p2a: Latios|par
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|45\/304 par|[from] sandstorm
|-heal|p1a: Nanami|300\/403|[from] item: Leftovers
|-heal|p2a: Latios|64\/304 par|[from] item: Leftovers
|turn|23
|
|move|p1a: Nanami|Protect|p1a: Nanami
|-singleturn|p1a: Nanami|Protect
|move|p2a: Latios|Hidden Power|p1a: Nanami
|-activate|p1a: Nanami|Protect
|
|-weather|Sandstorm|[upkeep]
|-damage|p2a: Latios|45\/304 par|[from] sandstorm
|-heal|p1a: Nanami|325\/403|[from] item: Leftovers
|-heal|p2a: Latios|64\/304 par|[from] item: Leftovers
|turn|24
|c|★Leftiez~|gg
|L|Korby
|
|move|p1a: Nanami|Iron Head|p2a: Latios
|-damage|p2a: Latios|0 fnt
|faint|p2a: Latios
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|350\/403|[from] item: Leftovers
|c|★Reymedy|gg
|
|switch|p2a: Hippowdon|Hippowdon, F|34\/420 tox
|-damage|p2a: Hippowdon|8\/420 tox|[from] Stealth Rock
|turn|25
|L|Marshall.Law
|
|move|p1a: Nanami|Body Slam|p2a: Hippowdon
|-damage|p2a: Hippowdon|0 fnt
|faint|p2a: Hippowdon
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|375\/403|[from] item: Leftovers
|
|switch|p2a: Stoutland|Stoutland, F|312\/312
|-damage|p2a: Stoutland|273\/312|[from] Stealth Rock
|turn|26
|
|move|p2a: Stoutland|Superpower|p1a: Nanami
|-damage|p1a: Nanami|194\/403
|-unboost|p2a: Stoutland|atk|1
|-unboost|p2a: Stoutland|def|1
|-damage|p2a: Stoutland|242\/312|[from] item: Life Orb
|move|p1a: Nanami|Body Slam|p2a: Stoutland
|-damage|p2a: Stoutland|126\/312
|-status|p2a: Stoutland|par
|
|-weather|Sandstorm|[upkeep]
|-heal|p1a: Nanami|219\/403|[from] item: Leftovers
|turn|27
|
|move|p1a: Nanami|Iron Head|p2a: Stoutland
|-damage|p2a: Stoutland|0 fnt
|faint|p2a: Stoutland
|
|win|Reymedy
|L|boudouche
|L|Leftiez~
|player|p2
|c|+Ybel|wp
|L|Reymedy
|player|p1
|L|Ybel
|L|Vinc2612
|L|Twin Citiez
|L|davidTheMaster
|L|Lusso.
|L|TewMew
        """

        const val SAMPLE_FILE = "C:\\Users\\nerow\\Downloads\\Gen5OU-2015-05-10-reymedy-leftiez.html"
        const val SAMPLE_FILE_2 =
            "C:\\Users\\nerow\\Downloads\\Gen8NationalDex-2022-06-19-dinglemanschmidt-neroweneed.html"
        const val SAMPLE_FILE_3 = "C:/Users/nerow/Downloads/Gen8NationalDex-2022-07-24-ninjatonguescarf-neroweneed.html"
        const val SAMPLE_FILE_4 = "C:\\Users\\nerow\\Downloads\\Ubers-2015-04-26-panamaxis-smogmog.html"
        const val SAMPLE_FILE_5 = "C:\\Users\\nerow\\Downloads\\Gen3OU-2015-05-02-pokebasket-alf.html"
    }

    @Test
    fun sampleCommand() {
        val replay = MutableReplay()
        for (message in FileReplayStream(File(SAMPLE_FILE_3))) {
            if (message is IBattleCommand) {
                message(replay)
                if (message.terminate)
                    break
            }
        }
        replay.gameState.run {
            if (this is GameState.Inactive) {
                if (winner != null) {
                    val player = replay.players.first { it.username == winner }
                    println("$winner wins ${player.team.count { it.status != PokemonStatus.FAINTED }}-0")
                }

            }
        }


        println(replay)
    }
}