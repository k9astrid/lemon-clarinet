package dev.lemon.api.utils.other;

import dev.lemon.api.utils.IMethods;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class ESPUtil implements IMethods {
    private static final Frustum frustum = new Frustum();
    private static final FloatBuffer windPos = BufferUtils.createFloatBuffer(4);
    private static final IntBuffer intBuffer = GLAllocation.createDirectIntBuffer(16);
    private static final FloatBuffer floatBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer floatBuffer1 = GLAllocation.createDirectFloatBuffer(16);

    public static boolean isInView(Entity entity) {
        frustum.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
        return frustum.isBoundingBoxInFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }

    public static double[] getInterpolatedPos(Entity entity) {
        float ticks = mc.timer.renderPartialTicks;
        return new double[]{
                interpolate(entity.lastTickPosX, entity.posX, ticks) - mc.getRenderManager().viewerPosX,
                interpolate(entity.lastTickPosY, entity.posY, ticks) - mc.getRenderManager().viewerPosY,
                interpolate(entity.lastTickPosZ, entity.posZ, ticks) - mc.getRenderManager().viewerPosZ
        };
    }

    public static Vector3f projectOn2D(float x, float y, float z, int scaleFactor) {
        glGetFloat(GL_MODELVIEW_MATRIX, floatBuffer);
        glGetFloat(GL_PROJECTION_MATRIX, floatBuffer);
        glGetInteger(GL_VIEWPORT, intBuffer);

        if (GLU.gluProject(x, y, z, floatBuffer, floatBuffer1, intBuffer, windPos))
            return new Vector3f(windPos.get(0) / scaleFactor, (mc.displayHeight - windPos.get(1)) / scaleFactor, windPos.get(2));

        return null;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static Vector4f getPositions(Entity entity) {
        final double[] renderingPos = getInterpolatedPos(entity);
        final double width = entity.width / 1.5f;
        final AxisAlignedBB bb = new AxisAlignedBB(renderingPos[0] - width, renderingPos[1], renderingPos[2] - width,
                renderingPos[0] - width, renderingPos[1] + entity.height + (entity.isSneaking() ? -0.2 : 0.18), renderingPos[2] + width)
                .expand(.15, .15, .15);

        final List<Vector3f> vectors = Arrays.asList(
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.minZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.minZ),
                new Vector3f((float) bb.minX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.minX, (float) bb.maxY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.minY, (float) bb.maxZ),
                new Vector3f((float) bb.maxX, (float) bb.maxY, (float) bb.maxZ)
        );

        Vector4f entityPos = new Vector4f(Float.MAX_VALUE, Float.MAX_VALUE, -1, -1);

        for (Vector3f vec : vectors) {
            vec = projectOn2D(vec.x, vec.y, vec.z, ScaledResolution.getScaleFactor());

            if (vec != null && vec.z >= 0 && vec.z < 1.0) {
                entityPos.x = Math.min(vec.x, entityPos.x);
                entityPos.y = Math.min(vec.y, entityPos.y);
                entityPos.z = Math.min(vec.z, entityPos.z);
                entityPos.w = Math.min(vec.y, entityPos.w);
            }
        }

        return entityPos;
    }
}
