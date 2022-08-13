package github.nwn.jubilife.tools.psra

class CommandHandlerSetBuilder<State : Any> {
    private val commands = HashMap<String, ICommand<State, *>>()
    fun <Payload : Any> command(type: String, op: CommandHandlerBuilder<State, Payload>.() -> Unit) {
        commands[type] = CommandHandlerBuilder<State, Payload>().apply(op).build()
    }

    fun commandRaw(type: String, op: CommandHandler<State, List<String>>) {
        commands[type] = CommandRaw(op)
    }

    internal fun build() = ICommandHandlerSet.Impl(commands.toMap())
}

class CommandHandlerBuilder<State : Any, Payload : Any> {
    var transformer: TokenTransformer<State, Payload>? = null
    var command: CommandHandler<State, Payload>? = null
    fun transformer(op: TokenTransformer<State, Payload>) {
        transformer = op
    }

    fun command(op: CommandHandler<State, Payload>) {
        command = op
    }

    internal fun build(): ICommand<State, *> {
        if (command == null) {
            throw IllegalStateException("Must specify Command")
        }
        if (transformer == null) {
            throw IllegalStateException("Must specify Transformer")
        }
        return Command(transformer!!, command!!)
    }
}

fun <T : Any> Commands(op: CommandHandlerSetBuilder<T>.() -> Unit) = CommandHandlerSetBuilder<T>().apply(op).build()

interface ICommandHandlerSet<T : Any> {
    fun handle(message: String, state: T)
    fun handle(messages: Sequence<String>, state: T) {
        messages.forEachIndexed { index, message ->
            try {
                handle(message, state)
            } catch (exception: Exception) {
                throw PSRAParseException(message, index, exception)
            }
        }
    }

    class Impl<T : Any>(val commands: Map<String, ICommand<T, *>> = emptyMap()) : ICommandHandlerSet<T> {
        companion object {
            const val MESSAGE_PREFIX = '|'
        }

        override fun handle(message: String, state: T) {
            message.trim().also {
                if (it.startsWith(MESSAGE_PREFIX) && it.length > 1) {
                    val tokens = it.substring(1).split('|')
                    if (tokens.isNotEmpty()) {
                        commands[tokens[0]]?.invoke(tokens.drop(1), state)
                    }
                }
            }

        }

    }

}


fun interface TokenTransformer<State : Any, Payload : Any> {
    fun transform(tokens: List<String>, state: State): Payload
}

fun interface CommandHandler<State : Any, Payload : Any> {
    fun handle(payload: Payload, state: State)
}

interface ICommand<State : Any, Payload : Any> : TokenTransformer<State, Payload>, CommandHandler<State, Payload> {
    operator fun invoke(tokens: List<String>, state: State) {
        handle(transform(tokens, state), state)
    }
}

data class Command<State : Any, Payload : Any>(
    private val transformer: TokenTransformer<State, Payload>,
    private val commandHandler: CommandHandler<State, Payload>
) : ICommand<State, Payload> {
    override fun transform(tokens: List<String>, state: State): Payload {
        return transformer.transform(tokens, state)
    }

    override fun handle(payload: Payload, state: State) {
        commandHandler.handle(payload, state)
    }

}

data class CommandRaw<State : Any>(
    private val commandHandler: CommandHandler<State, List<String>>
) : ICommand<State, List<String>> {
    override fun transform(tokens: List<String>, state: State): List<String> {
        return tokens
    }

    override fun handle(payload: List<String>, state: State) {
        commandHandler.handle(payload, state)
    }

}