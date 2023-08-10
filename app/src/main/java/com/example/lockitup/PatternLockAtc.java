package com.example.lockitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.example.lockitup.Model.Password;
import com.example.lockitup.services.BackgroundManager;
import com.example.lockitup.utils.Utils;
import com.shuhart.stepview.StepView;

import java.util.List;

public class PatternLockAtc extends AppCompatActivity {

    StepView stepView;
    LinearLayout normalLayout;
    TextView status_password;
    RelativeLayout relativeLayout;

    Password utilsPassword;
    String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_lock_atc);

        BackgroundManager.getInstance().init(this).startService();

        initIconeApp();
        initLayout();

        initPatternListener();
    }

    private void initIconeApp() {
        if (getIntent().getStringExtra("broadcast_reciever") != null){

            ImageView icone = findViewById(R.id.icone_app);
            String current_app = new Utils(this).getLastApp();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = getPackageManager().getApplicationInfo(current_app,0);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }

            icone.setImageDrawable(applicationInfo.loadIcon(getPackageManager()));



        }

    }

    private void initPatternListener()
    {
        PatternLockView patternLockView = findViewById(R.id.pattern_view);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {

                String pwd = PatternLockUtils.patternToString(patternLockView,pattern);

                if (pwd.length() < 4){
                    status_password.setText(utilsPassword.SHEMA_FAILED);
                    patternLockView.clearPattern();
                    return;
                }

                if(utilsPassword.getpassword() == null){
                    if (utilsPassword.isFirstStep())
                    {
                       userPassword = pwd;
                       utilsPassword.setFirstStep(false);
                       status_password.setText(utilsPassword.STATUS_NEXT_STEP);
                       stepView.go(1,true);
                    }
                    else {
                        if (userPassword.equals(pwd)) {
                             utilsPassword.setPassword(userPassword);
                             status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                             stepView.done(true);

                             startAct();
                        }else{
                            status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                        }
                    }
                }
                else {
                          if (utilsPassword.isCorrect(pwd))
                          {
                              status_password.setText(utilsPassword.STATUS_PASSWORD_CORRECT);
                              startAct();
                          }
                          else {
                              status_password.setText(utilsPassword.STATUS_PASSWORD_INCORRECT);
                          }
                }

                patternLockView.clearPattern();

            }

            @Override
            public void onCleared() {

            }
        });
    }

    private void startAct() {

        if (getIntent().getStringExtra("broadcast_reciever") ==null){
            startActivity(new Intent(this,MainActivity.class));
        }
        finish();
    }

    private void initLayout() {
        stepView = findViewById(R.id.step_view);
        normalLayout = findViewById(R.id.normal_layout);
        relativeLayout = findViewById(R.id.root);
        status_password = findViewById(R.id.status_password);

        utilsPassword = new Password(this);

        status_password.setText(utilsPassword.STATUS_FIRST_STEP);

        if (utilsPassword.getpassword() == null)
        {
                normalLayout.setVisibility(View.GONE);
                stepView.setVisibility(View.VISIBLE);
                stepView.setStepsNumber(2);
                stepView.go(0,true);

        }else {
            normalLayout.setVisibility(View.VISIBLE);
            stepView.setVisibility(View.GONE);

            int backColor = ResourcesCompat.getColor(getResources(),R.color.blue,null);
            relativeLayout.setBackgroundColor(backColor);
            status_password.setTextColor(Color.WHITE);

        }
    }

    public void onBackPressed(){

        if(utilsPassword.getpassword() == null && !utilsPassword.isFirstStep()) {

            stepView.go(0, true);
            utilsPassword.setFirstStep(true);
            status_password.setText(utilsPassword.STATUS_FIRST_STEP);

        }else{
            startCurrentHomePackage();
            finish();
            super.onBackPressed();
        }
    }

    private void startCurrentHomePackage() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = getPackageManager().resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);

        ActivityInfo activityInfo = resolveInfo.activityInfo;
        ComponentName componentName = new ComponentName(activityInfo.applicationInfo.packageName,activityInfo.name);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);

        new Utils(this).clearLastApp();

    }
}