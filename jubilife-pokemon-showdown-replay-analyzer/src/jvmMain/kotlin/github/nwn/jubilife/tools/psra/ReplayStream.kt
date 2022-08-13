@file:JvmName("JVMReplayStream")

package github.nwn.jubilife.tools.psra

import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.IMessage
import github.nwn.jubilife.tools.psra.protocol.RawMessage
import github.nwn.jubilife.tools.psra.protocol.UnknownCommand
import org.jsoup.Jsoup
import java.io.File
import java.io.StringReader
import java.util.ServiceLoader

private val BATTLE_COMMAND_LOADER by lazy {
    ServiceLoader.load(IBattleCommandFactory::class.java)
}
private var BATTLE_COMMANDS: Map<String, IBattleCommandFactory>? = null
actual fun ReplayStream.Companion.getCommandMap(): Map<String, IBattleCommandFactory> {
    if (BATTLE_COMMANDS == null) {
        BATTLE_COMMANDS = BATTLE_COMMAND_LOADER.associateBy { it.commandName }
    }
    return BATTLE_COMMANDS ?: emptyMap()
}

class FileReplayStream : ReplayStream {
    private val lines: Sequence<IMessage>

    constructor(file: File) {

        val content = Jsoup.parse(file).select("script.battle-log-data[type=text/plain]").first()?.data() ?: ""
        if (content.isEmpty()) {
            throw IllegalArgumentException("Invalid Replay File")
        }
        lines = StringReader(content).readLines().asSequence().mapNotNull { message ->
            message.trim().let {
                if (it.startsWith(ICommandHandlerSet.Impl.MESSAGE_PREFIX) && it.length > 1) {
                    val commandMessage = it.substring(1)
                    val tokens = commandMessage.split('|')
                    if (tokens.isNotEmpty()) {
                        ReplayStream.getCommandMap()[tokens[0]]?.create(commandMessage)
                    } else
                        UnknownCommand(it)
                } else
                    RawMessage(it)
            }

        }
    }

    override fun iterator(): Iterator<IMessage> {
        return lines.iterator()
    }


}