package net.typho.big_shot_lib.api.client.opengl.state

interface GlFlagTracker {
    var blendEnabled: Boolean
    var colorLogicOpEnabled: Boolean
    var cullFaceEnabled: Boolean
    var debugOutputEnabled: Boolean
    var debugOutputSynchronousEnabled: Boolean
    var depthClampEnabled: Boolean
    var depthEnabled: Boolean
    var ditherEnabled: Boolean
    var framebufferSRGBEnabled: Boolean
    var lineSmoothEnabled: Boolean
    var multisampleEnabled: Boolean
    var polygonOffsetEnabled: Boolean
    var polygonSmoothEnabled: Boolean
    var primitiveRestartEnabled: Boolean
    var primitiveRestartFixedIndexEnabled: Boolean
    var rasterizerDiscardEnabled: Boolean
    var sampleAlphaToCoverageEnabled: Boolean
    var sampleAlphaToOneEnabled: Boolean
    var sampleCoverageEnabled: Boolean
    var sampleShadingEnabled: Boolean
    var sampleMaskEnabled: Boolean
    var scissorEnabled: Boolean
    var stencilEnabled: Boolean
    var textureCubeMapSeamlessEnabled: Boolean
    var programPointSizeEnabled: Boolean
}