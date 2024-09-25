package com.alphatz.adek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class viewPageAdapter extends PagerAdapter {

    Context context;

    int img[] = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4
    };

    int heading[] ={
            R.string.heading1_text,
            R.string.heading2_text,
            R.string.heading3_text,
            R.string.heading4_text
    };

    int desc[] ={
            R.string.desc1_text,
            R.string.desc2_text,
            R.string.desc3_text,
            R.string.desc4_text
    };

    public viewPageAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout,container,false);

        ImageView slidetitleimage = (ImageView) view.findViewById(R.id.containerImg);
        TextView slideHeading = (TextView) view.findViewById(R.id.textTitle);
        TextView slidedec = (TextView) view.findViewById(R.id.desc);

        slidetitleimage.setImageResource(img[position]);
        slideHeading.setText(heading[position]);
        slidedec.setText(desc[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
