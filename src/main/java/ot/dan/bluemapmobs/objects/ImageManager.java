package ot.dan.bluemapmobs.objects;

import com.flowpowered.math.vector.Vector2i;
import de.bluecolored.bluemap.api.BlueMapAPI;
import org.bukkit.entity.EntityType;
import ot.dan.bluemapmobs.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ImageManager {
    private final Main plugin;
    private final HashMap<EntityType, MobInstance> mobInstances;

    public ImageManager(Main plugin) {
        this.plugin = plugin;
        this.mobInstances = new HashMap<>();
        loadImages();
    }

    private void loadImages() {
        int i = 0, k = 0;
        for (EntityType entity : EntityType.values()) {
            String name = entity.name().toLowerCase();
            try (InputStream mobImage = this.plugin.getResource(name + ".png")) {
                if (mobImage != null) {
                    Optional<BlueMapAPI> api = BlueMapAPI.getInstance();
                    if (api.isPresent()) {
                        BufferedImage image = ImageIO.read(mobImage);
                        Vector2i anchor = new Vector2i(image.getWidth() / 2, image.getHeight() / 2);
                        try {
                            EntityType entityType = EntityType.valueOf(name.toUpperCase());
                            this.mobInstances.put(entityType, new MobInstance(api.get().createImage(image, "bluemapmobs/icons/" + name), anchor));
                            System.out.println("[ V ] Loaded image for entity " + name);
                            i++;
                        }
                        catch (EnumConstantNotPresentException ignored) {
                            System.out.println("[ V ] Entity of image not existent " + name);
                        }
                    }
                } else {
                    System.out.println("[ * ] Not loaded image for entity " + name);
                    k++;
                }
            } catch (IOException ignored) {
                System.out.println("[ X ] Image loading exception for entity " + name);
            }
        }
        System.out.println("[ W ] BlueMap Mobs " + i + " images loaded and " + k + " images not loaded");
    }

    public Vector2i getMobAnchor(EntityType entityType) {
        if (this.mobInstances.containsKey(entityType)) {
            return this.mobInstances.get(entityType).getAnchor();
        }
        return null;
    }

    public String getMobImgUrl(EntityType entityType) {
        if (this.mobInstances.containsKey(entityType)) {
            return this.mobInstances.get(entityType).getImage();
        }
        return null;
    }
}
