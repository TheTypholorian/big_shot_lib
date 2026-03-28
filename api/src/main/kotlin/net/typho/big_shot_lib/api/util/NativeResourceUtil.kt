package net.typho.big_shot_lib.api.util

import org.lwjgl.system.NativeResource

fun <R, A : NativeResource, B : NativeResource> A.alsoUse(b: B, func: (a: A, b: B) -> R): R {
    return use { a ->
        b.use { b ->
            func(a, b)
        }
    }
}

fun <R, A : NativeResource, B : NativeResource, C : NativeResource> A.alsoUse(
    b: B,
    c: C,
    func: (a: A, b: B, c: C) -> R
): R {
    return use { a ->
        b.use { b ->
            c.use { c ->
                func(a, b, c)
            }
        }
    }
}

fun <R, A : NativeResource, B : NativeResource, C : NativeResource, D : NativeResource> A.alsoUse(
    b: B,
    c: C,
    d: D,
    func: (a: A, b: B, c: C, d: D) -> R
): R {
    return use { a ->
        b.use { b ->
            c.use { c ->
                d.use { d ->
                    func(a, b, c, d)
                }
            }
        }
    }
}