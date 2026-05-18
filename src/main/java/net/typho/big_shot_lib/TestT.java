package net.typho.big_shot_lib;

import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.plugin.Namespace;

public class TestT {
    @Namespace("big_shot_lib")
    public static void testMethod(Identifier id) {
    }

    static {
        testMethod(Identifier.minecraft("test"));
    }
}
