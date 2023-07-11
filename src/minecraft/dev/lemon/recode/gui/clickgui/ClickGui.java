package dev.lemon.recode.gui.clickgui;

import dev.lemon.recode.Lemon;
import dev.lemon.recode.module.Category;
import dev.lemon.recode.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import java.io.IOException;

public class ClickGui extends GuiScreen {


    private int guiPosX = this.width /2;
    private int guiPosY = this.height / 2;
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
        org.lwjgl.input.Keyboard.enableRepeatEvents(false);
    }


    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        switch (keyCode){
            case Keyboard.KEY_RSHIFT:
                Lemon.INSTANCE.getModuleManager().getModuleByName("ClickGUI").toggle();
                mc.displayGuiScreen(null);
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) {
            startX = mouseX;
            startY = mouseY;

            if (mouseHoveredOver(guiPosX, guiPosY, 300 + guiPosX, 10 + guiPosY, mouseX, mouseY)) {
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

        // Drawrect(x, y, width + x, height + or - y)

        Gui.drawRect(guiPosX, guiPosY,  300 + guiPosX, 250 + guiPosY, 0xff111111);
        RenderUtil.drawImage(new ResourceLocation("lemon/images/logo.png"), guiPosX +5, guiPosY + 5, 77, 24);
        for (Category c : Category.values()){
            GlStateManager.pushMatrix();
            GlStateManager.popMatrix();
        }
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
