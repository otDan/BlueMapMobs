package ot.dan.bluemapmobs.objects;

import com.flowpowered.math.vector.Vector2i;
import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.marker.Marker;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.POIMarker;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import ot.dan.bluemapmobs.Main;
import com.flowpowered.math.vector.Vector3d;

import java.io.IOException;
import java.util.*;

public class MobUpdater implements Runnable {
    private final Main plugin;
    private final Set<String> mobMarkers;
    private MarkerSet mobs;
    private MarkerAPI markerAPI;

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
        if(api.isPresent()) {
            try {
                this.markerAPI = api.get().getMarkerAPI();
                this.mobs = markerAPI.createMarkerSet("mobs");
                this.mobs.setDefaultHidden(true);
                for (World world : Bukkit.getWorlds()) {
                    api.flatMap(blueMapAPI -> blueMapAPI.getWorld(world.getUID())).ifPresent(blueWorld -> blueWorld.getMaps().forEach(map -> {
                        for (Entity entity : world.getEntities()) {
                            Location entityLocation = entity.getLocation();
                            if (entityLocation.getY() >= 60) {
                                String mobImageURL = plugin.getImageManager().getMobImgUrl(entity.getType().name().toLowerCase());
                                if (mobImageURL != null) {
                                    String entityMarkerId = String.format("entity:%s:%s:%s", map.getName(), entity.getType().name(), entity.getUniqueId());
                                    Vector3d warpMarkerPos = Vector3d.from(entityLocation.getX(), entityLocation.getY(), entityLocation.getZ());
                                    if (this.mobs != null) {
                                        POIMarker warpMarker = this.mobs.createPOIMarker(entityMarkerId, map, warpMarkerPos);
                                        warpMarker.setLabel(entity.getType().name());
                                        Vector2i iconAnchor = plugin.getImageManager().getMobAnchor(entity.getType().name().toLowerCase());
                                        warpMarker.setIcon(mobImageURL, iconAnchor);
                                        this.mobMarkers.add(warpMarker.getId());
                                    }
                                }
                            }
                        }
                    }));
                }
                this.markerAPI.save();
            }
            catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void removeMobMarkers() {
        try {
            Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
            if(api.isPresent()) {
                MarkerAPI markerAPI = api.get().getMarkerAPI();
                MarkerSet markerSetMobs = markerAPI.createMarkerSet("mobs");
                mobMarkers.forEach(markerSetMobs::removeMarker);
                mobMarkers.clear();
                markerAPI.save();
            }
        } catch (IOException ignored) {
        }
    }
}
