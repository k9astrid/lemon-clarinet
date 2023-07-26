package dev.lemon.api.bot.network;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import dev.lemon.api.bot.Bot;
import dev.lemon.api.bot.BotStarter;
import dev.lemon.api.bot.entity.BotController;
import dev.lemon.api.bot.entity.BotPlayer;
import dev.lemon.api.bot.proxy.Proxy;
import dev.lemon.api.bot.world.BotWorld;
import dev.lemon.api.utils.player.ChatUtil;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.*;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.input.Keyboard;
import viamcp.ViaMCP;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
public class BotPlayClient implements INetHandlerPlayClient {

    private final BotNetwork network;

    private BotController controller;

    private BotPlayer bot;

    private final GameProfile profile;

    public BotWorld world;

    private final Map<UUID, NetworkPlayerInfo> playerInfoMap;

    public final MovementInput movementInput;

    public boolean jump, sneak, forward, backward, left, right;

    private boolean doneLoadingTerrain, reg;

    public BotPlayClient(BotNetwork network, GameProfile gameProfile) {
        this.playerInfoMap = Maps.newHashMap();

        this.movementInput = new MovementInput() {
            @Override
            public void updatePlayerMoveState() {
                this.moveForward = 0.0F;
                this.moveStrafe = 0.0F;
                this.jump = false;
                this.sneak = false;

                if (!((Minecraft.getMinecraft()).currentScreen instanceof GuiChat)) {
                    if (Keyboard.isKeyDown(72) || Keyboard.isKeyDown(200) || BotPlayClient.this.forward)
                        this.moveForward++;
                    if (Keyboard.isKeyDown(76) || Keyboard.isKeyDown(208) || BotPlayClient.this.backward)
                        this.moveForward--;
                    if (Keyboard.isKeyDown(75) || Keyboard.isKeyDown(203) || BotPlayClient.this.left)
                        this.moveStrafe++;
                    if (Keyboard.isKeyDown(77) || Keyboard.isKeyDown(205) || BotPlayClient.this.right)
                        this.moveStrafe--;
                    if (Keyboard.isKeyDown(79) || BotPlayClient.this.jump || BotPlayClient.this.bot.isInWater())
                        this.jump = true;
                    if (Keyboard.isKeyDown(81) || BotPlayClient.this.sneak) {
                        this.sneak = true;
                        this.moveStrafe = (float)(this.moveStrafe * 0.3D);
                        this.moveForward = (float)(this.moveForward * 0.3D);
                    }
                }
            }
        };

        this.network = network;
        this.profile = gameProfile;
    }

    public void sendPacket(Packet<?> packet) {
        this.network.sendPacket(packet);
    }

    @Override
    public void onDisconnect(IChatComponent reason) {
        Bot.bots.remove(getBot2());

        this.network.closeChannel();

        (new Thread(() -> {
            Proxy proxy = getBot2().getNetwork().proxy;
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), getProfile().getName());

            try {
                BotNetwork botNetwork = BotNetwork.createNetworkManagerAndConnect(InetAddress.getByName(GuiConnecting.ip), GuiConnecting.port, proxy);
                botNetwork.setNetHandler(new BotLoginClient(botNetwork));
                botNetwork.sendPacket(new C00Handshake(ViaMCP.getInstance().getVersion(), GuiConnecting.ip, GuiConnecting.port, EnumConnectionState.LOGIN));
                botNetwork.sendPacket(new C00PacketLoginStart(gameProfile));
            } catch (Exception ignored) { }
        })).start();
    }

    @Override
    public void handleSpawnObject(S0EPacketSpawnObject packetIn) { }

    @Override
    public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn) { }

    @Override
    public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn) { }

    @Override
    public void handleSpawnMob(S0FPacketSpawnMob packetIn) {
        double x = packetIn.getX();
        double y = packetIn.getY();
        double z = packetIn.getZ();
        float f = (packetIn.getYaw() * 360) / 256.f;
        float f2 = (packetIn.getPitch() * 360) / 256.f;

        EntityLivingBase entityLivingBase = (EntityLivingBase) EntityList.createEntityByID(packetIn.getEntityType(), this.world);

        if (entityLivingBase != null) {
            entityLivingBase.renderYawOffset = (packetIn.getHeadPitch() * 360) / 256.f;
            entityLivingBase.rotationYawHead = (packetIn.getHeadPitch() * 360) / 256.f;

            Entity[] array = entityLivingBase.getParts();
            if (array != null) {
                int id = packetIn.getEntityID() - entityLivingBase.getEntityId();

                for (Entity entity : array)
                    entity.setEntityId(entity.getEntityId() + id);
            }

            entityLivingBase.setEntityId(packetIn.getEntityID());

            entityLivingBase.setPositionAndRotation(x, y, z, f, f2);

            entityLivingBase.motionX = (packetIn.getVelocityX() / 8000.f);
            entityLivingBase.motionY = (packetIn.getVelocityY() / 8000.f);
            entityLivingBase.motionZ = (packetIn.getVelocityZ() / 8000.f);

            this.world.addEntityToWorld(packetIn.getEntityID(), entityLivingBase);

            List<DataWatcher.WatchableObject> list = packetIn.func_149027_c();

            if (list != null)
            {
                bot.getDataWatcher().updateWatchedObjectsFromList(list);
            }
        }
    }

    @Override
    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) { }

    @Override
    public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {
        EntityPainting entityPainting = new EntityPainting(this.world, packetIn.getPosition(), packetIn.getFacing(), packetIn.getTitle());
        this.world.addEntityToWorld(packetIn.getEntityID(), entityPainting);
    }

    @Override
    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {
        double x = packetIn.getX();
        double y = packetIn.getY();
        double z = packetIn.getZ();

        float f = (packetIn.getYaw() * 360) / 256.f;
        float f2 = (packetIn.getPitch() * 360) / 256.f;

        EntityOtherPlayerMP entityOtherPlayerMP = new EntityOtherPlayerMP(this.world, getPlayerInfo(packetIn.getPlayer()).getGameProfile());
        entityOtherPlayerMP.prevPosX = x;
        entityOtherPlayerMP.lastTickPosX = x;
        entityOtherPlayerMP.prevPosY = y;
        entityOtherPlayerMP.lastTickPosY = y;
        entityOtherPlayerMP.prevPosZ = z;
        entityOtherPlayerMP.lastTickPosZ = z;

        entityOtherPlayerMP.setPositionAndRotation(x, y, z, f, f2);

        this.world.addEntityToWorld(packetIn.getEntityID(), entityOtherPlayerMP);

        List<DataWatcher.WatchableObject> list = packetIn.func_148944_c();

        if (list != null)
        {
            bot.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    @Override
    public void handleAnimation(S0BPacketAnimation packetIn) { }

    @Override
    public void handleStatistics(S37PacketStatistics packetIn) { }

    @Override
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {
        this.world.sendBlockBreakProgress(packetIn.getBreakerId(), packetIn.getPosition(), packetIn.getProgress());
    }

    @Override
    public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) { }

    @Override
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {
        if (this.world.isBlockLoaded(packetIn.getPos())) {
            TileEntity tileEntity = this.world.getTileEntity(packetIn.getPos());

            int type = packetIn.getTileEntityType();

            boolean cock = (type == 2 && tileEntity instanceof TileEntityCommandBlock);

            if ((type == 1 && tileEntity instanceof TileEntityMobSpawner) ||
                    cock ||
                    (type == 3 && tileEntity instanceof TileEntityBeacon) ||
                    (type == 4 && tileEntity instanceof TileEntitySkull) ||
                    (type == 5 && tileEntity instanceof TileEntityFlowerPot) ||
                    (type == 6 && tileEntity instanceof TileEntityBanner))
                tileEntity.readFromNBT(packetIn.getNbtCompound());
        }
    }

    @Override
    public void handleBlockAction(S24PacketBlockAction packetIn) {
        this.world.addBlockEvent(packetIn.getBlockPosition(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }

    @Override
    public void handleBlockChange(S23PacketBlockChange packetIn) {
        this.world.invalidateRegionAndSetBlock(packetIn.getBlockPosition(), packetIn.getBlockState());
    }

    @Override
    public void handleChat(S02PacketChat packetIn) {
        String string = stripColor(packetIn.getChatComponent().getFormattedText());

        if (string.contains("/reg") && !this.reg) {
            this.reg = true;
            (new Thread(() -> {
                String pass = RandomStringUtils.randomAlphabetic(MathHelper.getRandomNumberBetween(6, 8)).toLowerCase();
                this.bot.sendChatMessage(String.format("/register %s %s", pass, pass));
                ChatUtil.send("Successfully registered " + this.bot.getName() + " with " + pass);
            })).start();
        }
    }

    @Override
    public void handleTabComplete(S3APacketTabComplete packetIn) { }

    @Override
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
        for (S22PacketMultiBlockChange.BlockUpdateData blockUpdateData : packetIn.getChangedBlocks())
            this.world.invalidateRegionAndSetBlock(blockUpdateData.getPos(), blockUpdateData.getBlockState());
    }

    @Override
    public void handleMaps(S34PacketMaps packetIn) { }

    @Override
    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {
        Container container = null;
        BotPlayer botPlayer = this.bot;

        if (packetIn.getWindowId() == 0)
            container = botPlayer.inventoryContainer;
        else if (packetIn.getWindowId() == botPlayer.openContainer.windowId)
            container = botPlayer.openContainer;

        if (container != null && !packetIn.func_148888_e())
            sendPacket(new C0FPacketConfirmTransaction(packetIn.getWindowId(), packetIn.getActionNumber(), true));
    }

    @Override
    public void handleCloseWindow(S2EPacketCloseWindow packetIn) {
        this.bot.closeScreenAndDropStack();
    }

    @Override
    public void handleWindowItems(S30PacketWindowItems packetIn) {
        BotPlayer botPlayer = this.bot;

        if (packetIn.func_148911_c() == 0)
            botPlayer.inventoryContainer.setAll(Arrays.asList(packetIn.getItemStacks()));
        else if (packetIn.func_148911_c() == botPlayer.openContainer.windowId) {
            botPlayer.openContainer.setAll(Arrays.asList(packetIn.getItemStacks()));
        }
    }

    @Override
    public void handleOpenWindow(S2DPacketOpenWindow packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, Minecraft.getMinecraft());

        this.bot.currentContainerName = packetIn.getWindowTitle().getUnformattedText();

        if ("minecraft:container".equals(packetIn.getGuiId())) {
            this.bot.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            this.bot.openContainer.windowId = packetIn.getWindowId();
        } else if ("minecraft:villager".equals(packetIn.getGuiId())) {
            this.bot.displayVillagerTradeGui(new NpcMerchant(this.bot, packetIn.getWindowTitle()));
            this.bot.openContainer.windowId = packetIn.getWindowId();
        } else if ("EntityHorse".equals(packetIn.getGuiId())) {
            Entity entity = this.world.getEntityByID(packetIn.getEntityId());
            if (entity instanceof EntityHorse)
                this.bot.openContainer.windowId = packetIn.getWindowId();
        } else if (!packetIn.hasSlots()) {
            this.bot.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            this.bot.openContainer.windowId = packetIn.getWindowId();
        } else {
            ContainerLocalMenu containerLocalMenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());

            this.bot.displayGUIChest(containerLocalMenu);
            this.bot.openContainer.windowId = packetIn.getWindowId();
        }
    }

    @Override
    public void handleWindowProperty(S31PacketWindowProperty packetIn) {
        BotPlayer botPlayer = this.bot;

        if (botPlayer.openContainer != null && botPlayer.openContainer.windowId == packetIn.getWindowId())
            botPlayer.openContainer.updateProgressBar(packetIn.getVarIndex(), packetIn.getVarValue());
    }

    @Override
    public void handleSetSlot(S2FPacketSetSlot packetIn) {
        ItemStack itemStack = packetIn.func_149174_e();
        int slot = packetIn.func_149173_d();

        if (packetIn.func_149175_c() == -1)
            this.bot.inventory.setItemStack(itemStack);
        else if (packetIn.func_149175_c() == -2)
            this.bot.inventory.setInventorySlotContents(slot, itemStack);
        else {
            boolean flag = false;

            if (BotStarter.mc.currentScreen instanceof GuiContainerCreative) {
                GuiContainerCreative containerCreative = (GuiContainerCreative) BotStarter.mc.currentScreen;
                flag = (containerCreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex());
            }

            if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && slot < 45) {
                if (!(itemStack == null)) {
                    ItemStack itemStack1 = this.bot.inventoryContainer.getSlot(slot).getStack();

                    if (itemStack1 == null || itemStack1.stackSize < itemStack.stackSize)
                        itemStack.setAnimationsToGo(5);
                }

                this.bot.inventoryContainer.putStackInSlot(slot, itemStack);
            } else if (packetIn.func_149175_c() == this.bot.openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag))
                this.bot.openContainer.putStackInSlot(slot, itemStack);
        }
    }

    @Override
    public void handleCustomPayload(S3FPacketCustomPayload packetIn) {
        if ("MC|Brand".equals(packetIn.getChannelName()))
            this.bot.setServerBrand(packetIn.getBufferData().readStringFromBuffer(32767));
    }

    @Override
    public void handleDisconnect(S40PacketDisconnect packetIn) {
        //TODO: idk just fill it up..
    }

    @Override
    public void handleUseBed(S0APacketUseBed packetIn) {
        packetIn.getPlayer(this.world).trySleep(packetIn.getBedPosition());
    }

    @Override
    public void handleEntityStatus(S19PacketEntityStatus packetIn) {
        Entity entity = packetIn.getEntity(this.world);

        if (entity != null && packetIn.getOpCode() != 21 && packetIn.getOpCode() != 35)
            entity.handleStatusUpdate(packetIn.getOpCode());
    }

    @Override
    public void handleEntityAttach(S1BPacketEntityAttach packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());
        Entity vehicleEntity = this.world.getEntityByID(packetIn.getVehicleEntityId());

        if (entity instanceof EntityLiving)
            if (vehicleEntity != null) {
                ((EntityLiving) entity).setLeashedToEntity(vehicleEntity, false);
            } else ((EntityLiving) entity).clearLeashed(false, false);
    }

    @Override
    public void handleExplosion(S27PacketExplosion packetIn) {
        Explosion explosion = new Explosion(this.world, null, packetIn.getX(), packetIn.getY(), packetIn.getZ(), packetIn.getStrength(), packetIn.getAffectedBlockPositions());
        explosion.doExplosionB(true);
        this.bot.motionX += packetIn.func_149149_c(); // Guessed
        this.bot.motionY += packetIn.func_149144_d(); // Guessed
        this.bot.motionZ += packetIn.func_149147_e(); // Guessed
    }

    @Override
    public void handleChangeGameState(S2BPacketChangeGameState packetIn) {
        int state = packetIn.getGameState();
        float value = packetIn.func_149137_d();
        int groszusIsAVerySexyGuyIFuckedHim = MathHelper.floor_double(value + .5f);

        switch (state) {
            case 1:
                this.world.getWorldInfo().setRaining(true);
                this.world.setRainStrength(.0f);
                break;

            case 2:
                this.world.getWorldInfo().setRaining(false);
                this.world.setRainStrength(1.f);
                break;

            case 3:
                this.controller.setGameType(WorldSettings.GameType.getByID(groszusIsAVerySexyGuyIFuckedHim));
                break;

            case 4:
                if (groszusIsAVerySexyGuyIFuckedHim == 0)
                    this.bot.connection.sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
                break;

            case 7:
                this.world.setRainStrength(value);
                break;

            case 8:
                this.world.setThunderStrength(value);
                break;
        }
    }

    @Override
    public void handleKeepAlive(S00PacketKeepAlive packetIn) {
        sendPacket(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }

    @Override
    public void handleChunkData(S21PacketChunkData packetIn) {
        if (packetIn.func_149274_i())
            this.world.doPreChunk(packetIn.getChunkX(), packetIn.getChunkZ(), true);

        Chunk chunk = this.world.getChunkFromChunkCoords(packetIn.getChunkX(), packetIn.getChunkZ());
        chunk.fillChunk(packetIn.func_149272_d(), packetIn.getExtractedSize(), packetIn.func_149274_i()); //From 766 NetHandlerPlayClient.java

        this.world.markBlockRangeForRenderUpdate(packetIn.getChunkX() << 4, 0, packetIn.getChunkZ() << 4, (packetIn.getChunkX() << 4) + 15, 256, (packetIn.getChunkZ() << 4) + 15);

        if (!packetIn.func_149274_i() || !(this.world.provider instanceof WorldProviderSurface))
            chunk.resetRelightChecks();
    }

    // Copy pasted from minecraft
    @Override
    public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, Minecraft.getMinecraft());

        for (int i = 0; i < packetIn.getChunkCount(); ++i)
        {
            int j = packetIn.getChunkX(i);
            int k = packetIn.getChunkZ(i);
            this.world.doPreChunk(j, k, true);
            Chunk chunk = this.world.getChunkFromChunkCoords(j, k);
            chunk.fillChunk(packetIn.getChunkBytes(i), packetIn.getChunkSize(i), true);
            this.world.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);

            if (!(this.world.provider instanceof WorldProviderSurface))
            {
                chunk.resetRelightChecks();
            }
        }
    }

    @Override
    public void handleEffect(S28PacketEffect packetIn) {
        if (packetIn.isSoundServerwide())
            this.world.playBroadcastSound(packetIn.getSoundType(), packetIn.getSoundPos(), packetIn.getSoundData());
    }

    @Override
    public void handleJoinGame(S01PacketJoinGame packetIn) {
        this.controller = new BotController(this);
        this.world = new BotWorld(
                this,
                new WorldSettings(
                    0L,
                    packetIn.getGameType(),
                    true,
                    packetIn.isHardcoreMode(),
                    packetIn.getWorldType()
                ),
                packetIn.getDimension(),
                packetIn.getDifficulty()
        );
        this.loadWorld(world);

        this.bot.dimension = packetIn.getDimension();
        this.bot.setEntityId(packetIn.getEntityId());
        this.bot.setReducedDebug(packetIn.isReducedDebugInfo());
        this.controller.setGameType(packetIn.getGameType());

        sendPacket(new C15PacketClientSettings("en_US", 4, EntityPlayer.EnumChatVisibility.FULL, true, 0));
        this.network.sendPacket(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));

        this.world.setBot(this.bot);

        //TODO: notification for "Connected" here!

        Bot.bots.add(new Bot(this.network, this, this.controller, this.bot, this.world));
    }

    private void loadWorld(BotWorld botWorld) {
        this.world = botWorld;
        this.bot = new BotPlayer(this);
        this.controller.flipPlayer(this.bot);
        this.bot.preparePlayerToSpawn();
        this.world.spawnEntityInWorld(this.bot);
        this.controller.setPlayerCapabilities(this.bot);
        this.bot.movementInput = this.movementInput;
        this.world.setBot(this.bot);
    }

    @Override
    public void handleEntityMovement(S14PacketEntity packetIn) {
        Entity entity = packetIn.getEntity(this.world);
        if (entity != null) {
            entity.serverPosX += packetIn.func_149062_c();
            entity.serverPosY += packetIn.func_149061_d();
            entity.serverPosZ += packetIn.func_149064_e();

            double x = entity.serverPosX / 4096.0D;
            double y = entity.serverPosY / 4096.0D;
            double z = entity.serverPosZ / 4096.0D;

            float f = packetIn.func_149060_h() ? (float)(packetIn.func_149066_f() * 360) / 256.0F : entity.rotationYaw;
            float f1 = packetIn.func_149060_h() ? (float)(packetIn.func_149063_g() * 360) / 256.0F : entity.rotationPitch;

            entity.setPositionAndRotation2(x, y, z, f, f1, 3, false);
            entity.onGround = packetIn.getOnGround();
        }
    }

    @Override
    public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {
        double x = packetIn.getX();
        double y = packetIn.getY();
        double z = packetIn.getZ();

        float yaw = packetIn.getYaw();
        float pitch = packetIn.getPitch();

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X))
            x += bot.posX;
        else bot.motionX = 0;

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y))
            y += bot.posY;
        else bot.motionY = 0;

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z))
            z += bot.posZ;
        else bot.motionY = 0;

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
            pitch += bot.rotationPitch;

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
            yaw += bot.rotationYaw;

        bot.setPositionAndRotation(x, y, z, yaw, pitch);

        this.network.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(bot.posX, bot.getEntityBoundingBox().minY, bot.posZ, bot.rotationYaw, bot.rotationPitch, false));

        if (!this.doneLoadingTerrain)
        {
            this.bot.prevPosX = this.bot.posX;
            this.bot.prevPosY = this.bot.posY;
            this.bot.prevPosZ = this.bot.posZ;
            this.doneLoadingTerrain = true;
        }
    }

    @Override
    public void handleParticles(S2APacketParticles packetIn) { }

    @Override
    public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {
        bot.capabilities.isFlying = packetIn.isFlying();
        bot.capabilities.isCreativeMode = packetIn.isCreativeMode();
        bot.capabilities.disableDamage = packetIn.isInvulnerable();
        bot.capabilities.allowFlying = packetIn.isAllowFlying();
        bot.capabilities.setFlySpeed(packetIn.getFlySpeed());
        bot.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }

    @Override
    public void handlePlayerListItem(S38PacketPlayerListItem packetIn) { }

    @Override
    public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {
        for (int i = 0; i < packetIn.getEntityIDs().length; ++i)
        {
            this.world.removeEntityFromWorld(packetIn.getEntityIDs()[i]);
        }
    }

    @Override
    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());

        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase)entity).removePotionEffectClient(packetIn.getEffectId());
        }
    }

    @Override
    public void handleRespawn(S07PacketRespawn packetIn) {
        Bot.bots.removeIf(b -> b.getConnection().equals(this));
        int id = bot.getEntityId();
        String brand = this.bot.getServerBrand();

        if (packetIn.getDimensionID() != this.bot.dimension) {
            this.doneLoadingTerrain = false;
            this.world = new BotWorld(
                    this,
                    new WorldSettings(
                            0L,
                            packetIn.getGameType(),
                            false,
                            this.world.getWorldInfo().isHardcoreModeEnabled(),
                            packetIn.getWorldType()
                    ),
                    packetIn.getDimensionID(),
                    packetIn.getDifficulty()
            );
            loadWorld(this.world);
            this.bot.dimension = packetIn.getDimensionID();
        }

        setDimensionAndSpawnPlayer(packetIn.getDimensionID());
        this.controller.setGameType(packetIn.getGameType());
        this.bot.setEntityId(id);
        this.bot.setServerBrand(brand);
        this.world.setBot(bot);
        Bot.bots.add(new Bot(this.network, this, this.controller, this.bot, this.world));
    }

    @Override
    public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {
        Entity entity = packetIn.getEntity(this.world);

        if (entity != null)
        {
            float f = (float)(packetIn.getYaw() * 360) / 256.0F;
            entity.setRotationYawHead(f);
        }
    }

    @Override
    public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {
        if (packetIn.getHeldItemHotbarIndex() >= 0 && packetIn.getHeldItemHotbarIndex() < InventoryPlayer.getHotbarSize())
        {
            this.bot.inventory.currentItem = packetIn.getHeldItemHotbarIndex();
        }
    }

    @Override
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) { }

    @Override
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());

        if (entity != null && packetIn.func_149376_c() != null)
        {
            entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }

    @Override
    public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityID());

        if (entity != null)
        {
            entity.setVelocity((double)packetIn.getMotionX() / 8000.0D, (double)packetIn.getMotionY() / 8000.0D, (double)packetIn.getMotionZ() / 8000.0D);
        }
    }

    @Override
    public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityID());

        if (entity != null)
        {
            entity.setCurrentItemOrArmor(packetIn.getEquipmentSlot(), packetIn.getItemStack());
        }
    }

    @Override
    public void handleSetExperience(S1FPacketSetExperience packetIn) {
        this.bot.setXPStats(packetIn.func_149397_c(), packetIn.getTotalExperience(), packetIn.getLevel());
    }

    @Override
    public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {
        this.bot.setPlayerSPHealth(packetIn.getHealth());
        this.bot.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        this.bot.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    @Override
    public void handleTeams(S3EPacketTeams packetIn) { }

    @Override
    public void handleUpdateScore(S3CPacketUpdateScore packetIn) { }

    @Override
    public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {
        this.bot.setSpawnPoint(packetIn.getSpawnPos(), true);
        this.world.getWorldInfo().setSpawn(packetIn.getSpawnPos());
    }

    @Override
    public void handleTimeUpdate(S03PacketTimeUpdate packetIn) { }

    @Override
    public void handleUpdateSign(S33PacketUpdateSign packetIn) {
        if (this.world.isBlockLoaded(packetIn.getPos()))
        {
            TileEntity tileentity = this.world.getTileEntity(packetIn.getPos());

            if (tileentity instanceof TileEntitySign)
            {
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;

                if (tileentitysign.getIsEditable())
                {
                    System.arraycopy(packetIn.getLines(), 0, tileentitysign.signText, 0, 4);
                    tileentitysign.markDirty();
                }
            }
        }
    }

    @Override
    public void handleSoundEffect(S29PacketSoundEffect packetIn) { }

    @Override
    public void handleCollectItem(S0DPacketCollectItem packetIn) { }

    @Override
    public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());

        if (entity != null)
        {
            entity.serverPosX = packetIn.getX();
            entity.serverPosY = packetIn.getY();
            entity.serverPosZ = packetIn.getZ();
            double d0 = (double)entity.serverPosX / 32.0D;
            double d1 = (double)entity.serverPosY / 32.0D;
            double d2 = (double)entity.serverPosZ / 32.0D;
            float f = (float)(packetIn.getYaw() * 360) / 256.0F;
            float f1 = (float)(packetIn.getPitch() * 360) / 256.0F;

            if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D)
            {
                entity.setPositionAndRotation2(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
            }
            else
            {
                entity.setPositionAndRotation2(d0, d1, d2, f, f1, 3, true);
            }

            entity.onGround = packetIn.getOnGround();
        }
    }

    @Override
    public void handleEntityProperties(S20PacketEntityProperties packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());

        if (entity != null)
        {
            if (!(entity instanceof EntityLivingBase))
            {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            else
            {
                BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();

                for (S20PacketEntityProperties.Snapshot s20packetentityproperties$snapshot : packetIn.func_149441_d())
                {
                    IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(s20packetentityproperties$snapshot.func_151409_a());

                    if (iattributeinstance == null)
                    {
                        iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute((IAttribute)null, s20packetentityproperties$snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
                    }

                    iattributeinstance.setBaseValue(s20packetentityproperties$snapshot.func_151410_b());
                    iattributeinstance.removeAllModifiers();

                    for (AttributeModifier attributemodifier : s20packetentityproperties$snapshot.func_151408_c())
                    {
                        iattributeinstance.applyModifier(attributemodifier);
                    }
                }
            }
        }
    }

    @Override
    public void handleEntityEffect(S1DPacketEntityEffect packetIn) {
        Entity entity = this.world.getEntityByID(packetIn.getEntityId());

        if (entity instanceof EntityLivingBase)
        {
            PotionEffect potioneffect = new PotionEffect(packetIn.getEffectId(), packetIn.getDuration(), packetIn.getAmplifier(), false, packetIn.func_179707_f());
            potioneffect.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
        }
    }

    @Override
    public void handleCombatEvent(S42PacketCombatEvent packetIn) {
        if (packetIn.eventType == S42PacketCombatEvent.Event.ENTITY_DIED && this.world.getEntityByID(packetIn.field_179774_b) == this.bot)
            sendPacket(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
    }

    @Override
    public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {
        this.world.getWorldInfo().setDifficulty(packetIn.getDifficulty());
        this.world.getWorldInfo().setDifficultyLocked(packetIn.isDifficultyLocked());
    }

    @Override
    public void handleCamera(S43PacketCamera packetIn) { }

    @Override
    public void handleWorldBorder(S44PacketWorldBorder packetIn) {
        packetIn.func_179788_a(this.world.getWorldBorder());
    }

    @Override
    public void handleTitle(S45PacketTitle packetIn) { }

    @Override
    public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {
        if (!this.network.isLocalChannel())
        {
            this.network.setCompressionThreshold(packetIn.func_179760_a());
        }
    }

    @Override
    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) { }

    @Override
    public void handleResourcePack(S48PacketResourcePackSend packetIn) { }

    @Override
    public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {
        Entity entity = packetIn.getEntity(this.world);

        if (entity != null)
        {
            entity.clientUpdateEntityNBT(packetIn.getTagCompound());
        }
    }

    private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");

    private Bot getBot2() {
        Bot bot = null;
        for (Bot bot2 : Bot.bots) {
            if (!bot2.getPlayer().getDisplayName().getUnformattedTextForChat().equalsIgnoreCase(this.bot.getDisplayName().getUnformattedText()))
                continue;
            bot = bot2;
        }
        return bot;
    }

    private void setDimensionAndSpawnPlayer(int dimension) {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        this.world.removeEntity(this.bot);

        this.bot = new BotPlayer(this);
        this.bot.getDataWatcher().updateWatchedObjectsFromList(bot.getDataWatcher().getAllWatched());
        this.bot.dimension = dimension;
        this.bot.preparePlayerToSpawn();

        this.world.spawnEntityInWorld(this.bot);

        this.controller.flipPlayer(this.bot);

        try {
            this.bot.movementInput = this.movementInput;
        } catch (Exception ignored) { }

        this.controller.setPlayerCapabilities(this.bot);
        this.bot.setReducedDebug(this.bot.hasReducedDebug());
        this.world.setBot(bot);
    }

    public NetworkPlayerInfo getPlayerInfo(UUID uUID) {
        return this.playerInfoMap.get(uUID);
    }

    public static String stripColor(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
