package com.maxim.bodymarket;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/** Сохраняет в мире: какие части проданы, какая болезнь, когда следующая. */
public class BodyData extends PersistentState {
    public static class PlayerBody {
        public final Set<String> parts = new HashSet<>();
        public String disease = "";
        public long nextRoll = 0;
        public boolean egg = false;
    }

    private final Map<UUID, PlayerBody> players = new HashMap<>();

    public PlayerBody of(UUID id) {
        return players.computeIfAbsent(id, k -> new PlayerBody());
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound all = new NbtCompound();
        players.forEach((id, b) -> {
            NbtCompound c = new NbtCompound();
            NbtList list = new NbtList();
            b.parts.forEach(s -> list.add(NbtString.of(s)));
            c.put("parts", list);
            c.putString("disease", b.disease);
            c.putLong("next", b.nextRoll);
            c.putBoolean("egg", b.egg);
            all.put(id.toString(), c);
        });
        nbt.put("players", all);
        return nbt;
    }

    public static BodyData fromNbt(NbtCompound nbt) {
        BodyData d = new BodyData();
        NbtCompound all = nbt.getCompound("players");
        for (String key : all.getKeys()) {
            NbtCompound c = all.getCompound(key);
            PlayerBody b = d.of(UUID.fromString(key));
            NbtList list = c.getList("parts", NbtElement.STRING_TYPE);
            for (int i = 0; i < list.size(); i++) b.parts.add(list.getString(i));
            b.disease = c.getString("disease");
            b.nextRoll = c.getLong("next");
            b.egg = c.getBoolean("egg");
        }
        return d;
    }

    public static BodyData get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager()
            .getOrCreate(BodyData::fromNbt, BodyData::new, "bodymarket");
    }
}
