package com.lgh.tapclick.myactivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lgh.advertising.tapclick.BuildConfig;
import com.lgh.advertising.tapclick.R;
import com.lgh.advertising.tapclick.databinding.ActivitySettingBinding;
import com.lgh.tapclick.mybean.LatestMessage;
import com.lgh.tapclick.myclass.DataDao;
import com.lgh.tapclick.myclass.MyApplication;
import com.lgh.tapclick.myfunction.MyUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {
    private Context context;
    private DataDao dataDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingBinding settingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(settingBinding.getRoot());
        context = getApplicationContext();
        dataDao = MyApplication.dataDao;

        settingBinding.settingAutoHideOnTaskList.setChecked(MyApplication.myAppConfig.autoHideOnTaskList);

        settingBinding.settingOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });

        settingBinding.settingPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(intent, "请选择应用市场"));
                } else {
                    Toast.makeText(context, "请到应用市场评分", Toast.LENGTH_SHORT).show();
                }
            }
        });

        settingBinding.settingAutoHideOnTaskList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.myAppConfig.autoHideOnTaskList = settingBinding.settingAutoHideOnTaskList.isChecked();
                MyUtils.setExcludeFromRecents(MyApplication.myAppConfig.autoHideOnTaskList);
                dataDao.updateMyAppConfig(MyApplication.myAppConfig);
            }
        });

        settingBinding.settingAuthorChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent github = new Intent(Intent.ACTION_VIEW);
                github.addCategory(Intent.CATEGORY_DEFAULT);
                github.addCategory(Intent.CATEGORY_BROWSABLE);
                github.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                github.setData(Uri.parse("https://github.com/LGH1996/TapClick"));
                startActivity(Intent.createChooser(github, "github"));
            }
        });

        settingBinding.settingGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openChat = new Intent(Intent.ACTION_VIEW, Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3Dw3oVSTyApiatRQNpBpZbdxWYVdK5f-08"));
                if (openChat.resolveActivity(getPackageManager()) != null) {
                    startActivity(openChat);
                } else {
                    Toast.makeText(context, "未安装QQ或TIM", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x01) {
            if (!MyApplication.myAppConfig.isVip) {
                MyApplication.myAppConfig.isVip = true;
                dataDao.updateMyAppConfig(MyApplication.myAppConfig);
                Toast.makeText(context, "水印已去除，重启后生效", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
