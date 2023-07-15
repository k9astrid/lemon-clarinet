package dev.lemon.gui.clickgui;

import dev.lemon.Lemon;
import dev.lemon.module.Module;
import dev.lemon.utils.render.ColorUtil;
import dev.lemon.utils.render.RenderUtil;
import dev.lemon.utils.math.TimerUtil;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

import org.lwjgl.opengl.GL11;

public class ClickGui extends GuiScreen {
    TimerUtil timer = new TimerUtil();
    private ScaledResolution sr = new ScaledResolution(mc);
    private int guiPosX = sr.getScaledWidth() / 2 - 200;
    private int guiPosY = sr.getScaledHeight() / 2 - 125;
    private boolean isDragging = false;
    private int startX, startY;

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {

    }

    private boolean mouseHoveredOver(float left, float top, float right, float bottom, int mouseX, int mouseY){
        return (mouseX >= left && mouseY >= top && mouseX < right && mouseY < bottom);
    }
    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }


    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        /*
        switch (keyCode){
            case Keyboard.KEY_RSHIFT:
                Lemon.INSTANCE.getModuleManager().getModuleByName("ClickGUI").toggle();
                mc.displayGuiScreen(null);
                break;
        }

         */
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton == 0) {
            startX = mouseX;
            startY = mouseY;

            if (mouseHoveredOver(guiPosX, guiPosY, 300 + guiPosX, 24 + guiPosY, mouseX, mouseY)) {
                isDragging = true;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            isDragging = false;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

    }
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        float mult = 0.0035F;

        if(mult * timer.getElapsedTime() < 2.1) {mult -= (float) (timer.getElapsedTime() * 0.0000022);}
        mult *= timer.getElapsedTime();
        mult = Math.min(mult, 1); //makes it inf large without this :(

        GL11.glTranslatef(sr.getScaledWidth() / 2 - mult * sr.getScaledWidth() / 2, sr.getScaledHeight() / 2 - mult * sr.getScaledHeight() / 2, 1);
        GL11.glScalef(mult, mult, 1);//does da funny scaling

        //Drawrect(x, y, width + x, height + or - y)
 /*
        GlStateManager.pushMatrix();
        //GL11.glScalef(MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1), MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1), MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1));
        GL11.glScalef(MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1), MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1), MathHelper.clamp_float((float) timer.getTimeElapsed() /500, 0.2f, 1));
    */
        GlStateManager.pushMatrix();
        Gui.drawRect(guiPosX, guiPosY,  400 + guiPosX, 250 + guiPosY, 0xff111111);
        Gui.drawRect(guiPosX, guiPosY,  400 + guiPosX, 24 + guiPosY, 0xff191919);
        Gui.drawRect(guiPosX, guiPosY,  400 + guiPosX, 2 + guiPosY, ColorUtil.fadeLemonColors(0));

        RenderUtil.drawImage(new ResourceLocation("lemon/images/logo.png"), guiPosX +5, guiPosY, 70, 24); // TODO: make the texture load

        mc.fontRendererObj.drawStringWithShadow(Lemon.INSTANCE.getVersion(), guiPosX + 64 + mc.fontRendererObj.getStringWidth(Lemon.INSTANCE.getVersion()), guiPosY + 4,-1);
        int offset = 0;
        for (Module.Category c : Module.Category.values()){
            mc.fontRendererObj.drawString(c.name(),guiPosX + 5, (guiPosY + 5) + 26 + offset, 0xffFFFFFF);

            offset += 30;
        }
        GlStateManager.popMatrix();

        if (isDragging) {
            int offsetX = mouseX - startX;
            int offsetY = mouseY - startY;

            guiPosX += offsetX;
            guiPosY += offsetY;

            startX = mouseX;
            startY = mouseY;
        }
    }
}
