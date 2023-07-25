package dev.lemon.client.modules.render;

import dev.lemon.api.event.IEventListener;
import dev.lemon.api.event.annotations.Subscribe;
import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.NumberSetting;
import dev.lemon.api.setting.impl.TextSetting;
import dev.lemon.api.spotify.SpotifyAPI;
import dev.lemon.api.utils.font.Fonts;
import dev.lemon.api.utils.other.Animation;
import dev.lemon.api.utils.other.DecelerateAnimation;
import dev.lemon.api.utils.other.Direction;
import dev.lemon.api.utils.render.RenderUtil;
import dev.lemon.client.events.input.MouseEvent;
import dev.lemon.client.events.render.Render2DEvent;
import dev.lemon.client.gui.dropdown.Scissoring;
import dev.lemon.client.main.Lemon;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spotify extends Module {
    public TextSetting clientID = new TextSetting("Client ID");

    public NumberSetting posX = new NumberSetting("Pos X",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().width / 2, 1, () -> false);
    public NumberSetting posY = new NumberSetting("Pos Y",
            0, 0, (double) Toolkit.getDefaultToolkit().getScreenSize().height / 2, 1, () -> false);

    public SpotifyAPI api;
    private boolean dragging;
    private double draggingX, draggingY, width, height;
    private boolean downloadedCover;
    private ResourceLocation currentAlbumCover;
    private CurrentlyPlayingContext currentPlayingContext;
    private Track currentTrack;

    private final Animation scrollTrack = new DecelerateAnimation(10000, 1, Direction.BACKWARDS);
    private final Animation scrollArtist = new DecelerateAnimation(10000, 1, Direction.BACKWARDS);

    public Spotify() {
        super("Spotify", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        if (mc.player == null) {
            toggle();
            return;
        }

        if (api == null)
            api = new SpotifyAPI();

        api.build(clientID.getText());
        api.connect();
    }

    @Subscribe
    private final IEventListener<Render2DEvent> onRender2D = e -> {
        if (api == null)
            return;

        if (api.currentTrack == null || api.currentPlayingContext == null)
            return;

        if (currentPlayingContext != api.currentPlayingContext) {
            this.currentPlayingContext = api.currentPlayingContext;
        }

        if (currentTrack != api.currentTrack) {
            this.currentTrack = api.currentTrack;
        }

        ScaledResolution sr = new ScaledResolution(mc);

        if (this.dragging) {
            if (!(mc.currentScreen instanceof GuiChat)) {
                this.dragging = false;
            } else {
                this.posX.setValue(this.draggingX + ((double) (Mouse.getX() * sr.getScaledWidth()) / mc.displayWidth));
                this.posY.setValue(this.draggingY + (sr.getScaledHeight() - (double) (Mouse.getY() *
                        sr.getScaledHeight()) / mc.displayHeight - 1));
            }
        }

        this.posX.setValue(Math.min(this.posX.getVal(), sr.getScaledWidth() - this.width - 1));
        this.posY.setValue(Math.min(this.posY.getVal(), sr.getScaledHeight() - this.height - 1));
        this.posX.setValue(Math.max(this.posX.getVal(), 1));
        this.posY.setValue(Math.max(this.posY.getVal(), 0.5));

        GlStateManager.pushMatrix();
        GlStateManager.translate(this.posX.getVal(), this.posY.getVal(), 0);
        this.width = 135 + 20;
        this.height = 45;

        RenderUtil.drawGradientRound(0, 0, (float) width, (float) height, 6,
                Lemon.INSTANCE.getColorManager().getColor().getGradientColor1(),
                Lemon.INSTANCE.getColorManager().getColor().getGradientColor2(),
                Lemon.INSTANCE.getColorManager().getColor().getGradientColor3(),
                Lemon.INSTANCE.getColorManager().getColor().getGradientColor4());

        RenderUtil.drawRound(1, 1, (float) width - 2, (float) height - 2, 5, new Color(0, 0, 0, 160));

        Scissoring.push();
        Scissoring.setFromComponentCoordinates((int) (posX.getVal() + 45), (int) (posY.getVal() + 3), (int) width - (45), (int) height);
        final StringBuilder artistsDisplay = new StringBuilder();
        for (int artistIndex = 0; artistIndex < currentTrack.getArtists().length; artistIndex++) {
            final ArtistSimplified artist = currentTrack.getArtists()[artistIndex];
            artistsDisplay.append(artist.getName()).append(artistIndex + 1 == currentTrack.getArtists().length ? '.' : ", ");
        }

        if (scrollTrack.finished(Direction.BACKWARDS)) {
            scrollTrack.reset();
        }
        if (scrollArtist.finished(Direction.BACKWARDS)) {
            scrollArtist.reset();
        }

        boolean needsToScrollTrack = Fonts.GREYCLIFF_BOLD_26.getStringWidth(currentTrack.getName()) > 48;
        boolean needsToScrollArtist = Fonts.GREYCLIFF_22.getStringWidth(artistsDisplay.toString()) > 140 + (45);

        Fonts.GREYCLIFF_BOLD_26.drawStringWithShadow(currentTrack.getName(), 45, 5, -1);
        Fonts.GREYCLIFF_16.drawStringWithShadow(artistsDisplay.toString(), 46, 17 + 2, new Color(201, 201, 201, 255).getRGB());
        Scissoring.unset();
        Scissoring.pop();

        if (currentAlbumCover != null && downloadedCover) {
            mc.getTextureManager().bindTexture(currentAlbumCover);
            GlStateManager.color(1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            RenderUtil.drawRoundTextured(5.5f, 5.5f, 34, 34, 3, 1);
        }

        if ((currentAlbumCover == null || !currentAlbumCover.getResourcePath().contains(currentTrack.getAlbum().getId()))) {
            downloadedCover = false;

            final ThreadDownloadImageData albumCover = new ThreadDownloadImageData(null, currentTrack.getAlbum().getImages()[1].getUrl(), null, new IImageBuffer() {
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    downloadedCover = true;
                    return image;
                }

                @Override
                public void skinAvailable() { }
            });

            mc.getTextureManager().loadTexture(currentAlbumCover = new ResourceLocation("spotifyAlbums/" + currentTrack.getAlbum().getId()), albumCover);
        }

        RenderUtil.drawRound(45F, (float) (this.height - 10), (float) (this.width - 50f) - 15, 4, 1.5f, new Color(0, 0, 0, 170));
        RenderUtil.drawRound(45F, (float) (this.height - 10), (float) (this.width - 50f - 14) * ((float) currentPlayingContext.getProgress_ms() / currentTrack.getDurationMs()), 4, 1.5f, new Color(255, 255, 255, 255));

        GlStateManager.popMatrix();
    };

    @Subscribe
    private final IEventListener<MouseEvent> onMouse = e -> {
        switch (e.getType()){
            case CLICK:
                if(e.getMouseButton() == 0){
                    if(isMouseInBounds(e.getMouseX(), e.getMouseY(),
                            this.posX.getVal(), this.posY.getVal(),
                            this.posX.getVal() + this.width,
                            this.posY.getVal() + this.height)){
                        this.dragging = true;
                        this.draggingX = this.posX.getVal() - e.getMouseX();
                        this.draggingY = this.posY.getVal() - e.getMouseY();
                    }
                }
                break;
            case RELEASED:
                this.dragging = false;
                break;
        }
    };

    public boolean isMouseInBounds(double mouseX, double mouseY, double x, double y, double x1, double y1) {
        return mouseX >= x && mouseX <= x1 && mouseY >= y && mouseY <= y1;
    }
}
