package net.typho.big_shot_lib.api.client.opengl.state

interface GlTextureTracker {
    var texture1DBinding: Int
    var texture2DBinding: Int
    var texture3DBinding: Int
    var texture1DArrayBinding: Int
    var texture2DArrayBinding: Int
    var textureRectangleBinding: Int
    var textureCubeMapBinding: Int
    var textureCubeMapArrayBinding: Int
    var texture2DMultisampleBinding: Int
    var texture2DMultisampleArrayBinding: Int
}