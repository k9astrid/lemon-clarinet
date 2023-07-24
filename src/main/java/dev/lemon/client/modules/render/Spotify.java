package dev.lemon.client.modules.render;

import dev.lemon.api.module.Module;
import dev.lemon.api.setting.impl.TextSetting;
import dev.lemon.api.spotify.SpotifyAPI;

public class Spotify extends Module {
    public TextSetting clientID = new TextSetting("Client ID", "");

    public SpotifyAPI api;

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
}
