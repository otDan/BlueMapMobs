package ot.dan.bluemapmobs.objects;

import com.flowpowered.math.vector.Vector2i;

public class MobInstance {
    private String entity;
    private String image;
    private Vector2i anchor;

    public MobInstance(String entity, String image, Vector2i anchor) {
        this.entity = entity;
        this.image = image;
        this.anchor = anchor;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Vector2i getAnchor() {
        return anchor;
    }

    public void setAnchor(Vector2i anchor) {
        this.anchor = anchor;
    }
}
