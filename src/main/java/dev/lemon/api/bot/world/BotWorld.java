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

    public Entity removeEntityFromWorld(int p_73028_1_)
    {
        Entity entity = this.entitiesById.removeObject(p_73028_1_);

        if (entity != null)
        {
            this.entityList.remove(entity);
            this.removeEntity(entity);
        }

        return entity;
    }

    public void removeAllEntities()
    {
        this.loadedEntityList.removeAll(this.unloadedEntityList);

        for (int i = 0; i < this.unloadedEntityList.size(); ++i)
        {
            Entity entity = (Entity)this.unloadedEntityList.get(i);
            int j = entity.chunkCoordX;
            int k = entity.chunkCoordZ;

            if (entity.addedToChunk && this.isChunkLoaded(j, k, true))
            {
                this.getChunkFromChunkCoords(j, k).removeEntity(entity);
            }
        }

        for (int l = 0; l < this.unloadedEntityList.size(); ++l)
        {
            this.onEntityRemoved((Entity)this.unloadedEntityList.get(l));
        }

        this.unloadedEntityList.clear();

        for (int i1 = 0; i1 < this.loadedEntityList.size(); ++i1)
        {
            Entity entity1 = (Entity)this.loadedEntityList.get(i1);

            if (entity1.ridingEntity != null)
            {
                if (!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1)
                {
                    continue;
                }

                entity1.ridingEntity.riddenByEntity = null;
                entity1.ridingEntity = null;
            }

            if (entity1.isDead)
            {
                int j1 = entity1.chunkCoordX;
                int k1 = entity1.chunkCoordZ;

                if (entity1.addedToChunk && this.isChunkLoaded(j1, k1, true))
                {
                    this.getChunkFromChunkCoords(j1, k1).removeEntity(entity1);
                }

                this.loadedEntityList.remove(i1--);
                this.onEntityRemoved(entity1);
            }
        }
    }
}
