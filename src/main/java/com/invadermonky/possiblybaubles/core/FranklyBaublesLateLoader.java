package com.invadermonky.possiblybaubles.core;

import com.google.common.collect.ImmutableMap;
import com.invadermonky.possiblybaubles.handlers.ConfigHandlerPB;
import zone.rong.mixinbooter.Context;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

public class FranklyBaublesLateLoader implements ILateMixinLoader {
    public static final ImmutableMap<String, Predicate<Context>> MIXINS = ImmutableMap.copyOf(new HashMap<String, Predicate<Context>>() {{
        put("mixins/mixins.possiblybaubles.battery.json", c -> ConfigHandlerPB.TOGGLES.battery);
        put("mixins/mixins.possiblybaubles.goggles.json", c -> ConfigHandlerPB.TOGGLES.goggles);
        put("mixins/mixins.possiblybaubles.growthring.json", c -> ConfigHandlerPB.TOGGLES.growthRing);
        put("mixins/mixins.possiblybaubles.liquidbanningring.json", c -> ConfigHandlerPB.TOGGLES.liquidBanningRing);
        put("mixins/mixins.possiblybaubles.magnetring.json", c -> ConfigHandlerPB.TOGGLES.magnetRing);
        put("mixins/mixins.possiblybaubles.potionring.json", c -> ConfigHandlerPB.TOGGLES.potionRing);
        put("mixins/mixins.possiblybaubles.sacks.json", c -> ConfigHandlerPB.TOGGLES.sacks);
        put("mixins/mixins.possiblybaubles.wingsofthebat.json", c -> ConfigHandlerPB.TOGGLES.wingsOfTheBat);
    }});

    @Override
    public List<String> getMixinConfigs() {
        return MIXINS.keySet().asList();
    }

    @Override
    public boolean shouldMixinConfigQueue(Context context) {
        return !MIXINS.containsKey(context.mixinConfig()) || MIXINS.get(context.mixinConfig()).test(context);
    }
}
