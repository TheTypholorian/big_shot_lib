package net.typho.big_shot_lib.api.client.opengl.state

interface GlBufferTracker {
    var arrayBufferBinding: Int
    var elementArrayBufferBinding: Int
    var pixelPackBufferBinding: Int
    var pixelUnpackBufferBinding: Int
    var textureBufferBinding: Int
    var copyReadBufferBinding: Int
    var copyWriteBufferBinding: Int
    var drawIndirectBufferBinding: Int
    var dispatchIndirectBufferBinding: Int
    var queryBufferBinding: Int
    var transformFeedbackBufferBinding: Int
    var uniformBufferBinding: Int
    var atomicCounterBufferBinding: Int
    var shaderStorageBufferBinding: Int
}