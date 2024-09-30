package com.alphatz.adek;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    ViewPager mSlideViewPage;
    LinearLayout mDotlayout;
    Button mbutton;
    TextView[] dots;
    viewPageAdapter viewPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mSlideViewPage = (ViewPager) findViewById(R.id.slideviewer);
        mDotlayout = (LinearLayout) findViewById(R.id.indicator_layout);
        mbutton = (Button) findViewById(R.id.btn_next);

        viewPageAdapter = new viewPageAdapter(this);
        mSlideViewPage.setAdapter(viewPageAdapter);

        setUpindicator(0);
        mbutton.setVisibility(View.INVISIBLE);
        mSlideViewPage.addOnPageChangeListener(viewListener);
    }

    public void setUpindicator(int position){
        dots = new TextView[4];
        mDotlayout.removeAllViews();

        for(int i = 0; i<dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive, getApplicationContext().getTheme()));
            mDotlayout.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.hijau_tua, getApplicationContext().getTheme()));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if(position == 3){
                mbutton.setVisibility(View.VISIBLE);
                mbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else{
                mbutton.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getItem(int i){
        return mSlideViewPage.getCurrentItem() +i;

    }
}