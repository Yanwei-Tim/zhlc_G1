package com.zhlt.g1.util;
import com.zhlt.g1.camera.bean.CameraPhoto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PersonEncoder extends MessageToByteEncoder<CameraPhoto>  {

    @Override
    protected void encode(ChannelHandlerContext ctx, CameraPhoto msg, ByteBuf out) throws Exception {
        out.writeBytes(ByteObjConverter.ObjectToByte(msg));
    }
}