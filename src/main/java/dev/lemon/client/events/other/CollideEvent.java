package dev.lemon.client.events.other;

import dev.lemon.api.event.CancellableEvent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class CollideEvent extends CancellableEvent {

    @Getter
    @Setter
    private AxisAlignedBB boundingBox;

    @Getter
    private BlockPos pos;

    @Getter
    private Entity collidingEntity;

    public CollideEvent(AxisAlignedBB boundingBox, BlockPos pos, Entity collidingEntity){
        this.boundingBox = boundingBox;
        this.pos = pos;
        this.collidingEntity = collidingEntity;
    }
}
