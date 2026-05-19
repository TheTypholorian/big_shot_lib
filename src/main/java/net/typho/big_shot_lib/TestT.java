package net.typho.big_shot_lib;

import net.minecraft.resources.Identifier;
import net.typho.big_shot_lib.api.plugin.Environment;
import net.typho.big_shot_lib.api.plugin.Namespace;
import net.typho.big_shot_lib.api.plugin.OnlyIn;

@OnlyIn(Environment.CLIENT)
public class TestT {
    @Namespace("big_shot_lib")
    public static void testMethod(TestInterface id) {
    }

    static {
        testMethod(Identifier.minecraft("test"));
    }
}
