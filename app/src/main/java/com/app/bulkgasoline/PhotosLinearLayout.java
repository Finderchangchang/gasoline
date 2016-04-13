package com.app.bulkgasoline;

import java.util.ArrayList;

import com.app.bulkgasoline.utils.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PhotosLinearLayout extends LinearLayout {

    private int MAX_COUNT = 2;
    private ArrayList<Bitmap> mBitmaps;
    private boolean mFixedHeader;
    private boolean mReadOnly;

    private Bitmap mHeader;
    private Bitmap mFirstImg;
    private OnClickListener mListener;

    public PhotosLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    class IndexImageView extends ImageView {

        private int index = -1;

        public IndexImageView(Context context) {
            super(context);

        }

        public IndexImageView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int i) {
            index = i;
        }
    }

    ;

    public void initLayout(Context context) {
        mFixedHeader = true;
        mBitmaps = new ArrayList<Bitmap>();

        int height = (int) getResources().getDimension(R.dimen.header_height);
        for (int i = 0; i < MAX_COUNT + 1; i++) {
            IndexImageView image = new IndexImageView(context);
            if (i == 0) {
                image.setVisibility(View.VISIBLE);
                image.setImageResource(R.drawable.default_header1);
            } else {
                image.setVisibility(View.GONE);
                image.setImageResource(R.drawable.icon_add_photo);
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    height, height);
            // lp.setMarginEnd(10);
            this.addView(image, lp);
        }

        setImageViewBitmap();
    }

    public Bitmap getHeader() {
        return mHeader;
    }

    public Bitmap GetFirstImg() {
        return mBitmaps.get(1);
    }

    public ArrayList<Bitmap> getBitmaps() {
        return mBitmaps;
    }

    private OnClickListener onClickPreview = new OnClickListener() {

        @Override
        public void onClick(View view) {

            Utils.setPreviewBitmap((Bitmap) view.getTag());
            Intent intent = new Intent();
            intent.setClass(getContext(), PreviewImageActivity.class);

            getContext().startActivity(intent);
        }

    };

    private OnLongClickListener onLongClickImage = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            if (!mReadOnly) {
                IndexImageView image = (IndexImageView) view;
                final int index = image.getIndex();
                if (index == -1 && mHeader == null)
                    return true;

                if (index >= 0 && index >= mBitmaps.size())
                    return true;

                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.text_to_delete_photos)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(R.string.text_ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        if (index == -1)
                                            setHeader(null);
                                        else {
                                            mBitmaps.remove(index);
                                            setImageViewBitmap();
                                        }
                                    }
                                })
                        .setNegativeButton(R.string.text_cancel, null).show();

                return true;
            }
            return false;
        }

    };

    private void setImageViewBitmap() {
        for (int i = -1; i < MAX_COUNT; i++) {
            IndexImageView image = (IndexImageView) this.getChildAt(i + 1);
            image.setTag(null);
            image.setIndex(i);
            image.setOnClickListener(null);
            image.setOnLongClickListener(null);

            if (i == -1) {
                if (mFixedHeader) {
                    image.setVisibility(View.VISIBLE);
                    if (mHeader == null)
                        image.setImageResource(R.drawable.default_header);
                    else
                        image.setImageBitmap(mHeader);

                    image.setTag(mHeader);
                } else {
                    image.setVisibility(View.GONE);
                }

                image.setOnClickListener(onClickPreview);
                image.setOnLongClickListener(onLongClickImage);
            } else {
                if (i < mBitmaps.size()) {
                    image.setVisibility(View.VISIBLE);
                    image.setImageBitmap(mBitmaps.get(i));
                    image.setOnClickListener(onClickPreview);
                    image.setOnLongClickListener(onLongClickImage);
                    image.setTag(mBitmaps.get(i));
                } else if (i == mBitmaps.size()) {
                    if (mReadOnly) {
                        image.setVisibility(View.GONE);
                    } else {
                        image.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.icon_add_photo);
                        image.setOnClickListener(mListener);
                    }
                } else {
                    image.setVisibility(View.GONE);
                    image.setImageBitmap(null);
                    image.setOnClickListener(null);
                }
            }
        }
    }

    public void setFixedHeader(boolean visible) {
        mFixedHeader = visible;
        setImageViewBitmap();
    }

    public void setClickPohoListener(OnClickListener listener) {
        mListener = listener;
        setImageViewBitmap();
    }

    public void setReadOnly(boolean readonly) {
        mReadOnly = readonly;
        setImageViewBitmap();
    }

    public void setHeader(Bitmap header) {
        mHeader = header;
        setImageViewBitmap();
    }


    public void setPhotos(ArrayList<Bitmap> bitmaps) {
        mBitmaps.clear();
        for (int i = 0; i < bitmaps.size() && i < MAX_COUNT; i++) {
            mBitmaps.add(bitmaps.get(i));
        }

        setImageViewBitmap();
    }

    public void addPhoto(Bitmap bitmap) {
        if (mBitmaps.size() >= MAX_COUNT)
            return;

        mBitmaps.add(bitmap);
        setImageViewBitmap();
    }

    public void clearPhotos() {
        mBitmaps = new ArrayList<Bitmap>();
        setImageViewBitmap();
    }
}
