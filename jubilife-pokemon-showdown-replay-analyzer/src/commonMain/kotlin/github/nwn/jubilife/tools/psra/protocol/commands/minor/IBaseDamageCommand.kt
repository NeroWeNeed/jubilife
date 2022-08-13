package github.nwn.jubilife.tools.psra.protocol.commands.minor

import github.nwn.jubilife.tools.psra.protocol.IBattleCommand
import github.nwn.jubilife.tools.psra.protocol.MutableReplay
import github.nwn.jubilife.tools.psra.protocol.PokemonStatus
import github.nwn.jubilife.tools.psra.protocol.Position

interface IBaseDamageCommand : IBattleCommand {
    val position: Position
    val name: String
    val health: Float
    val status: PokemonStatus

    override fun invoke(replay: MutableReplay) {
        replay.update(replay.battle[position]) {
            it.copy(
                health = health,
                status = if (health == 0f) PokemonStatus.FAINTED else status
            )
        }
    }
}