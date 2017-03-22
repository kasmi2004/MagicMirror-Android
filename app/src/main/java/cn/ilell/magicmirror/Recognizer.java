package cn.ilell.magicmirror;

import android.media.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * 图片识别和结果反馈
 * Created by lhc35 on 2017/3/22.
 */

public class Recognizer {
    private Socket mSock;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    //    private Image mImage;

    public Recognizer() {
        try{
            mSock = new Socket("127.0.0.1",3538);
            // 获取Socket的Stream对象用于读取、发送数据
            outputStream = mSock.getOutputStream();
            inputStream = mSock.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);
            objectOutputStream = new ObjectOutputStream(outputStream);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 识别图片
     * @param image
     * @return
     */
    public String recognizeImg(Image image) {
//        mImage = image;
        String rul = "";
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        //发送要识别的图片
        try{
            outputStream.write(bytes);
            outputStream.flush();
            //接收识别结果
            rul = (String) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rul;
    }

    /**
     * 发送对识别结果的反馈
     * @param feedback
     * @return
     */
    public boolean sendFeedback(String feedback){
        try {
            objectOutputStream.write(feedback.getBytes());
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
