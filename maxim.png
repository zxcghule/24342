package com.maxim.bodymarket;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;

/** Части тела, которые можно продать Максиму. */
public enum Part {
    LEFT_EYE("left_eye", "Левый глаз", 10,
        "Ты не можешь видеть как раньше: в глазах темнеет.",
        "Не грусти, у Совы вместо глаза вообще дрон!",
        new Fx(StatusEffects.DARKNESS, 0)),
    RIGHT_EYE("right_eye", "Правый глаз", 10,
        "Ты не можешь смотреть вдаль: в глазах темнеет.",
        "Продашь оба — будешь играть за Рейну вслепую.",
        new Fx(StatusEffects.DARKNESS, 0)),
    LEFT_EAR("left_ear", "Левое ухо", 6,
        "Ты не можешь держать равновесие: вестибулярка шалит.",
        "Зато не услышишь, как напарник орёт «ты где?!»",
        new Fx(StatusEffects.SLOWNESS, 0)),
    RIGHT_EAR("right_ear", "Правое ухо", 6,
        "Ты не можешь ходить прямо: шатает в сторону.",
        "Теперь можно играть без звука шагов, как профи.",
        new Fx(StatusEffects.SLOWNESS, 0)),
    NOSE("nose", "Нос", 5,
        "Ты не чувствуешь запах еды: она больше не радует, голод быстрее.",
        "Зато твои носки больше не пахнут. Для тебя.",
        new Fx(StatusEffects.HUNGER, 0)),
    TONGUE("tongue", "Язык", 8,
        "Ты не можешь писать в чат: языка-то нет!",
        "Наконец-то тишина. Я обожаю молчаливых клиентов.",
        new Fx(StatusEffects.UNLUCK, 0)),
    TOOTH("tooth", "Зуб", 4,
        "Ты не можешь улыбаться: удача от тебя отвернулась.",
        "Зато ультуешь — и никто не видит, что ты без зуба.",
        new Fx(StatusEffects.UNLUCK, 0)),
    HAIR("hair", "Волосы", 3,
        "БАФФ: аэродинамика! Бежишь быстрее.",
        "Добро пожаловать в клуб лысых. Я президент.",
        new Fx(StatusEffects.SPEED, 0)),
    LEFT_ARM("left_arm", "Левая рука", 15,
        "Ты не можешь быстро копать: одна рука не тянет.",
        "Зато одной рукой тоже можно держать Вандал!",
        new Fx(StatusEffects.MINING_FATIGUE, 0)),
    RIGHT_ARM("right_arm", "Правая рука", 15,
        "Ты не можешь бить как раньше: удар стал слабее.",
        "Ничего, мышкой кликать ещё получится.",
        new Fx(StatusEffects.WEAKNESS, 0)),
    LEFT_PINKY("left_pinky", "Левый мизинец", 3,
        "БАФФ: меньше пальцев — быстрее копаешь!",
        "Мизинец всё равно нужен только чтобы пить чай красиво.",
        new Fx(StatusEffects.HASTE, 0)),
    RIGHT_THUMB("right_thumb", "Правый большой палец", 7,
        "Ты не можешь крепко схватить: удар слабее.",
        "Лайки теперь ставить нечем. Минус один хейтер... то есть плюс.",
        new Fx(StatusEffects.WEAKNESS, 0)),
    LEFT_LEG("left_leg", "Левая нога", 12,
        "Ты не можешь быстро бегать: хромаешь.",
        "Зато не будешь прыгать как Джетт и падать.",
        new Fx(StatusEffects.SLOWNESS, 0)),
    RIGHT_LEG("right_leg", "Правая нога", 12,
        "Ты не можешь бегать быстро: хромаешь на другую ногу.",
        "Продал обе? Тогда Максим довезёт... в тележке. За деньги.",
        new Fx(StatusEffects.SLOWNESS, 0)),
    TOE("toe", "Мизинец на ноге", 2,
        "БАФФ: ты легче, прыгаешь выше!",
        "Об угол стола ты его бил чаще всего. Правильное решение.",
        new Fx(StatusEffects.JUMP_BOOST, 0)),
    RIB("rib", "Лишнее ребро", 6,
        "БАФФ: бежишь быстрее! Но бьёшь слабее.",
        "Адам тоже так начинал. И где он теперь?",
        new Fx(StatusEffects.SPEED, 0), new Fx(StatusEffects.WEAKNESS, 0)),
    LEFT_KIDNEY("left_kidney", "Левая почка", 20,
        "Ты не можешь долго терпеть голод: есть хочется быстрее.",
        "Одной почки хватит. Сейдж если что подлечит.",
        new Fx(StatusEffects.HUNGER, 0)),
    RIGHT_KIDNEY("right_kidney", "Правая почка", 22,
        "Ты не можешь нормально работать: слабость и голод.",
        "Обе почки продал? Уважаю. Пей воду. Нет, не пей.",
        new Fx(StatusEffects.HUNGER, 1), new Fx(StatusEffects.WEAKNESS, 0)),
    SPLEEN("spleen", "Селезёнка", 8,
        "БАФФ: бегаешь быстрее! Но бьёшь слабее.",
        "Селезёнка была как гаджет у Кэй/О: вроде есть, а пользы ноль.",
        new Fx(StatusEffects.SPEED, 0), new Fx(StatusEffects.WEAKNESS, 0)),
    GALLBLADDER("gallbladder", "Желчный пузырь", 9,
        "БАФФ: больше энергии! Но голод быстрее.",
        "Жирное теперь есть нельзя. Хотя тебе и раньше нельзя было.",
        new Fx(StatusEffects.HUNGER, 0), new Fx(StatusEffects.HASTE, 0)),
    APPENDIX("appendix", "Аппендикс", 4,
        "БАФФ: удача! Лишнее убрали.",
        "Бесполезный орган. Как мой напарник по Ранкеду.",
        new Fx(StatusEffects.LUCK, 0)),
    LIVER("liver", "Часть печени", 18,
        "Ты не можешь выдерживать нагрузку: слабость.",
        "Печень растёт заново! Значит, я куплю её ещё раз. Шучу. Или нет.",
        new Fx(StatusEffects.WEAKNESS, 0)),
    LUNG("lung", "Лёгкое", 25,
        "Ты не можешь бежать и копать: задыхаешься.",
        "Одним лёгким тоже можно орать «ПЛЕЙНТ!».",
        new Fx(StatusEffects.SLOWNESS, 1), new Fx(StatusEffects.MINING_FATIGUE, 0)),
    STOMACH("stomach", "Часть желудка", 14,
        "Ты не можешь наесться: голод идёт быстрее.",
        "Зато экономия на еде. Теоретически.",
        new Fx(StatusEffects.HUNGER, 1)),
    INTESTINE("intestine", "Кусок кишечника", 10,
        "Ты не можешь нормально переваривать: голод быстрее.",
        "Не спрашивай, зачем мне это. Просто не спрашивай.",
        new Fx(StatusEffects.HUNGER, 0)),
    THYROID("thyroid", "Щитовидка", 12,
        "Ты не можешь шустро двигаться: обмен веществ тормозит.",
        "Теперь ты спокойный, как Сейдж после хила.",
        new Fx(StatusEffects.SLOWNESS, 0), new Fx(StatusEffects.HUNGER, 0)),
    BLOOD("blood", "Пол-литра крови", 7,
        "Ты не можешь бегать и бить в полную силу: слабость.",
        "Донорам дают печеньки. Тебе — изумруды. Выгодно!",
        new Fx(StatusEffects.WEAKNESS, 0), new Fx(StatusEffects.SLOWNESS, 0)),
    BRAIN("brain", "Кусочек мозга", 30,
        "БАФФ: меньше думаешь — быстрее бегаешь! Но удача на нуле.",
        "Тебе он всё равно не пригождался. Без обид.",
        new Fx(StatusEffects.SPEED, 0), new Fx(StatusEffects.UNLUCK, 2), new Fx(StatusEffects.MINING_FATIGUE, 0));

    public final String id;
    public final String ru;
    public final int price;
    public final String msg;
    public final String joke;
    public final Fx[] fx;

    Part(String id, String ru, int price, String msg, String joke, Fx... fx) {
        this.id = id;
        this.ru = ru;
        this.price = price;
        this.msg = msg;
        this.joke = joke;
        this.fx = fx;
    }

    public static Part byId(String id) {
        for (Part p : values()) if (p.id.equals(id)) return p;
        return null;
    }

    public record Fx(StatusEffect effect, int amp) {}
}
