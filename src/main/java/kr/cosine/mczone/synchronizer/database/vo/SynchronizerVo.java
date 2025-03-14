package kr.cosine.mczone.synchronizer.database.vo;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.GameType;

import java.util.Collections;
import java.util.List;

public class SynchronizerVo {
    private static final SynchronizerVo DEFAULT_INSTANCE = new SynchronizerVo(100f, 100f, new ListTag(), 0, Collections.emptyList(), GameType.SURVIVAL.getId());

    public static SynchronizerVo getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private float health;

    private float maxHealth;

    private ListTag inventory;

    private int heldItemSlot;

    private List<MobEffectInstance> mobEffects;

    private GameType gameMode;

    public SynchronizerVo(float health, float maxHealth, ListTag inventory, int heldItemSlot, List<MobEffectInstance> mobEffects, int gameModeId) {
        this.health = health;
        this.maxHealth = maxHealth;
        this.inventory = inventory;
        this.heldItemSlot = heldItemSlot;
        this.mobEffects = mobEffects;
        this.gameMode = GameType.byId(gameModeId);
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

    public GameType getGameMode() {
        return gameMode;
    }
}
