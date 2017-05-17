package just.bikebyebye;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;//判断注册请求
    private boolean loginSuccess = false;//判断登陆成功与否
    private int height;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    //改变布局高度的失败尝试
  /* WindowManager wm = this.getWindowManager();
    int height = wm.getDefaultDisplay().getHeight();
    int width = wm.getDefaultDisplay().getWidth();
    View view = getLayoutInflater().inflate(R.layout.activity_login,null);
    LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.login_layout);
    LinearLayout.LayoutParams linearParams
            =new LinearLayout.LayoutParams(width,height);*/



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_login);

        //设置高度为全屏
       /* DisplayMetrics dm = getResources().getDisplayMetrics();
        height = dm.heightPixels;
        linearLayout = (LinearLayout)findViewById(R.id.login_layout);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)linearLayout.getLayoutParams();
        layoutParams.height =height;
        linearLayout.setLayoutParams(layoutParams);*/


        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        //Log.d(TAG, "Login");
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        /*if(email == "fengscox@gmial.com" && password == "422887137"){
            loginSuccess = true;
        }*/
        loginSuccess = true;
        if (!validate()) {
            onLoginFailed();
            return;
        }
        else if(loginSuccess){
            onLoginSuccess();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Light_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();//进度条显示
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        _loginButton.setEnabled(true);
        finish();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void onLoginFailed() {
        //错误提示
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        //输入清除
        /*_emailText.setText("");
        _passwordText.setText("");*/
        _loginButton.setEnabled(true);
    }
    //判断输入是否有效
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
//emial不为空，且要符合email的一般格式

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("输入有效邮件地址");
            valid = false;
        } else {
            _emailText.setError(null);
        }
//密码不能为空，且密码必须在8-16位
        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            _passwordText.setError("密码必须为8~16位字符");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}