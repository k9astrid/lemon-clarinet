package dev.lemon.api.utils.other;

import com.google.common.base.Predicates;
import dev.lemon.api.utils.IMethods;
import dev.lemon.api.utils.math.Vector2f;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.List;

@UtilityClass
public final class RayCastUtil implements IMethods {

    public MovingObjectPosition rayCast(final Vector2f rot, final double range, final float expand, Entity entity) {
        final float partialTicks = mc.timer.renderPartialTicks;

        MovingObjectPosition objectPosition;

        if (entity != null && mc.world != null) {
            objectPosition = entity.rayTraceCustom(range, rot.x, rot.y);
            double dist = range;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);

            if (objectPosition != null)
                dist = objectPosition.hitVec.distanceTo(vec3);

            final Vec3 vec31 = mc.player.getVectorForRotation(rot.y, rot.x);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);

            Entity pointed = null;
            Vec3 vec33 = null;
            final List<Entity> list = mc.world.getEntitiesInAABBexcluding(
                    entity,
                    entity.getEntityBoundingBox().addCoord(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range)
                            .expand(1.f, 1.f, 1.f),
                    Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith)
            );

            double dick = dist;

            for (final Entity e : list) {
                final float yes = e.getCollisionBorderSize() + expand;
                final AxisAlignedBB axisAlignedBB = e.getEntityBoundingBox().expand(yes, yes, yes);
                final MovingObjectPosition object = axisAlignedBB.calculateIntercept(vec3, vec32);

                if (axisAlignedBB.isVecInside(vec3)) {
                    if (dick >= 0) {
                        pointed = e;
                        vec33 = object == null ? vec3 : object.hitVec;
                        dick = 0;
                    }
                } else if (object != null) {
                    final double distance = vec3.distanceTo(object.hitVec);

                    if (dick < dick || dick == 0) {
                        pointed = e;
                        vec33 = object.hitVec;
                        dick = distance;
                    }
                }
            }

            if (pointed != null && (dick < dist || objectPosition == null))
                objectPosition = new MovingObjectPosition(pointed, vec33);

            return objectPosition;
        }

        return null;
    }
}
