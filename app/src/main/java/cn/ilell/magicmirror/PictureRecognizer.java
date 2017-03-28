package cn.ilell.magicmirror;

import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * 图片识别和结果反馈
 * Created by lhc35 on 2017/3/22.
 */

public class PictureRecognizer{
    private Socket mSock;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    //    private Image mImage;

    public PictureRecognizer() {
        try{
            mSock = new Socket("192.168.0.196",3538);
            // 获取Socket的Stream对象用于读取、发送数据
            dataInputStream = new DataInputStream(mSock.getInputStream());
            InputStreamReader inSR = new InputStreamReader(dataInputStream, "UTF-8");
            bufferedReader = new BufferedReader(inSR);

            dataOutputStream = new DataOutputStream(mSock.getOutputStream());
            OutputStreamWriter outSW = new OutputStreamWriter(dataOutputStream, "UTF-8");
            bufferedWriter = new BufferedWriter(outSW);
        } catch (UnknownHostException e) {
            Log.i("abc", e.toString());
            Log.i("abc", e.getMessage());
        } catch (IOException e) {
            Log.i("abc", e.toString());
            Log.i("abc", e.getMessage());
        }

    }

    /**
     * 识别图片
     * @param imageBytes
     * @return
     */
    public String recognizeImg(byte[] imageBytes) {
        String rul = "";
        //发送要识别的图片
        try{
            String file_size = String.format("%010d", imageBytes.length);
            //发送图片大小
            bufferedWriter.write(file_size);
            bufferedWriter.flush();
            //发送图片
            dataOutputStream.write(imageBytes);
            dataOutputStream.flush();
            //接收识别结果
            rul = bufferedReader.readLine();
        } catch (Exception e) {
            Log.i("abc", e.toString());
            Log.i("abc", e.getMessage());
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
            bufferedWriter.write(feedback);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    protected void finalize() throws Throwable {
        mSock.close();
        super.finalize();
    }
}
