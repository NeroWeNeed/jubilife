package github.nwn.jubilife.tools.psra

import github.nwn.jubilife.tools.psra.protocol.IBattleCommandFactory
import github.nwn.jubilife.tools.psra.protocol.commands.major.*
import github.nwn.jubilife.tools.psra.protocol.commands.minor.*


class CureStatusCommandService : IBattleCommandFactory by CureStatusCommand.Companion
class CureTeamCommandService : IBattleCommandFactory by CureTeamCommand.Companion
class DamageCommandService : IBattleCommandFactory by DamageCommand.Companion
class EndItemCommandService : IBattleCommandFactory by EndItemCommand.Companion
class HealCommandService : IBattleCommandFactory by HealCommand.Companion
class ItemCommandService : IBattleCommandFactory by ItemCommand.Companion
class SetHPCommandService : IBattleCommandFactory by SetHPCommand.Companion
class StatusCommandService : IBattleCommandFactory by StatusCommand.Companion
class WinCommandService : IBattleCommandFactory by WinCommand.Companion
class TeamSizeCommandService : IBattleCommandFactory by TeamSizeCommand.Companion
class GameTypeCommandService : IBattleCommandFactory by GameTypeCommand.Companion
class DetailsChangeCommandService : IBattleCommandFactory by DetailsChangeCommand.Companion
class TurnCommandService : IBattleCommandFactory by TurnCommand.Companion
class TieCommandService : IBattleCommandFactory by TieCommand.Companion
class PokeCommandService : IBattleCommandFactory by PokeCommand.Companion
class PlayerCommandService : IBattleCommandFactory by PlayerCommand.Companion
class FaintCommandService : IBattleCommandFactory by FaintCommand.Companion
class SwitchCommandService : IBattleCommandFactory by SwitchCommand.Companion
class ClearPokeCommandService : IBattleCommandFactory by ClearPokeCommand.Companion
class ReplaceCommandService : IBattleCommandFactory by ReplaceCommand.Companion