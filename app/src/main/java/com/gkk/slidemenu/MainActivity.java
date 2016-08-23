package com.gkk.slidemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    private SlideView menu;
    private View news;
    private View showSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initEvent() {
        news.setOnClickListener(this);
        showSelect.setOnClickListener(this);

    }

    private void initView() {
        menu = (SlideView) findViewById(R.id.sv);
        news = findViewById(R.id.news);
        showSelect = findViewById(R.id.showSelector);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.news:
                Toast.makeText(this, "123", Toast.LENGTH_SHORT).show();
                break;
            case R.id.showSelector:
                menu.toggle();
                break;
        }
    }
}
