package ot.dan.bluemapmobs.objects;

import com.flowpowered.math.vector.Vector2i;

public class MobInstance {
    private String image;
    private Vector2i anchor;

    public MobInstance(String image, Vector2i anchor) {
        this.image = image;
        this.anchor = anchor;
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
