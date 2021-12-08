package ot.dan.bluemapmobs;

import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import ot.dan.bluemapmobs.objects.ImageManager;
import ot.dan.bluemapmobs.manager.MobUpdater;

import java.util.Optional;

public class Main extends JavaPlugin {
    private ImageManager imageManager;
    private BukkitTask updaterTaks;

    @Override
    public void onEnable() {
        BlueMapAPI.onEnable(api -> {
            registerBlueMap();
            MobUpdater mobUpdater = new MobUpdater(this);
            this.updaterTaks = Bukkit.getScheduler().runTaskTimerAsynchronously(this, mobUpdater, 0, 20*3);
        });
    }

    @Override
    public void onDisable() {
        if (this.updaterTaks != null) {
            this.updaterTaks.cancel();
        }
    }

    private void registerBlueMap() {
        Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
        if(api.isPresent()) {
            this.imageManager = new ImageManager(this);
        }
    }

    public ImageManager getImageManager() {
        return this.imageManager;
    }
}
