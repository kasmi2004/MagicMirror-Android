package cn.ilell.magicmirror;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cn.ilell.magicmirror.dialog.RulAndFeedbackDialog;
import cn.ilell.magicmirror.inter.PictureCallback;

public class MainActivity extends AppCompatActivity implements PictureCallback{
    //控制相机的fragment
    Camera2BasicFragment camera2BasicFragment;
    private RulAndFeedbackDialog rulAndFeedbackDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            camera2BasicFragment = new Camera2BasicFragment();
            camera2BasicFragment.setPictureCallback(MainActivity.this);
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, camera2BasicFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "默认样式的Toast", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void dealPicture(Image image) {
        //建立连接
//        Recognizer recognizer = new Recognizer();
        //发送图片，获取识别结果
//        String rul = recognizer.recognizeImg(image);
        //显示识别结果，获取用户反馈

        rulAndFeedbackDialog = new RulAndFeedbackDialog(MainActivity.this);
        rulAndFeedbackDialog.setTitle("提示");
        rulAndFeedbackDialog.setMessage("确定退出应用?");
        rulAndFeedbackDialog.setYesOnclickListener("正确", new RulAndFeedbackDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                Toast.makeText(MainActivity.this,"点击了--正确--按钮",Toast.LENGTH_LONG).show();
                //将用户反馈发送给服务器
//        recognizer.sendFeedback("yes");
                rulAndFeedbackDialog.dismiss();
            }
        });
        rulAndFeedbackDialog.setNoOnclickListener("错误", new RulAndFeedbackDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                Toast.makeText(MainActivity.this,"点击了--错误--按钮",Toast.LENGTH_LONG).show();
                //将用户反馈发送给服务器
//        recognizer.sendFeedback("no");
                rulAndFeedbackDialog.dismiss();
            }
        });
        rulAndFeedbackDialog.show();


    }

}
