package net.typho.big_shot_lib.state

import com.mojang.blaze3d.vertex.VertexFormat

interface IRenderSettingArray : IRenderSetting {
    fun bufferSize(): Int

    fun format(): VertexFormat

    fun mode(): VertexFormat.Mode

    fun sort(): Boolean
}