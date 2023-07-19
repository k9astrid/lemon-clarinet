package dev.lemon.api.bot.world;

import com.google.common.collect.Sets;
import dev.lemon.api.bot.entity.BotPlayer;
import dev.lemon.api.bot.network.BotPlayClient;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

import java.util.Set;

@Getter @Setter
public class BotWorld extends World {
    private ChunkProviderClient clientChunkProvider;

    private final BotPlayClient connection;

    private final Set<Entity> entityList, entitySpawnQueue;

    private BotPlayer bot;

    public BotWorld(BotPlayClient botPlayClient, WorldSettings worldSettings, int dimension, EnumDifficulty difficulty) {
        super(new SaveHandlerMP(), new WorldInfo(worldSettings, "MpServer"), DimensionType.getById(dimension).createDimension(), Minecraft.getMinecraft().mcProfiler, true);

        this.entityList = Sets.newHashSet();
        this.entitySpawnQueue = Sets.newHashSet();
        this.connection = botPlayClient;
        getWorldInfo().setDifficulty(difficulty);
        this.provider.setWorldObj(this);
        setSpawnPoint(new BlockPos(8, 64, 8));
        this.chunkProvider = createChunkProvider();
        this.mapStorage = new SaveDataMemoryStorage();
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        this.clientChunkProvider = new ChunkProviderClient(this);
        return this.clientChunkProvider;
    }

    @Override
    protected int getRenderDistanceChunks() {
        return 50;
    }

    public void addEntityToWorld(int entityID, Entity entityToSpawn) {
        Entity entity = getEntityByID(entityID);

        if (entity != null)
            removeEntity(entity);

        this.entityList.add(entityToSpawn);

        if (!spawnEntityInWorld(entityToSpawn))
            this.entitySpawnQueue.add(entityToSpawn);

        this.entitiesById.addKey(entityID, entityToSpawn);
    }

    @Deprecated
    public boolean invalidateRegionAndSetBlock(BlockPos blockPos, IBlockState iBlockState) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        return setBlockState(blockPos, iBlockState, 3);
    }

    public void doPreChunk(int x, int z, boolean fullChunk) {
        if (fullChunk) {
            this.clientChunkProvider.loadChunk(x, z);
        } else {
            this.clientChunkProvider.unloadChunk(x, z);
            markBlockRangeForRenderUpdate(x * 16, 0, z * 16, x * 16 + 15, 256, z * 16 + 15);
        }
    }
}
