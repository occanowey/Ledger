package com.github.quiltservertools.ledger.network.packet

import com.github.quiltservertools.ledger.utility.PlayerResult
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

class PlayerPacket : LedgerPacket<PlayerResult> {
    override val channel: Identifier = LedgerPacketTypes.PLAYER.id
    override var buf: PacketByteBuf = PacketByteBufs.create()
    override fun populate(content: PlayerResult) {
        buf.writeUuid(content.uuid)
        buf.writeString(content.name)
        buf.writeLong(content.firstJoin.epochSecond)
        buf.writeLong(content.lastJoin.epochSecond)
        buf.writeBlockPos(content.lastJoinPos)
        buf.writeIdentifier(content.world)
    }
}
