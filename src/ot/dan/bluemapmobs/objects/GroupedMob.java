package ot.dan.bluemapmobs.objects;

import org.bukkit.entity.EntityType;

public class GroupedMob {
    private EntityType entityType;
    private int amount;

    public GroupedMob(EntityType entityType, int amount) {
        this.entityType = entityType;
        this.amount = amount;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getAmount() {
        return amount;
    }
}
