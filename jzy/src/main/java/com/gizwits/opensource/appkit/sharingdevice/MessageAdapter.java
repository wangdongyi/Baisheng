package com.gizwits.opensource.appkit.sharingdevice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gizwits.opensource.appkit.R;

import java.util.ArrayList;

import wdy.business.util.CodeUtil;

/**
 * 作者：王东一
 * 创建时间：2018/6/20.
 */
public class MessageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<String> list = new ArrayList<>();
    private RequestOptions options;

    public MessageAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
        options = new RequestOptions()
                .centerCrop().dontAnimate()
                .placeholder(R.mipmap.pic)
                .error(R.mipmap.default_picture)
                .priority(Priority.HIGH);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        ViewHolder viewHolder = null;
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_message, parent, false);
        viewHolder = new ViewHolder(view, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final String mac = list.get(position);
        Glide.with(viewHolder.zmt_item_imageView).
                load("http://www.kongtiaoguanjia.com/" + mac).apply(options).
                into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                        //计算 修改ImageView的高
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.zmt_item_imageView.getLayoutParams();
                        int w = 0;
                        int h = 0;
                        w = resource.getIntrinsicWidth();
                        h = resource.getIntrinsicHeight();
                        if (w == 0 || h == 0) {
                            params.height = (CodeUtil.getScreenWidth(mContext)) * 128 / 128;
                            viewHolder.zmt_item_imageView.setLayoutParams(params);
                            viewHolder.zmt_item_imageView.setImageResource(R.mipmap.default_picture);
                        } else {
                            params.height = (CodeUtil.getScreenWidth(mContext)) * h / w;
                            viewHolder.zmt_item_imageView.setLayoutParams(params);
                            viewHolder.zmt_item_imageView.setImageDrawable(resource);
                        }
                        viewHolder.zmt_item_imageView.setLayoutParams(params);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public ImageView zmt_item_imageView;

        public ViewHolder(View rootView, int type) {
            super(rootView);
            this.rootView = rootView;
            this.zmt_item_imageView = (ImageView) rootView.findViewById(R.id.imageView);
        }

    }
}
