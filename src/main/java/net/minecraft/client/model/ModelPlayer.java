package net.minecraft.client.model;

import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.main.Lemon;
import dev.lemon.client.modules.render.CustomModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModelPlayer extends ModelBiped
{
    private final ModelRenderer body;
    private final ModelRenderer eye;
    private final ModelRenderer left_leg;
    private final ModelRenderer right_leg;

    private final ModelRenderer rabbitBone;
    private final ModelRenderer rabbitRleg;
    private final ModelRenderer rabbitLarm;
    private final ModelRenderer rabbitRarm;
    private final ModelRenderer rabbitLleg;
    private final ModelRenderer rabbitHead;

    ModelRenderer head;
    ModelRenderer nose;
    ModelRenderer ear1;
    ModelRenderer ear2;
    ModelRenderer bodyfront;
    ModelRenderer bodyback;
    ModelRenderer leg1;
    ModelRenderer foot1;
    ModelRenderer leg2;
    ModelRenderer foot2;
    ModelRenderer leg3;
    ModelRenderer foot3;
    ModelRenderer leg4;
    ModelRenderer foot4;
    ModelRenderer tail;

    public ModelRenderer bipedLeftArmwear;
    public ModelRenderer bipedRightArmwear;
    public ModelRenderer bipedLeftLegwear;
    public ModelRenderer bipedRightLegwear;
    public ModelRenderer bipedBodyWear;
    private ModelRenderer bipedCape;
    private ModelRenderer bipedDeadmau5Head;
    private boolean smallArms;
    private static final String __OBFID = "CL_00002626";

    public ModelPlayer(float p_i46304_1_, boolean p_i46304_2_)
    {
        super(p_i46304_1_, 0.0F, 64, 64);
        this.smallArms = p_i46304_2_;
        this.bipedDeadmau5Head = new ModelRenderer(this, 24, 0);
        this.bipedDeadmau5Head.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1, p_i46304_1_);
        this.bipedCape = new ModelRenderer(this, 0, 0);
        this.bipedCape.setTextureSize(64, 32);
        this.bipedCape.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1, p_i46304_1_);

        if (p_i46304_2_)
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArm = new ModelRenderer(this, 40, 16);
            this.bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_);
            this.bipedRightArm.setRotationPoint(-5.0F, 2.5F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.5F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.5F, 10.0F);
        }
        else
        {
            this.bipedLeftArm = new ModelRenderer(this, 32, 48);
            this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_);
            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedLeftArmwear = new ModelRenderer(this, 48, 48);
            this.bipedLeftArmwear.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedLeftArmwear.setRotationPoint(5.0F, 2.0F, 0.0F);
            this.bipedRightArmwear = new ModelRenderer(this, 40, 32);
            this.bipedRightArmwear.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
            this.bipedRightArmwear.setRotationPoint(-5.0F, 2.0F, 10.0F);
        }


        this.body = new ModelRenderer(this);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.setTextureOffset(34, 8).addBox(-4.0F, 6.0F, -3.0F, 8, 12, 6);
        this.body.setTextureOffset(15, 10).addBox(-3.0F, 9.0F, 3.0F, 6, 8, 3);
        this.body.setTextureOffset(26, 0).addBox(-3.0F, 5.0F, -3.0F, 6, 1, 6);
        this.eye = new ModelRenderer(this);
        this.eye.setTextureOffset(0, 10).addBox(-3.0F, 7.0F, -4.0F, 6, 4, 1);
        this.left_leg = new ModelRenderer(this);
        this.left_leg.setRotationPoint(-2.0F, 18.0F, 0.0F);
        this.left_leg.setTextureOffset(0, 0).addBox(2.9F, 0.0F, -1.5F, 3, 6, 3, 0.0F);
        this.right_leg = new ModelRenderer(this);
        this.right_leg.setRotationPoint(2.0F, 18.0F, 0.0F);
        this.right_leg.setTextureOffset(13, 0).addBox(-5.9F, 0.0F, -1.5F, 3, 6, 3);

        (this.head = new ModelRenderer(this, 29, 5)).addBox(-4.0F, -4.0F, -6.0F, 8, 7, 6);
        this.head.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.head.setTextureSize(64, 64);
        this.head.mirror = true;
        this.setRotationAngle(this.head, 0.0F, 0.0F, 0.0F);
        (this.nose = new ModelRenderer(this, 45, 20)).addBox(-2.0F, -0.5F, -7.5F, 4, 3, 2);
        this.nose.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.nose.setTextureSize(64, 64);
        this.nose.mirror = true;
        this.setRotationAngle(this.nose, 0.0F, 0.0F, 0.0F);
        (this.ear1 = new ModelRenderer(this, 45, 27)).addBox(1.5F, -6.0F, -4.0F, 4, 4, 2);
        this.ear1.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.ear1.setTextureSize(64, 64);
        this.ear1.mirror = true;
        this.setRotationAngle(this.ear1, 0.0F, -0.1745329F, 0.0F);
        (this.ear2 = new ModelRenderer(this, 45, 34)).addBox(-5.5F, -6.0F, -4.0F, 4, 4, 2);
        this.ear2.setRotationPoint(0.0F, 14.0F, -5.0F);
        this.ear2.setTextureSize(64, 64);
        this.ear2.mirror = true;
        this.setRotationAngle(this.ear2, 0.0F, 0.1745329F, 0.0F);
        (this.bodyfront = new ModelRenderer(this, 2, 45)).addBox(0.0F, 0.0F, 0.0F, 9, 8, 9);
        this.bodyfront.setRotationPoint(-4.5F, 11.0F, -6.0F);
        this.bodyfront.setTextureSize(64, 64);
        this.bodyfront.mirror = true;
        this.setRotationAngle(this.bodyfront, 0.0872665F, 0.0F, 0.0F);
        (this.bodyback = new ModelRenderer(this, 2, 26)).addBox(0.0F, 0.0F, 0.0F, 10, 8, 10);
        this.bodyback.setRotationPoint(-5.0F, 10.0F, 3.0F);
        this.bodyback.setTextureSize(64, 64);
        this.bodyback.mirror = true;
        this.setRotationAngle(this.bodyback, -0.0872665F, 0.0F, 0.0F);
        (this.leg1 = new ModelRenderer(this, 44, 50)).addBox(0.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg1.setRotationPoint(1.0F, 16.0F, -5.0F);
        this.leg1.setTextureSize(64, 64);
        this.leg1.mirror = true;
        this.setRotationAngle(this.leg1, 0.0F, 0.0F, 0.0F);
        (this.foot1 = new ModelRenderer(this, 47, 43)).addBox(0.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot1.setRotationPoint(1.0F, 16.0F, -5.0F);
        this.foot1.setTextureSize(64, 64);
        this.foot1.mirror = true;
        this.setRotationAngle(this.foot1, 0.0F, 0.0F, 0.0F);
        (this.leg2 = new ModelRenderer(this, 44, 50)).addBox(-4.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg2.setRotationPoint(-1.0F, 16.0F, -5.0F);
        this.leg2.setTextureSize(64, 64);
        this.leg2.mirror = true;
        this.setRotationAngle(this.leg2, 0.0F, 0.0F, 0.0F);
        (this.foot2 = new ModelRenderer(this, 47, 43)).addBox(-4.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot2.setRotationPoint(-1.0F, 16.0F, -5.0F);
        this.foot2.setTextureSize(64, 64);
        this.foot2.mirror = true;
        this.setRotationAngle(this.foot2, 0.0F, 0.0F, 0.0F);
        (this.leg3 = new ModelRenderer(this, 44, 50)).addBox(0.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg3.setRotationPoint(1.5F, 16.0F, 9.0F);
        this.leg3.setTextureSize(64, 64);
        this.leg3.mirror = true;
        this.setRotationAngle(this.leg3, 0.0F, 0.0F, 0.0F);
        (this.foot3 = new ModelRenderer(this, 47, 43)).addBox(0.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot3.setRotationPoint(1.5F, 16.0F, 9.0F);
        this.foot3.setTextureSize(64, 64);
        this.foot3.mirror = true;
        this.setRotationAngle(this.foot3, 0.0F, 0.0F, 0.0F);
        (this.leg4 = new ModelRenderer(this, 44, 50)).addBox(-4.0F, 0.0F, -2.0F, 4, 8, 4);
        this.leg4.setRotationPoint(-1.5F, 16.0F, 9.0F);
        this.leg4.setTextureSize(64, 64);
        this.leg4.mirror = true;
        this.setRotationAngle(this.leg4, 0.0F, 0.0F, 0.0F);
        (this.foot4 = new ModelRenderer(this, 47, 43)).addBox(-4.0F, 6.0F, -3.0F, 4, 2, 1);
        this.foot4.setRotationPoint(-1.5F, 16.0F, 9.0F);
        this.foot4.setTextureSize(64, 64);
        this.foot4.mirror = true;
        this.setRotationAngle(this.foot4, 0.0F, 0.0F, 0.0F);
        (this.tail = new ModelRenderer(this, 2, 3)).addBox(-2.0F, -2.0F, 0.0F, 4, 5, 17);
        this.tail.setRotationPoint(0.0F, 14.0F, 11.0F);
        this.tail.setTextureSize(64, 64);
        this.tail.mirror = true;
        this.setRotationAngle(this.tail, -0.1745329F, 0.0F, 0.0F);

        this.textureWidth = 64;
        this.textureHeight = 64;

        (this.rabbitBone = new ModelRenderer(this)).setRotationPoint(0.0F, 24.0F, 0.0F);
        this.rabbitBone.cubeList.add(new ModelBox(this.rabbitBone, 28, 45, -5.0F, -13.0F, -5.0F, 10, 11, 8, 0.0F, false));
        (this.rabbitRleg = new ModelRenderer(this)).setRotationPoint(-3.0F, -2.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitRleg);
        this.rabbitRleg.cubeList.add(new ModelBox(this.rabbitRleg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F, false));
        (this.rabbitLarm = new ModelRenderer(this)).setRotationPoint(5.0F, -13.0F, -1.0F);
        this.setRotationAngle(this.rabbitLarm, 0.0F, 0.0F, -0.0873F);
        this.rabbitBone.addChild(this.rabbitLarm);
        this.rabbitLarm.cubeList.add(new ModelBox(this.rabbitLarm, 0, 0, 0.0F, 0.0F, -2.0F, 2, 8, 4, 0.0F, false));
        (this.rabbitRarm = new ModelRenderer(this)).setRotationPoint(-5.0F, -13.0F, -1.0F);
        this.setRotationAngle(this.rabbitRarm, 0.0F, 0.0F, 0.0873F);
        this.rabbitBone.addChild(this.rabbitRarm);
        this.rabbitRarm.cubeList.add(new ModelBox(this.rabbitRarm, 0, 0, -2.0F, 0.0F, -2.0F, 2, 8, 4, 0.0F, false));
        (this.rabbitLleg = new ModelRenderer(this)).setRotationPoint(3.0F, -2.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitLleg);
        this.rabbitLleg.cubeList.add(new ModelBox(this.rabbitLleg, 0, 0, -2.0F, 0.0F, -2.0F, 4, 2, 4, 0.0F, false));
        (this.rabbitHead = new ModelRenderer(this)).setRotationPoint(0.0F, -14.0F, -1.0F);
        this.rabbitBone.addChild(this.rabbitHead);
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 0, -3.0F, 0.0F, -4.0F, 6, 1, 6, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, -5.0F, -9.0F, -5.0F, 2, 3, 2, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 56, 0, 3.0F, -9.0F, -5.0F, 2, 3, 2, 0.0F, true));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 0, 45, -4.0F, -11.0F, -4.0F, 8, 11, 8, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, 1.0F, -20.0F, 0.0F, 3, 9, 1, 0.0F, false));
        this.rabbitHead.cubeList.add(new ModelBox(this.rabbitHead, 46, 0, -4.0F, -20.0F, 0.0F, 3, 9, 1, 0.0F, false));
        this.textureWidth = 64;
        this.textureHeight = 64;

        this.bipedLeftLeg = new ModelRenderer(this, 16, 48);
        this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_);
        this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedLeftLegwear = new ModelRenderer(this, 0, 48);
        this.bipedLeftLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedLeftLegwear.setRotationPoint(1.9F, 12.0F, 0.0F);
        this.bipedRightLegwear = new ModelRenderer(this, 0, 32);
        this.bipedRightLegwear.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedRightLegwear.setRotationPoint(-1.9F, 12.0F, 0.0F);
        this.bipedBodyWear = new ModelRenderer(this, 16, 32);
        this.bipedBodyWear.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, p_i46304_1_ + 0.25F);
        this.bipedBodyWear.setRotationPoint(0.0F, 0.0F, 0.0F);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
        GlStateManager.pushMatrix();

        CustomModel model = (CustomModel) Lemon.INSTANCE.getModuleManager().getModuleByName("Custom Model");

        if (model.isToggled()) {
            if (CustomModel.onlyMe.isToggled() && entityIn != Minecraft.getMinecraft().player) {
                super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
                if (this.isChild) {
                    float f = 2.0F;
                    GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
                    GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                    this.bipedLeftLegwear.render(scale);
                    this.bipedRightLegwear.render(scale);
                    this.bipedLeftArmwear.render(scale);
                    this.bipedRightArmwear.render(scale);
                    this.bipedBodyWear.render(scale);
                } else {
                    if (entityIn.isSneaking()) {
                        GlStateManager.translate(0.0F, 0.2F, 0.0F);
                    }

                    this.bipedLeftLegwear.render(scale);
                    this.bipedRightLegwear.render(scale);
                    this.bipedLeftArmwear.render(scale);
                    this.bipedRightArmwear.render(scale);
                    this.bipedBodyWear.render(scale);
                }

                GlStateManager.popMatrix();
                return;
            }

            float alpha = 0, red = 0, green = 0, blue = 0;

            switch (CustomModel.mode.getMode()) {
                case "Panda":
                    head.rotateAngleX = bipedHead.rotateAngleX;
                    head.rotateAngleY = bipedHead.rotateAngleY;
                    head.rotateAngleZ = bipedHead.rotateAngleZ;
                    nose.rotateAngleX = bipedHead.rotateAngleX;
                    nose.rotateAngleY = bipedHead.rotateAngleY;
                    nose.rotateAngleZ = bipedHead.rotateAngleZ;
                    ear1.rotateAngleX = bipedHead.rotateAngleX;
                    ear1.rotateAngleY = bipedHead.rotateAngleY;
                    ear1.rotateAngleZ = bipedHead.rotateAngleZ;
                    ear2.rotateAngleX = bipedHead.rotateAngleX;
                    ear2.rotateAngleY = bipedHead.rotateAngleY;
                    ear2.rotateAngleZ = bipedHead.rotateAngleZ;
                    if (entityIn.isSneaking()) {
                        bodyfront.rotateAngleX = -.08f;
                        bodyback.setRotationPoint(-5.f, 11.f, 3.f);
                        bodyback.rotateAngleX = -.61f;
                        tail.setRotationPoint(0, 19.f, 8.f);
                        tail.rotateAngleX = -.08f;
                        leg1.rotateAngleX = 0;
                        foot1.rotateAngleX = 0;
                        leg2.rotateAngleX = 0;
                        foot2.rotateAngleX = 0;
                        leg3.setRotationPoint(1.5f, 22.f, 9.f);
                        foot3.setRotationPoint(1.5f, 16.f, 3.f);
                        leg4.setRotationPoint(-1.5f, 22.f, 9.f);
                        foot4.setRotationPoint(-1.5f, 16.f, 3.f);
                        leg3.rotateAngleX = -1.57f;
                        foot3.rotateAngleX = 0;
                        leg4.rotateAngleX = -1.5f;
                        foot4.rotateAngleX = 0;
                    } else {
                        bodyfront.rotateAngleX = .08f;
                        bodyback.setRotationPoint(-5.f, 10.f, 3.f);
                        bodyback.rotateAngleX = -.08f;
                        tail.setRotationPoint(0, 14.f, 11.f);
                        leg3.setRotationPoint(1.5f, 16.f, 9.f);
                        foot3.setRotationPoint(1.5f, 16.f, 9.f);
                        leg4.setRotationPoint(-1.5f, 16.f, 9.f);
                        foot4.setRotationPoint(-1.5f, 16.f, 9.f);
                        float v = .66f;
                        leg1.rotateAngleX = MathHelper.cos(p_78088_2_ * v) * 1.4f * p_78088_3_;
                        foot1.rotateAngleX = MathHelper.cos(p_78088_2_ * v) * 1.4f * p_78088_3_;
                        leg2.rotateAngleX = MathHelper.cos(p_78088_2_ * v + MathHelper.PI) * 1.4f * p_78088_3_;
                        foot2.rotateAngleX = MathHelper.cos(p_78088_2_ * v + MathHelper.PI) * 1.4f * p_78088_3_;
                        leg3.rotateAngleX = MathHelper.cos(p_78088_2_ * v) * 1.4f * p_78088_3_;
                        foot3.rotateAngleX = MathHelper.cos(p_78088_2_ * v) * 1.4f * p_78088_3_;
                        leg4.rotateAngleX = MathHelper.cos(p_78088_2_ * v + MathHelper.PI) * 1.4f * p_78088_3_;
                        foot4.rotateAngleX = MathHelper.cos(p_78088_2_ * v + MathHelper.PI) * 1.4f * p_78088_3_;
                    }

                    head.render(scale);
                    nose.render(scale);
                    ear1.render(scale);
                    ear2.render(scale);
                    bodyfront.render(scale);
                    bodyback.render(scale);
                    leg1.render(scale);
                    foot1.render(scale);
                    leg2.render(scale);
                    foot2.render(scale);
                    leg3.render(scale);
                    foot3.render(scale);
                    leg4.render(scale);
                    foot4.render(scale);
                    tail.render(scale);
                    break;
                case "Rabbit":
                    alpha = (float) (0xFFFFFFFF >> 24 & 255) / 255.0F;
                    red = (float) (0xFFFFFFFF >> 16 & 255) / 255.0F;
                    green = (float) (0xFFFFFFFF >> 8 & 255) / 255.0F;
                    blue = (float) (0xFFFFFFFF & 255) / 255.0F;
                    GL11.glColor4f(red, green, blue, alpha);;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.25, 1.25, 1.25);
                    GlStateManager.translate(0, -.3, 0);
                    rabbitHead.rotateAngleX = bipedHead.rotateAngleX;
                    rabbitHead.rotateAngleY = bipedHead.rotateAngleY;
                    rabbitHead.rotateAngleZ = bipedHead.rotateAngleZ;
                    rabbitLarm.rotateAngleX = bipedLeftArm.rotateAngleX;
                    rabbitLarm.rotateAngleY = bipedLeftArm.rotateAngleY;
                    rabbitLarm.rotateAngleZ = bipedLeftArm.rotateAngleZ;
                    rabbitRarm.rotateAngleX = bipedRightArm.rotateAngleX;
                    rabbitRarm.rotateAngleY = bipedRightArm.rotateAngleY;
                    rabbitRarm.rotateAngleZ = bipedRightArm.rotateAngleZ;
                    rabbitRleg.rotateAngleX = bipedRightLeg.rotateAngleX;
                    rabbitRleg.rotateAngleY = bipedRightLeg.rotateAngleY;
                    rabbitRleg.rotateAngleZ = bipedRightLeg.rotateAngleZ;
                    rabbitLleg.rotateAngleX = bipedLeftLeg.rotateAngleX;
                    rabbitLleg.rotateAngleY = bipedLeftLeg.rotateAngleY;
                    rabbitLleg.rotateAngleZ = bipedLeftLeg.rotateAngleZ;
                    rabbitBone.render(scale);
                    GlStateManager.popMatrix();
                    break;
                case "Among Us":
                    bipedHead.rotateAngleY = p_78088_5_ * 0.017453292F;
                    bipedHead.rotateAngleX = p_78088_6_ * 0.017453292F;

                    bipedBody.rotateAngleY = 0;
                    bipedRightArm.rotationPointZ = 0;
                    bipedRightArm.rotationPointX = -5.f;
                    bipedLeftArm.rotationPointZ = 0;
                    bipedLeftArm.rotationPointX = 5.f;
                    float f = 1.f;

                    bipedRightArm.rotateAngleX = MathHelper.cos(p_78088_2_ * .6662f + MathHelper.PI) * 2 * p_78088_3_ * .5f / f;
                    bipedLeftArm.rotateAngleX = MathHelper.cos(p_78088_2_ * .6662f) * 2.f * p_78088_3_ * .5f / f;
                    bipedRightArm.rotateAngleZ = 0.f;
                    bipedLeftArm.rotateAngleZ = 0.f;
                    right_leg.rotateAngleX = MathHelper.cos(p_78088_2_ * .6662f) * 1.4f * p_78088_3_ / f;
                    left_leg.rotateAngleX = MathHelper.cos(p_78088_2_ * .6662f + MathHelper.PI) * 1.4f * p_78088_3_ / f;
                    right_leg.rotateAngleY = 0.f;
                    left_leg.rotateAngleY = 0.f;
                    right_leg.rotateAngleZ = 0.f;
                    left_leg.rotateAngleZ = 0.f;

                    switch (CustomModel.amongusMode.getMode()) {
                        case "Sync":
                            alpha = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 24 & 255) / 255.0F;
                            red = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 16 & 255) / 255.0F;
                            green = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 8 & 255) / 255.0F;
                            blue = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() & 255) / 255.0F;
                            GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
                            break;
                        case "Random":
                            alpha = (float) (CustomModel.getColor(entityIn).getRGB() >> 24 & 255) / 255.0F;
                            red = (float) (CustomModel.getColor(entityIn).getRGB() >> 16 & 255) / 255.0F;
                            green = (float) (CustomModel.getColor(entityIn).getRGB() >> 8 & 255) / 255.0F;
                            blue = (float) (CustomModel.getColor(entityIn).getRGB() & 255) / 255.0F;
                            GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
                            break;
                    }
                    if (isChild) {
                        GlStateManager.scale(.5, .5, .5);
                        GlStateManager.translate(0, 24. * scale, 0);
                        body.render(scale);
                        left_leg.render(scale);
                        right_leg.render(scale);
                    } else {
                        GlStateManager.translate(0, -.8, 0);
                        GlStateManager.scale(1.8, 1.6, 1.6);
                        GlStateManager.translate(0, .15, 0);
                        body.render(scale);
                        alpha = (float) (0xff00ffff >> 24 & 255) / 255.0F;
                        red = (float) (0xff00ffff >> 16 & 255) / 255.0F;
                        green = (float) (0xff00ffff >> 8 & 255) / 255.0F;
                        blue = (float) (0xff00ffff & 255) / 255.0F;
                        GL11.glColor4f(red, green, blue, alpha);
                        eye.render(scale);
                        switch (CustomModel.amongusMode.getMode()) {
                            case "Sync":
                                alpha = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 24 & 255) / 255.0F;
                                red = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 16 & 255) / 255.0F;
                                green = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() >> 8 & 255) / 255.0F;
                                blue = (float) (Lemon.INSTANCE.getColorManager().getColor().getFirstColor().getRGB() & 255) / 255.0F;
                                GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
                                break;
                            case "Random":
                                alpha = (float) (CustomModel.getColor(entityIn).getRGB() >> 24 & 255) / 255.0F;
                                red = (float) (CustomModel.getColor(entityIn).getRGB() >> 16 & 255) / 255.0F;
                                green = (float) (CustomModel.getColor(entityIn).getRGB() >> 8 & 255) / 255.0F;
                                blue = (float) (CustomModel.getColor(entityIn).getRGB() & 255) / 255.0F;
                                GL11.glColor4f(red, green, blue, alpha == 0.0F ? 1.0F : alpha);
                                break;
                        }
                        GlStateManager.translate(0, -.15, 0);
                        left_leg.render(scale);
                        right_leg.render(scale);
                        GlStateManager.color(1, 1, 1, 1);
                    }
                    break;
            }
        } else {
            super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
            if (this.isChild) {
                float f = 2.0F;
                GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
                GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            } else {
                if (entityIn.isSneaking()) {
                    GlStateManager.translate(0.0F, 0.2F, 0.0F);
                }

                this.bipedLeftLegwear.render(scale);
                this.bipedRightLegwear.render(scale);
                this.bipedLeftArmwear.render(scale);
                this.bipedRightArmwear.render(scale);
                this.bipedBodyWear.render(scale);
            }
        }

        GlStateManager.popMatrix();
    }

    public void renderDeadmau5Head(float p_178727_1_)
    {
        copyModelAngles(this.bipedHead, this.bipedDeadmau5Head);
        this.bipedDeadmau5Head.rotationPointX = 0.0F;
        this.bipedDeadmau5Head.rotationPointY = 0.0F;
        this.bipedDeadmau5Head.render(p_178727_1_);
    }

    public void renderCape(float p_178728_1_)
    {
        this.bipedCape.render(p_178728_1_);
    }

    public static int skyRainbow(int delay, double speed) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay) / speed);
        rainbow %= 360D;
        return Color.HSBtoRGB((double) ((float) ((rainbow %= 360) / 360)) < .5 ? -((float) (rainbow / 360))
                : (float) (rainbow / 360), .6f, 1);
    }

    public static int getRed(int argb) {
        return argb >> 16 & 0xFF;
    }

    public static int getGreen(int argb) {
        return argb >> 8 & 0xFF;
    }

    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
    {
        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, entityIn);
        copyModelAngles(this.bipedLeftLeg, this.bipedLeftLegwear);
        copyModelAngles(this.bipedRightLeg, this.bipedRightLegwear);
        copyModelAngles(this.bipedLeftArm, this.bipedLeftArmwear);
        copyModelAngles(this.bipedRightArm, this.bipedRightArmwear);
        copyModelAngles(this.bipedBody, this.bipedBodyWear);
    }

    public void renderRightArm()
    {
        this.bipedRightArm.render(0.0625F);
        this.bipedRightArmwear.render(0.0625F);
    }

    public void renderLeftArm()
    {
        this.bipedLeftArm.render(0.0625F);
        this.bipedLeftArmwear.render(0.0625F);
    }

    public void setInvisible(boolean invisible)
    {
        super.setInvisible(invisible);
        this.bipedLeftArmwear.showModel = invisible;
        this.bipedRightArmwear.showModel = invisible;
        this.bipedLeftLegwear.showModel = invisible;
        this.bipedRightLegwear.showModel = invisible;
        this.bipedBodyWear.showModel = invisible;
        this.bipedCape.showModel = invisible;
        this.bipedDeadmau5Head.showModel = invisible;
    }

    public void postRenderArm(float scale)
    {
        if (this.smallArms)
        {
            ++this.bipedRightArm.rotationPointX;
            this.bipedRightArm.postRender(scale);
            --this.bipedRightArm.rotationPointX;
        }
        else
        {
            this.bipedRightArm.postRender(scale);
        }
    }
}
