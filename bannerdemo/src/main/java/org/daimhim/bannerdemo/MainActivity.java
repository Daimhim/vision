package org.daimhim.bannerdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.daimhim.banner.Banner;
import org.daimhim.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Banner lBanner = (Banner) findViewById(R.id.b_banner);
        lBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(context).load(path).into(imageView);
            }
        });
        List<String> lStrings = new ArrayList<>();
        lStrings.add("http://pic1.win4000.com/wallpaper/8/58f5a79da3b60.jpg");
        lStrings.add("http://himg2.huanqiu.com/attachment2010/2017/0503/14/47/20170503024755104.jpg");
        lStrings.add("https://image.tmdb.org/t/p/original/wdxWpq6lzgWxH8N8YgqQmLPvgn5.jpg");
        lStrings.add("http://image.jisuxz.com/desktop/1834/jisuxz_wangzhe_yadianna_1_05.jpg");
        lStrings.add("http://i.imgur.com/R0ySZg5.jpg");
        lStrings.add("http://pic1.win4000.com/wallpaper/7/5902e207bc663.jpg");
        lStrings.add("https://cn.best-wallpaper.net/wallpaper/1920x1080/1608/Gal-Gadot-as-Wonder-Woman_1920x1080.jpg");
        lBanner.update(lStrings);
        lBanner.setBackgroundResource(R.color.colorAccent);
    }
}
