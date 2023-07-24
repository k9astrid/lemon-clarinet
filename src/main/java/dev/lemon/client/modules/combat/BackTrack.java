package dev.lemon.client.modules.combat;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.BooleanSetting;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.utils.math.TimerUtil;
import dev.lemon.api.utils.player.ChatUtil;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.other.PacketEvent;
import dev.lemon.client.events.other.TickEvent;
import dev.lemon.client.events.render.Render3DEvent;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.network.play.server.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BackTrack extends Module {

    public NumberSetting preaimRange = new NumberSetting("Pre Aim Range", 4, 0, 15, .1);
    public NumberSetting hitRange = new NumberSetting("Hit Range", 6, 3, 6, .1);
    public NumberSetting timerDelay = new NumberSetting("Timer Delay", 4000, 0, 30000, 100);
    public BooleanSetting reveal = new BooleanSetting("Reveal position", true);
    public BooleanSetting onlyWhenNeed = new BooleanSetting("Only when needed", true);
    public BooleanSetting onlyAura = new BooleanSetting("Only kill aura", true);
    public BooleanSetting delayVelocity = new BooleanSetting("Delay Velocity", true);
    public BooleanSetting delayExplosionVelocity = new BooleanSetting("Delay Ex Velocity", true);
    public BooleanSetting delayKeepAlive = new BooleanSetting("Delay Keep Alive", true);

    private EntityLivingBase entity = null;

    private INetHandler packetListener = null;

    private WorldClient lastWorld;

    private final ArrayList<Packet> packets = new ArrayList<>();

    private final TimerUtil timerUtil = new TimerUtil();

    public BackTrack() {
        super("Back Track", Category.COMBAT);
    }

    @Override
    protected void onEnable() {
        this.block = false;
        if (mc.world != null && mc.player != null) {
            for (Entity e : mc.world.loadedEntityList) {
                if (e instanceof EntityLivingBase) {
                    ((EntityLivingBase) e).realPosX = (int) e.serverPosX;
                    ((EntityLivingBase) e).realPosY = (int) e.serverPosY;
                    ((EntityLivingBase) e).realPosZ = (int) e.serverPosZ;
                }
            }
        }
    }

    @Subscribe
    public final IEventListener<PacketEvent> onPacket = e -> {
        if (e.getINetHandler() != null)
            this.packetListener = e.getINetHandler();

        if (e.getType() == PacketEvent.Type.RECEIVE) {
            if (e.getDirection() != EnumPacketDirection.CLIENTBOUND)
                return;

            if (e.getPacket() instanceof S08PacketPlayerPosLook)
                resetPackets(e.getINetHandler());

            if (e.getPacket() instanceof S14PacketEntity) {
                Entity entity1 = ((S14PacketEntity) e.getPacket()).getEntity(mc.world);

                if (entity1 instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity1;
                    entityLivingBase.realPosX = ((S14PacketEntity) e.getPacket()).func_149062_c();
                    entityLivingBase.realPosY = ((S14PacketEntity) e.getPacket()).func_149061_d();
                    entityLivingBase.realPosZ = ((S14PacketEntity) e.getPacket()).func_149064_e();
                }
            }

            if (e.getPacket() instanceof S18PacketEntityTeleport) {
                Entity entity1 = mc.world.getEntityByID(((S18PacketEntityTeleport) e.getPacket()).getEntityId());

                if (entity1 instanceof EntityLivingBase) {
                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity1;
                    entityLivingBase.realPosX = ((S18PacketEntityTeleport) e.getPacket()).getX();
                    entityLivingBase.realPosY = ((S18PacketEntityTeleport) e.getPacket()).getY();
                    entityLivingBase.realPosZ = ((S18PacketEntityTeleport) e.getPacket()).getZ();
                }
            }

            if (entity == null) {
                resetPackets(e.getINetHandler());
                return;
            }

             if (mc.world != null && mc.player != null) {
                if (this.lastWorld != mc.world) {
                    resetPackets(e.getINetHandler());
                    this.lastWorld = mc.world;
                    return;
                }
                addPackets(e.getPacket(), e);
             }
             this.lastWorld = mc.world;
        }
    };

    private boolean block, cock;

    @Subscribe
    public final IEventListener<Render3DEvent> onRedner3D = e -> {
        if (reveal.isToggled()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GlStateManager.disableCull();
            GL11.glDepthMask(false);
            if (this.entity != null && this.block) {
                float lineWidth = 3.0F;
                if (mc.player.getDistanceToEntity((Entity)entity) > 1.0F) {
                    double d0 = (1.0F - mc.player.getDistanceToEntity((Entity)entity) / 20.0F);
                    if (d0 < 0.3D)
                        d0 = 0.3D;
                    lineWidth = (float)(lineWidth * d0);
                }

                RenderUtil.drawEntityServerESP(entity, 0, 1, 1, 0.03137255f, 1.0f, lineWidth);
            }
            GL11.glDepthMask(true);
            GlStateManager.enableCull();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
        }
    };

    @Subscribe
    public final IEventListener<TickEvent> onTick = e -> {
        if (mc.player == null) {
            toggle();
            return;
        }

        if (Lemon.INSTANCE.getModuleManager().getModuleByName("Kill Aura").isToggled())
            entity = (EntityLivingBase) KillAura.target;
        else {
            List<Entity> list = mc.world.loadedEntityList
                    .stream()
                    .filter(this::isValid)
                    .sorted(Comparator.comparingDouble(en -> mc.player.getDistanceToEntity(en)))
                    .collect(Collectors.toList());

            if (!list.isEmpty())
                this.entity = (EntityLivingBase) list.get(0);

            if (onlyAura.isToggled())
                this.entity = null;
        }

        if (this.entity != null && mc.player != null && this.packetListener != null && mc.world != null) {
            double d0 = this.entity.realPosX / 32.0D;
            double d1 = this.entity.realPosY / 32.0D;
            double d2 = this.entity.realPosZ / 32.0D;
            double serverX = this.entity.serverPosX / 32.0D;
            double serverY = this.entity.serverPosY / 32.0D;
            double serverZ = this.entity.serverPosZ / 32.0D;
            float f = this.entity.width / 2.0F;
            AxisAlignedBB entityServerPos = new AxisAlignedBB(serverX - f, serverY, serverZ - f, serverX + f, serverY + this.entity.height, serverZ + f);
            Vec3 positionEyes = mc.player.getPositionEyes(mc.timer.renderPartialTicks);
            double currentX = MathHelper.clamp_double(positionEyes.xCoord, entityServerPos.minX, entityServerPos.maxX);
            double currentY = MathHelper.clamp_double(positionEyes.yCoord, entityServerPos.minY, entityServerPos.maxY);
            double currentZ = MathHelper.clamp_double(positionEyes.zCoord, entityServerPos.minZ, entityServerPos.maxZ);
            AxisAlignedBB entityPosMe = new AxisAlignedBB(d0 - f, d1, d2 - f, d0 + f, d1 + this.entity.height, d2 + f);
            double realX = MathHelper.clamp_double(positionEyes.xCoord, entityPosMe.minX, entityPosMe.maxX);
            double realY = MathHelper.clamp_double(positionEyes.yCoord, entityPosMe.minY, entityPosMe.maxY);
            double realZ = MathHelper.clamp_double(positionEyes.zCoord, entityPosMe.minZ, entityPosMe.maxZ);
            double distance = this.hitRange.getVal();
            if (!mc.player.canEntityBeSeen((Entity)this.entity))
                distance = Math.min(distance, 3.0D);
            double collision = this.entity.getCollisionBorderSize();
            double width = (mc.player.width / 2.0F);
            double mePosXForPlayer = (mc.player.getLastServerPosition()).xCoord + ((mc.player.getServerPosition()).xCoord - (mc.player.getLastServerPosition()).xCoord) / MathHelper.clamp_int(mc.player.rotIncrement, 1, 3);
            double mePosYForPlayer = (mc.player.getLastServerPosition()).yCoord + ((mc.player.getServerPosition()).yCoord - (mc.player.getLastServerPosition()).yCoord) / MathHelper.clamp_int(mc.player.rotIncrement, 1, 3);
            double mePosZForPlayer = (mc.player.getLastServerPosition()).zCoord + ((mc.player.getServerPosition()).zCoord - (mc.player.getLastServerPosition()).zCoord) / MathHelper.clamp_int(mc.player.rotIncrement, 1, 3);
            AxisAlignedBB mePosForPlayerBox = new AxisAlignedBB(mePosXForPlayer - width, mePosYForPlayer, mePosZForPlayer - width, mePosXForPlayer + width, mePosYForPlayer + mc.player.height, mePosZForPlayer + width);
            mePosForPlayerBox = mePosForPlayerBox.expand(collision, collision, collision);
            Vec3 entityPosEyes = new Vec3(serverX, serverY + this.entity.getEyeHeight(), serverZ);
            double bestX = MathHelper.clamp_double(entityPosEyes.xCoord, mePosForPlayerBox.minX, mePosForPlayerBox.maxX);
            double bestY = MathHelper.clamp_double(entityPosEyes.yCoord, mePosForPlayerBox.minY, mePosForPlayerBox.maxY);
            double bestZ = MathHelper.clamp_double(entityPosEyes.zCoord, mePosForPlayerBox.minZ, mePosForPlayerBox.maxZ);
            boolean sexy = false;

            if (entityPosEyes.distanceTo(new Vec3(bestX, bestY, bestZ)) > 3.0D || (mc.player.hurtTime < 8 && mc.player.hurtTime > 3))
                sexy = true;
            if (!this.onlyWhenNeed.isToggled())
                sexy = true;

            if (sexy && positionEyes.distanceTo(new Vec3(realX, realY, realZ)) > positionEyes.distanceTo(new Vec3(currentX, currentY, currentZ))
                    && mc.player.getServerPosition().distanceTo(new Vec3(d0, d1, d2)) < distance && !this.timerUtil.hasTimeElapsed((long)this.timerDelay.getVal())) {
                this.block = true;
                ChatUtil.addMessage("block");
            } else {
                this.block = false;
                resetPackets(this.packetListener);
                this.timerUtil.reset();
                ChatUtil.addMessage("bye");
            }
        }
    };

    private boolean isValid(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            if (entity.isInvisible())
                return false;

            if (((EntityLivingBase) entity).deathTime > 1)
                return false;

            if (!(entity instanceof EntityPlayer))
                return false;

            if (entity.ticksExisted < 50)
                return false;

            if (Lemon.INSTANCE.getBotManager().contains(entity))
                return false;

            if (entity.getName().equals("§aShop") || entity.getName().equals("SHOP") || entity.getName().equals("UPGRADES"))
                return false;

            if (entity.isDead)
                return false;
        }

        return (entity instanceof EntityLivingBase && entity != mc.player && mc.player.getDistanceToEntity(entity) < this.preaimRange.getVal());
    }

    private void addPackets(Packet packet, PacketEvent packetEvent) {
        synchronized (this.packets) {
            if (delayPackets(packet)) {
                this.packets.add(packet);
                packetEvent.setCancelled(true);
            }
        }
    }

    private boolean delayPackets(Packet packet) {
        if (mc.currentScreen != null)
            return false;

        if (packet instanceof S00PacketKeepAlive)
            return this.delayKeepAlive.isToggled();

        if (packet instanceof S12PacketEntityVelocity)
            return this.delayVelocity.isToggled();

        if (packet instanceof S27PacketExplosion)
            return this.delayExplosionVelocity.isToggled();

        if (packet instanceof S19PacketEntityStatus)
            return (((S19PacketEntityStatus) packet).getOpCode() != 2 || !(mc.world.getEntityByID(((S19PacketEntityStatus) packet).getEntityId()) instanceof EntityLivingBase));

        return (!(packet instanceof S06PacketUpdateHealth) && !(packet instanceof S29PacketSoundEffect) && !(packet instanceof S3EPacketTeams) && !(packet instanceof S0CPacketSpawnPlayer));
    }

    private void resetPackets(INetHandler netHandler) {
        if (this.packets.size() > 0)
            while (this.packets.size() != 0) {
                Packet packet = this.packets.get(0);

                try {
                    if (packet != null)
                        packet.processPacket(netHandler);
                } catch (ThreadQuickExitException ignored) {}

                this.packets.remove(this.packets.get(0));
            }
    }
}
