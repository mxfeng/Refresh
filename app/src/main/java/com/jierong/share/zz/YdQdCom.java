package com.jierong.share.zz;

import android.view.LayoutInflater;
import android.view.View;
import com.jierong.share.R;

public class YdQdCom implements Component {

    @Override
    public View getView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.view_yd_qiandao, null);
        return view;
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_BOTTOM;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_END;
    }

    @Override
    public int getXOffset() {
        return -20;
    }

    @Override
    public int getYOffset() {
        return 5;
    }
}
