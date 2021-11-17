package com.github.quiltservertools.ledger.utility

import com.github.quiltservertools.ledger.database.Tables
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import java.time.Instant
import java.util.*
import kotlin.time.ExperimentalTime

data class PlayerResult(
    val uuid: UUID,
    val name: String,
    val firstJoin: Instant,
    val lastJoin: Instant,
    val lastJoinPos: BlockPos,
    val world: Identifier
) {

    @OptIn(ExperimentalTime::class)
    fun toText(): Text {
        return TranslatableText(
            "text.ledger.player.result",
            name.literal().setStyle(TextColorPallet.light),
            MessageUtils.instantToText(firstJoin).setStyle(TextColorPallet.primaryVariant),
            MessageUtils.instantToText(lastJoin).setStyle(TextColorPallet.primaryVariant)
        ).setStyle(TextColorPallet.secondary)
    }

    companion object {
        fun fromRow(row: Tables.Player): PlayerResult = PlayerResult(
            row.playerId, row.playerName, row.firstJoin, row.lastJoin,
            BlockPos(row.lastLeaveX, row.lastLeaveY, row.lastLeaveZ),
            Identifier(row.lastLeaveWorld)
        )
    }
}
