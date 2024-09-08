package com.lgh.tapclick.myactivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lgh.advertising.tapclick.R;
import com.lgh.tapclick.mybean.MyAppConfig;
import com.lgh.tapclick.myclass.DataDaoHelper;

import io.reactivex.rxjava3.annotations.NonNull;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DataDaoHelper.getMyAppConfig(new DataDaoHelper.AbstractSimpleObserver<MyAppConfig>() {
            @Override
            public void onNext(@NonNull MyAppConfig config) {
                if (!config.isVip) {
                    View noVip = findViewById(R.id.no_vip);
                    if (noVip == null) {
                        return;
                    }
                    noVip.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
