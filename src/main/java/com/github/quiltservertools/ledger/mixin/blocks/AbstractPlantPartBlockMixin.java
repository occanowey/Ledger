package com.github.quiltservertools.ledger.mixin.blocks;

import com.github.quiltservertools.ledger.callbacks.BlockBreakCallback;
import com.github.quiltservertools.ledger.utility.Sources;
import java.util.Random;
import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPlantPartBlock.class)
public abstract class AbstractPlantPartBlockMixin {
    @Inject(method = "scheduledTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;breakBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    public void logPlantBreaks(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockBreakCallback.EVENT.invoker().breakBlock(world, pos, state, null, Sources.BROKE);
    }
}
