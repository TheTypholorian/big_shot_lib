package net.typho.big_shot_lib.api.annotation

@Target(AnnotationTarget.FUNCTION)
annotation class OnlyFor(
    val dist: Dist = Dist.BOTH,
    val dependencies: Array<DependencyVersionRange> = []
)
