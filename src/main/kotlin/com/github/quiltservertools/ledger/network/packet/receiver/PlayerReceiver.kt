package com.github.quiltservertools.ledger.network.packet.receiver

import com.github.quiltservertools.ledger.Ledger
import com.github.quiltservertools.ledger.commands.CommandConsts
import com.github.quiltservertools.ledger.commands.subcommands.PlayerCommand
import com.github.quiltservertools.ledger.database.DatabaseManager
import com.github.quiltservertools.ledger.network.Networking
import com.github.quiltservertools.ledger.network.packet.LedgerPacketTypes
import com.github.quiltservertools.ledger.network.packet.PlayerPacket
import com.github.quiltservertools.ledger.network.packet.Receiver
import com.github.quiltservertools.ledger.network.packet.response.ResponseCodes
import com.github.quiltservertools.ledger.network.packet.response.ResponseContent
import com.github.quiltservertools.ledger.network.packet.response.ResponsePacket
import com.mojang.authlib.GameProfile
import kotlinx.coroutines.launch
import me.lucko.fabric.api.permissions.v0.Permissions
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

class PlayerReceiver : Receiver {
    override fun receive(
        server: MinecraftServer,
        player: ServerPlayerEntity,
        handler: ServerPlayNetworkHandler,
        buf: PacketByteBuf,
        sender: PacketSender
    ) {
        if (!Permissions.check(player, PlayerCommand.PERMISSION_NODE, CommandConsts.PERMISSION_LEVEL) ||
            !Permissions.check(player, Networking.PERMISSION_NODE, CommandConsts.PERMISSION_LEVEL)) {
            ResponsePacket.sendResponse(ResponseContent(LedgerPacketTypes.PLAYER.id, ResponseCodes.NO_PERMISSION.code), sender)
            return
        }

        val length = buf.readInt()
        val profiles = mutableSetOf<GameProfile>()

        for (i in 0 until length) {
            val uuid = buf.readUuid()
            profiles.add(GameProfile(uuid, ""))
        }

        Ledger.launch {
            val results = DatabaseManager.searchPlayers(profiles)
            results.forEach {
                val packet = PlayerPacket()
                packet.populate(it)
                sender.sendPacket(LedgerPacketTypes.PLAYER.id, packet.buf)
            }
        }
    }
}
