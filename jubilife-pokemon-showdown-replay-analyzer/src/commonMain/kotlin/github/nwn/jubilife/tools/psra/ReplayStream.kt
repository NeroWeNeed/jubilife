package github.nwn.jubilife.tools.psra

import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.IMessage

interface ReplayStream : Iterable<IMessage> {
    companion object

}


expect fun ReplayStream.Companion.getCommandMap() : Map<String,IBattleCommandFactory>