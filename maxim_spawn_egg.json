package com.maxim.bodymarket;

import net.minecraft.entity.effect.StatusEffects;

/**
 * Болезни с «жизненными» симптомами, привязанные к агентам Valorant.
 * Постоянные эффекты задаются здесь, особые (вспышки, тряска экрана)
 * делаются в Market.special и на клиенте в DiseaseFx.
 * Ни одна болезнь не убивает сама по себе.
 */
public enum Disease {
    NARCOLEPSY("narcolepsy", "Нарколепсия", "Omen",
        "Каждые 5 секунд ты вырубаешься: экран гаснет и ты не можешь двигаться."),
    MIGRAINE("migraine", "Мигрень", "Reyna",
        "Голова раскалывается: экран пульсирует, слабость, тяжело копать.",
        new Part.Fx(StatusEffects.WEAKNESS, 0), new Part.Fx(StatusEffects.MINING_FATIGUE, 0)),
    ASTHMA("asthma", "Астма", "Brimstone",
        "Время от времени душит кашель: на несколько секунд почти не можешь бежать."),
    PHOENIX("phoenix", "Лихорадка Феникса", "Phoenix",
        "Жар: перед глазами красная дымка, слабость, ноги ватные.",
        new Part.Fx(StatusEffects.WEAKNESS, 0), new Part.Fx(StatusEffects.SLOWNESS, 0)),
    TREMOR("tremor", "Тремор рук", "Chamber",
        "Руки трясутся: прицел гуляет, копать тяжело.",
        new Part.Fx(StatusEffects.MINING_FATIGUE, 0)),
    NIGHTBLIND("nightblind", "Куриная слепота", "Yoru",
        "Ночью почти ничего не видно. Днём всё нормально."),
    AMNESIA("amnesia", "Амнезия", "Cypher",
        "Забываешь, что держал в руке: предмет в руке меняется сам."),
    HICCUPS("hiccups", "Икота", "Jett",
        "Каждые 10 секунд тебя подбрасывает от икоты."),
    INSOMNIA("insomnia", "Бессонница", "Fade",
        "Ты не можешь уснуть в кровати. Слабость от недосыпа.",
        new Part.Fx(StatusEffects.WEAKNESS, 0)),
    VERTIGO("vertigo", "Головокружение", "Gekko",
        "Время от времени мир начинает плыть и тебя мутит."),
    DIABETES("diabetes", "Диабет", "Neon",
        "Постоянный голод, а раз в минуту — резкий упадок сил.",
        new Part.Fx(StatusEffects.HUNGER, 1)),
    COLD("cold", "Простуда", "Sage",
        "Ноги ватные, периодически чихаешь и теряешь ход.",
        new Part.Fx(StatusEffects.SLOWNESS, 0)),
    COLORBLIND("colorblind", "Дальтонизм", "Harbor",
        "Мир стал серым, а изумруды никак не найти: удача хуже.",
        new Part.Fx(StatusEffects.UNLUCK, 0)),
    ARRHYTHMIA("arrhythmia", "Аритмия", "Raze",
        "Сердце колотится: красные вспышки на экране, иногда сбивается ход.",
        new Part.Fx(StatusEffects.WEAKNESS, 0)),
    VIPER("viper", "Яд Вайпер", "Viper",
        "Токсин в крови. Здоровье тает, но до смерти не доведёт.",
        new Part.Fx(StatusEffects.POISON, 0)),
    SOVA("sova", "Туннельное зрение", "Sova",
        "Как будто смотришь через дрон: вокруг темнеет.",
        new Part.Fx(StatusEffects.DARKNESS, 0)),
    OMEN("omen", "Теневой кашель", "Omen",
        "Тело тяжёлое, как в тени: тяжело ходить и копать.",
        new Part.Fx(StatusEffects.SLOWNESS, 0), new Part.Fx(StatusEffects.MINING_FATIGUE, 0)),
    SAGE("sage", "Обморожение", "Sage",
        "Ледяная стена в лёгких: сильно замедляет.",
        new Part.Fx(StatusEffects.SLOWNESS, 1)),
    SPIKE("spike", "Спайк-лихорадка", "Spike",
        "Таймер тикает! Руки не слушаются при копании.",
        new Part.Fx(StatusEffects.MINING_FATIGUE, 1)),
    KILLJOY("killjoy", "Вирус Килджой", "Killjoy",
        "Локдаун твоей удачи: всё идёт не по плану.",
        new Part.Fx(StatusEffects.UNLUCK, 2)),
    RAZE("raze", "Контузия", "Raze",
        "После Бум-бота ты светишься: тебя видно всем.",
        new Part.Fx(StatusEffects.GLOWING, 0));

    public final String id;
    public final String ru;
    public final String agent;
    public final String desc;
    public final Part.Fx[] fx;

    Disease(String id, String ru, String agent, String desc, Part.Fx... fx) {
        this.id = id;
        this.ru = ru;
        this.agent = agent;
        this.desc = desc;
        this.fx = fx;
    }

    public static Disease byId(String id) {
        for (Disease d : values()) if (d.id.equals(id)) return d;
        return null;
    }
}
