package kr.cosine.mczone.synchronizer.database.vo;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Collections;
import java.util.List;

public class SynchronizerVo {
    public static float DEFAULT_HEALTH = 100f;

    private static final SynchronizerVo DEFAULT_INSTANCE = new SynchronizerVo(DEFAULT_HEALTH, DEFAULT_HEALTH, new ListTag(), 0, Collections.emptyList());

    public static SynchronizerVo getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private float health;

    private float maxHealth;

    private ListTag inventory;

    private int heldItemSlot;

    private List<MobEffectInstance> mobEffects;

    public SynchronizerVo(float health, float maxHealth, ListTag inventory, int heldItemSlot, List<MobEffectInstance> mobEffects) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.inventory = inventory;
        this.heldItemSlot = heldItemSlot;
        this.mobEffects = mobEffects;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public ListTag getInventory() {
        return inventory;
    }

    public int getHeldItemSlot() {
        return heldItemSlot;
    }

    public List<MobEffectInstance> getMobEffects() {
        return mobEffects;
    }
}
