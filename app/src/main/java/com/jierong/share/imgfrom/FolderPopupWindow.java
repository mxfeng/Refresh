package com.jierong.share.imgfrom;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import com.jierong.share.R;
import com.jierong.share.imgfrom.model.FolderListContent;

/**
 * Created by zfdang on 2016-4-16.
 */
public class FolderPopupWindow extends PopupWindow {
    private static final String TAG = "FolderPopupWindow";
    private Context mContext;
    private View conentView;
    private RecyclerView recyclerView;
    private OnFolderListener mListener = null;

    // http://stackoverflow.com/questions/23464232/how-would-you-create-a-popover-view-in-android-like-facebook-comments
    public void initPopupWindow(final Activity context) {
        mContext = context;
        if (context instanceof OnFolderListener) {
            mListener = (OnFolderListener) context;
        } else {
            Log.d(TAG, "initPopupWindow: " + "context does not implement OnFolderListener");
        }

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = layoutInflater.inflate(R.layout.popup_folder_recyclerview, null, false);

        View rview = conentView.findViewById(R.id.folder_recyclerview);
        // Set the adapter
        if (rview instanceof RecyclerView) {
            recyclerView = (RecyclerView) rview;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL, 0));
            recyclerView.setAdapter(new FolderRecyclerViewAdapter(context, FolderListContent.FOLDERS, mListener));
        }

        // get device size
        Display display = context.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        this.setContentView(conentView);
        this.setWidth((int) (size.x * 0.9));
        this.setHeight((int) (size.y * 0.618));
        // http://stackoverflow.com/questions/12232724/popupwindow-dismiss-when-clicked-outside
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.if_popup_background));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimationPreview);
    }

}
