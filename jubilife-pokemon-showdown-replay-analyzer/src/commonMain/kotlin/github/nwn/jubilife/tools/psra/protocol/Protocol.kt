package github.nwn.jubilife.tools.psra.protocol

interface IMessage {
    val value: String
}

@JvmInline
value class RawMessage(override val value: String) : IMessage

sealed interface ICommand : IMessage

@JvmInline
value class UnknownCommand(override val value: String) : ICommand

interface IBattleCommand : ICommand {
    val terminate: Boolean
        get() = false

    operator fun invoke(replay: MutableReplay)
}

interface IBattleCommandFactory {
    val commandName: String
    fun create(value: String): IBattleCommand? {
        val tokens = value.substring(1).split('|')
        if (tokens.isNotEmpty()) {
            return create(value, tokens[0], tokens.drop(1))
        }
        return null
    }

    fun create(value: String, command: String, tokens: List<String>): IBattleCommand
}