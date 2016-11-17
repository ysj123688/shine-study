package com.bazinga.shine.IO;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件copy
 * @author liguolin
 * 描述：
 * 时间  2016年11月17日
 */
public class FileCopy {
    
    public static void fileCopy(String source,String target) throws IOException{
        
        InputStream in = new FileInputStream(source);
        OutputStream out =new FileOutputStream(target);
        byte[] buffer = new byte[4096];
        int byteToRead;
        while((byteToRead = in.read(buffer)) != -1){
            out.write(buffer, 0, byteToRead);
        }
        
    }
    
    public static void fileCopyNio(String source,String target) throws IOException{
        FileInputStream in = new FileInputStream(source);
        FileOutputStream out = new FileOutputStream(target);
        FileChannel inchannel = in.getChannel();
        FileChannel outchannel = out.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while(inchannel.read(buffer) != -1){
            buffer.flip();
            outchannel.write(buffer);
            buffer.clear();
        }
        
    }

}
