package ot.dan.bluemapmobs;

import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ot.dan.bluemapmobs.objects.ImageManager;
import ot.dan.bluemapmobs.objects.MobUpdater;

import java.util.Optional;

public class Main extends JavaPlugin {
    private Main instance;
    private ImageManager imageManager;

    @Override
    public void onEnable() {
        instance = this;
        BlueMapAPI.onEnable(api -> {
            registerBlueMap();
            MobUpdater mobUpdater = new MobUpdater(instance);
            Bukkit.getScheduler().runTaskTimerAsynchronously(instance, mobUpdater, 0, 20*3);
        });
    }

    @Override
    public void onDisable() {

    }

    private void registerBlueMap() {
        Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
        if(api.isPresent()) {
            this.imageManager = new ImageManager(this);
        }

    }

    public ImageManager getImageManager() {
        return imageManager;
    }
}
