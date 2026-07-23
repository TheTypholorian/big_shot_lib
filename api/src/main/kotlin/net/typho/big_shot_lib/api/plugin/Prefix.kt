package net.typho.big_shot_lib.api.plugin

import kotlin.reflect.KClass

/**
 * Adds a prefix to field and method names post-compile, which is useful for injected interfaces to prevent compatibility problems.
 * The prefix should be your mod id, since that's already a unique string.
 * You can also put the annotation on a class to apply to all fields and methods inside that class.
 * Applies to overridden methods.
 *
 * Details:
 * - Given the prefix `abc` and the name `def`, the new name will be `abc$def`.
 * - Overriding a method without this annotation and adding your own prefix annotation will make your method no longer override the parent method. Note that in this case, overrides of your override will have your prefix applied.
 * - The processor has full compat between different jars, so you don't need to worry about that.
 * - The processor caches a class hierarchy and all instances of this prefix inside dependency jars in `META-INF/big_shot_lib/prefix_info.json`. This is calculated in development, so your jar will not have this file. Adding this file to your jar does nothing and will be overridden.
 *
 * **WARNING**: If you are injecting an interface and you're using this annotation on a method that exists in the target class, there can be some edge cases that cause problems. Look at this example:
 * ```java
 * // Your code
 * public interface MyInterface {
 *     @Prefix("my_mod")
 *     default void doSomething() {
 *     }
 * }
 *
 * public class MySubclass extends TargetClass {
 *     @Override
 *     public void doSomething() {
 *     }
 * }
 *
 * // Dependency code
 * public class TargetClass implements MyInterface { // MyInterface has been injected via mixin + big shot lib
 *     public void doSomething() {
 *     }
 * }
 * ```
 * This will cause problems.
 * After compile, the `doSomething` methods in `MyInterface` and `MySubclass` will have the prefix `my_mod`.
 * This means that the method in `MySubclass` will not be overriding the method in `TargetClass`.
 *
 *
 * To fix this, you can specify classes where the prefix is not inherited in using the `ignoreSubclasses` field of this annotation (this also applies to the subclasses of those classes).
 *
 *
 * In the case where you would want the method in `MySubclass` to override the `MyInterface` method but not the `TargetClass` method, you can simply add another `@Prefix` annotation to the method in `MySubclass`.
 * The processor runs bottom-up, so this will work.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
annotation class Prefix(
    val value: String,
    val ignoreSubclasses: Array<KClass<*>> = []
)