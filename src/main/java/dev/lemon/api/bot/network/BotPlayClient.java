package dev.lemon.api.bot.network;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.storage.EntityTracker1_11;
import dev.lemon.api.bot.Bot;
import dev.lemon.api.bot.BotStarter;
import dev.lemon.api.bot.entity.BotController;
import dev.lemon.api.bot.entity.BotPlayer;
import dev.lemon.api.bot.proxy.Proxy;
import dev.lemon.api.bot.world.BotWorld;
import io.netty.buffer.Unpooled;
import lombok.Data;
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
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraft.tileentity.*;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
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

    private BotNetwork network;

    private BotController controller;

    private BotPlayer bot;

    private final GameProfile profile;

    public BotWorld world;

    private final Map<UUID, NetworkPlayerInfo> playerInfoMap;

    public final MovementInput movementInput;

    public boolean jump, sneak, forward, backward, left, right;

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
            EntityTracker.updateServerPosition(entityLivingBase, x, y, z);

            entityLivingBase.renderYawOffset = (packetIn.getHeadPitch() * 360) / 256.f;
            entityLivingBase.rotationYawHead = (packetIn.getHeadPitch() * 360) / 256.f;

            Entity[] array = entityLivingBase.getParts();
            if (array != null) {
                int id = packetIn.getEntityID() - entityLivingBase.getEntityId();

                for (Entity entity : array)
                    entity.setEntityId(entity.getEntityId() + id);
            }

            entityLivingBase.setEntityId(packetIn.getEntityID()); // should work without entity unique ID

            entityLivingBase.setPositionAndRotation(x, y, z, f, f2);

            entityLivingBase.motionX = (packetIn.getVelocityX() / 8000.f);
            entityLivingBase.motionY = (packetIn.getVelocityY() / 8000.f);
            entityLivingBase.motionZ = (packetIn.getVelocityZ() / 8000.f);

            this.world.addEntityToWorld(packetIn.getEntityID(), entityLivingBase);

            //should work without data manager...
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

        EntityTracker.updateServerPosition(entityOtherPlayerMP, x, y, z);

        entityOtherPlayerMP.setPositionAndRotation(x, y, z, f, f2);

        this.world.addEntityToWorld(packetIn.getEntityID(), entityOtherPlayerMP);

        //That should work without data manager...
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

            boolean cock = (type == 2 && tileEntity instanceof TileEntityCommandBlock), cock2 = cock;

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
        //TODO: make autoregister
    }

    @Override
    public void handleTabComplete(S3APacketTabComplete packetIn) { }

    @Override
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {
        for (S22PacketMultiBlockChange.BlockUpdateData blockUpdateData : packetIn.getChangedBlocks())
            this.world.invalidateRegionAndSetBlock(blockUpdateData.getPos(), blockUpdateData.getBlockState());
    }

    @Override
    public void handleMaps(S34PacketMaps packetIn) {
        //TODO: handleMaps...
    }

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
            if (entity instanceof EntityHorse) // Should work on 1.12.2 its AbstractHorse
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
        BotPlayer botPlayer = this.bot;
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

    }

    @Override
    public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn) {

    }

    @Override
    public void handleParticles(S2APacketParticles packetIn) {

    }

    @Override
    public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn) {

    }

    @Override
    public void handlePlayerListItem(S38PacketPlayerListItem packetIn) {

    }

    @Override
    public void handleDestroyEntities(S13PacketDestroyEntities packetIn) {

    }

    @Override
    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn) {

    }

    @Override
    public void handleRespawn(S07PacketRespawn packetIn) {

    }

    @Override
    public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn) {

    }

    @Override
    public void handleHeldItemChange(S09PacketHeldItemChange packetIn) {

    }

    @Override
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn) {

    }

    @Override
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn) {

    }

    @Override
    public void handleEntityVelocity(S12PacketEntityVelocity packetIn) {

    }

    @Override
    public void handleEntityEquipment(S04PacketEntityEquipment packetIn) {

    }

    @Override
    public void handleSetExperience(S1FPacketSetExperience packetIn) {

    }

    @Override
    public void handleUpdateHealth(S06PacketUpdateHealth packetIn) {

    }

    @Override
    public void handleTeams(S3EPacketTeams packetIn) {

    }

    @Override
    public void handleUpdateScore(S3CPacketUpdateScore packetIn) {

    }

    @Override
    public void handleSpawnPosition(S05PacketSpawnPosition packetIn) {

    }

    @Override
    public void handleTimeUpdate(S03PacketTimeUpdate packetIn) {

    }

    @Override
    public void handleUpdateSign(S33PacketUpdateSign packetIn) {

    }

    @Override
    public void handleSoundEffect(S29PacketSoundEffect packetIn) {

    }

    @Override
    public void handleCollectItem(S0DPacketCollectItem packetIn) {

    }

    @Override
    public void handleEntityTeleport(S18PacketEntityTeleport packetIn) {

    }

    @Override
    public void handleEntityProperties(S20PacketEntityProperties packetIn) {

    }

    @Override
    public void handleEntityEffect(S1DPacketEntityEffect packetIn) {

    }

    @Override
    public void handleCombatEvent(S42PacketCombatEvent packetIn) {

    }

    @Override
    public void handleServerDifficulty(S41PacketServerDifficulty packetIn) {

    }

    @Override
    public void handleCamera(S43PacketCamera packetIn) {

    }

    @Override
    public void handleWorldBorder(S44PacketWorldBorder packetIn) {

    }

    @Override
    public void handleTitle(S45PacketTitle packetIn) {

    }

    @Override
    public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn) {

    }

    @Override
    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn) {

    }

    @Override
    public void handleResourcePack(S48PacketResourcePackSend packetIn) {

    }

    @Override
    public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn) {

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

    public NetworkPlayerInfo getPlayerInfo(UUID uUID) {
        return this.playerInfoMap.get(uUID);
    }

    public static String stripColor(String input) {
        return COLOR_PATTERN.matcher(input).replaceAll("");
    }
}
