package dev.lemon.api.utils.render;

import lombok.experimental.UtilityClass;
import net.minecraft.client.shader.Framebuffer;

@UtilityClass
public class PostProcessingUtil {

    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public void drawBloom(Runnable runnable) {
        stencilFramebuffer = RenderUtil.createFrameBuffer(stencilFramebuffer);
        stencilFramebuffer.framebufferClear();
        stencilFramebuffer.bindFramebuffer(false);
        runnable.run();
        stencilFramebuffer.unbindFramebuffer();

        KawaseBloom.renderBlur(stencilFramebuffer.framebufferTexture, 2, 3);
    }

}
