package dev.lemon.api.bot.network;

import com.mojang.authlib.GameProfile;
import com.viaversion.viaversion.protocols.protocol1_11to1_10.storage.EntityTracker1_11;
import dev.lemon.api.bot.Bot;
import dev.lemon.api.bot.entity.BotPlayer;
import dev.lemon.api.bot.proxy.Proxy;
import dev.lemon.api.bot.world.BotWorld;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.entity.*;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.*;
import net.minecraft.util.IChatComponent;
import viamcp.ViaMCP;

import java.net.InetAddress;
import java.util.List;
import java.util.UUID;

@Getter
public class BotPlayClient implements INetHandlerPlayClient {

    private BotNetwork network;

    private BotPlayer bot;

    private final GameProfile profile;

    private BotWorld world;

    public BotPlayClient(BotNetwork network, GameProfile gameProfile) {
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
    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn) {

    }

    @Override
    public void handleSpawnPainting(S10PacketSpawnPainting packetIn) {

    }

    @Override
    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn) {

    }

    @Override
    public void handleAnimation(S0BPacketAnimation packetIn) {

    }

    @Override
    public void handleStatistics(S37PacketStatistics packetIn) {

    }

    @Override
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn) {

    }

    @Override
    public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn) {

    }

    @Override
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn) {

    }

    @Override
    public void handleBlockAction(S24PacketBlockAction packetIn) {

    }

    @Override
    public void handleBlockChange(S23PacketBlockChange packetIn) {

    }

    @Override
    public void handleChat(S02PacketChat packetIn) {

    }

    @Override
    public void handleTabComplete(S3APacketTabComplete packetIn) {

    }

    @Override
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn) {

    }

    @Override
    public void handleMaps(S34PacketMaps packetIn) {

    }

    @Override
    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn) {

    }

    @Override
    public void handleCloseWindow(S2EPacketCloseWindow packetIn) {

    }

    @Override
    public void handleWindowItems(S30PacketWindowItems packetIn) {

    }

    @Override
    public void handleOpenWindow(S2DPacketOpenWindow packetIn) {

    }

    @Override
    public void handleWindowProperty(S31PacketWindowProperty packetIn) {

    }

    @Override
    public void handleSetSlot(S2FPacketSetSlot packetIn) {

    }

    @Override
    public void handleCustomPayload(S3FPacketCustomPayload packetIn) {

    }

    @Override
    public void handleDisconnect(S40PacketDisconnect packetIn) {

    }

    @Override
    public void handleUseBed(S0APacketUseBed packetIn) {

    }

    @Override
    public void handleEntityStatus(S19PacketEntityStatus packetIn) {

    }

    @Override
    public void handleEntityAttach(S1BPacketEntityAttach packetIn) {

    }

    @Override
    public void handleExplosion(S27PacketExplosion packetIn) {

    }

    @Override
    public void handleChangeGameState(S2BPacketChangeGameState packetIn) {

    }

    @Override
    public void handleKeepAlive(S00PacketKeepAlive packetIn) {

    }

    @Override
    public void handleChunkData(S21PacketChunkData packetIn) {

    }

    @Override
    public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn) {

    }

    @Override
    public void handleEffect(S28PacketEffect packetIn) {

    }

    @Override
    public void handleJoinGame(S01PacketJoinGame packetIn) {

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

    private Bot getBot2() {
        Bot bot = null;
        for (Bot bot2 : Bot.bots) {
            if (!bot2.getPlayer().getDisplayName().getUnformattedTextForChat().equalsIgnoreCase(this.bot.getDisplayName().getUnformattedText()))
                continue;
            bot = bot2;
        }
        return bot;
    }
}
