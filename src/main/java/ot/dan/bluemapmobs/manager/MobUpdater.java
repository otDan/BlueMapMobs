package ot.dan.bluemapmobs.manager;

import com.flowpowered.math.vector.Vector2i;
import com.flowpowered.math.vector.Vector3d;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import ot.dan.bluemapmobs.Main;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MobUpdater implements Runnable {
    private final Main plugin;
    private final Set<String> mobMarkers;
    private MarkerSet mobs;

    public MobUpdater(Main plugin) {
        this.plugin = plugin;
        this.mobMarkers = new HashSet<>();
    }

    @Override
    public void run() {
        removeMobMarkers();
        addMobMarkers();
    }

    public void addMobMarkers() {
        Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
        if (api.isPresent()) {
            try {
                MarkerAPI markerAPI = api.get().getMarkerAPI();
                this.mobs = markerAPI.createMarkerSet("mobs");
                this.mobs.setDefaultHidden(true);
                for (World world : Bukkit.getWorlds()) {
                    api.flatMap(blueMapAPI -> blueMapAPI.getWorld(world.getUID())).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
                        for (Entity entity : world.getEntities()) {
                            Location entityLocation = entity.getLocation();
                            if (entityLocation.getY() >= 60) {
                                String mobImageURL = plugin.getImageManager().getMobImgUrl(entity.getType());
                                if (mobImageURL != null) {
                                    String entityMarkerId = String.format("entity:%s:%s:%s", map.getName(), entity.getType().name(), entity.getUniqueId());
                                    Vector3d warpMarkerPos = Vector3d.from(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ());
                                    if (this.mobs != null) {
                                        POIMarker warpMarker = this.mobs.createPOIMarker(entityMarkerId, map, warpMarkerPos);
                                        warpMarker.setLabel(entity.getType().name());
                                        Vector2i iconAnchor = this.plugin.getImageManager().getMobAnchor(entity.getType());
                                        warpMarker.setIcon(mobImageURL, iconAnchor);
                                        this.mobMarkers.add(warpMarker.getId());
                                    }
                                }
                            }
                        }
                    }));
                }
                markerAPI.save();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void removeMobMarkers() {
        try {
            Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
            if (api.isPresent()) {
                MarkerAPI markerAPI = api.get().getMarkerAPI();
                MarkerSet markerSetMobs = markerAPI.createMarkerSet("mobs");
                this.mobMarkers.forEach(markerSetMobs::removeMarker);
                this.mobMarkers.clear();
                markerAPI.save();
            }
        } catch (IOException ignored) {
        }
    }
}
