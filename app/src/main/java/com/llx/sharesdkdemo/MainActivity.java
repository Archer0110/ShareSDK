package com.llx.sharesdkdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import onekeyshare.OnekeyShare;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private TextView text;
   // String userIcon;
    String userName;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        text = (TextView) findViewById(R.id.text);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String userIcon = (String) msg.obj;//发送空消息也行，只起到提示的作用
                BitmapUtils b = new BitmapUtils(MainActivity.this);
                b.display(img,userIcon);
                text.setText(userName);
            }
        };
    }
    public void myhaha(View v){
        showShare();
    }
    public void myhehe(View v){
        Platform weibo = ShareSDK.getPlatform(QQ.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weibo.setPlatformActionListener(new PlatformActionListener() {
            //用户是不合法的
            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                arg2.printStackTrace();
                Log.e("---","buhefa");
            }
            //用户是合法的
            @Override
            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                //输出所有授权信息
                arg0.getDb().exportData();
                String userIcon = arg0.getDb().getUserIcon();
                Log.e("----",""+arg0.getDb().getUserIcon());
                userName = arg0.getDb().getUserName();
                Log.e("----",""+arg0.getDb().getUserName());

                //发送
                Message message = mHandler.obtainMessage();
                message.obj = userIcon;
                mHandler.sendMessage(message);

                Log.e("----",""+arg0.getDb().exportData());
                Log.e("----",""+arg2);
            }
            //用户取消了授权调用的方法
            @Override
            public void onCancel(Platform arg0, int arg1) {
                Log.e("---","quxiao");
            }
        });
        //authorize与showUser单独调用一个即可
      //  weibo.authorize();//单独授权,OnComplete返回的hashmap是空的
        weibo.showUser(null);//授权并获取用户信息
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("我是李玲霞的分享");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://www.baidu.com");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是李玲霞的分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("https://www.baidu.com/img/bd_logo1.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://zhidao.baidu.com/question/507168352.html");
        // 启动分享GUI
        oks.show(this);
    }
}
