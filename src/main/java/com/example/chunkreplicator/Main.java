package com.example.chunkreplicator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("chunkreplicator")
public class Main {
    private static final int RADIUS = 25;

    public Main() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlace(BlockEvent.EntityPlaceEvent e) {
        if (e.getLevel().isClientSide()) return;
        Level w = (Level) e.getLevel();
        BlockState b = e.getPlacedBlock();
        int y = e.getPos().getY();
        if (w.players().isEmpty()) return;
        BlockPos p = w.players().get(0).blockPosition();
        int cx = p.getX() >> 4;
        int cz = p.getZ() >> 4;
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                if (w.hasChunk(cx+dx, cz+dz)) {
                    LevelChunk c = w.getChunk(cx+dx, cz+dz);
                    BlockPos tp = new BlockPos((cx+dx)*16+8, y, (cz+dz)*16+8);
                    if (c.getBlockState(tp).canBeReplaced()) c.setBlockState(tp, b, false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent e) {
        if (e.getLevel().isClientSide()) return;
        Level w = (Level) e.getLevel();
        int y = e.getPos().getY();
        if (w.players().isEmpty()) return;
        BlockPos p = w.players().get(0).blockPosition();
        int cx = p.getX() >> 4;
        int cz = p.getZ() >> 4;
        for (int dx = -RADIUS; dx <= RADIUS; dx++) {
            for (int dz = -RADIUS; dz <= RADIUS; dz++) {
                if (w.hasChunk(cx+dx, cz+dz)) {
                    LevelChunk c = w.getChunk(cx+dx, cz+dz);
                    c.setBlockState(new BlockPos((cx+dx)*16+8, y, (cz+dz)*16+8), Blocks.AIR.defaultBlockState(), false);
                }
            }
        }
    }
}
