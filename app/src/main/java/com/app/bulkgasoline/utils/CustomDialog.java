package com.app.bulkgasoline.utils;

import java.util.ArrayList;

import com.app.bulkgasoline.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String message_top;// 顶部消息
        private String message_bottom;// 底部消息
        private Boolean message_result;// 是否加红
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private int imgView;
        private Bitmap bit1;
        private ArrayList<Bitmap> bit2;
        private Boolean result;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private DialogInterface.OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message_top, String message_bottom,
                                  Boolean message_result) {
            this.message_top = message_top;
            this.message_bottom = message_bottom;
            this.message_result = message_result;
            return this;
        }

        public Builder setView(ArrayList<Bitmap> bit2,
                               Boolean result) {
            this.bit2 = bit2;
            this.result = result;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message_top = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context,
                    R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_not_normal_layout, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the confirm button
            if (positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.positiveButton))
                        .setText(positiveButtonText);
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.negativeButton))
                        .setText(negativeButtonText);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            TextView msg_top = (TextView) layout.findViewById(R.id.message_top);
            TextView msg_bottom = (TextView) layout
                    .findViewById(R.id.message_bottom);
            LinearLayout cnt_msg_bottom = (LinearLayout) layout
                    .findViewById(R.id.cnt_msg_bottom);
            LinearLayout img2 = (LinearLayout) layout.findViewById(R.id.img);
            ImageView image1 = (ImageView) layout
                    .findViewById(R.id.imageview_header1);
            ImageView image2 = (ImageView) layout
                    .findViewById(R.id.imageview_header2);
            if (message_top != null) {
                if (result) {// 用图片
                    img2.setVisibility(View.VISIBLE);
                    image1.setVisibility(View.VISIBLE);
                    image2.setVisibility(View.VISIBLE);
                    if (bit2.size() > 0) {
                        image1.setImageBitmap(bit2.get(0));
                    } else {
                        image1.setImageResource(R.drawable.no_image);
                    }
                    if (bit2.size() > 1) {
                        image2.setImageBitmap(bit2.get(1));
                    } else {
                        image2.setImageResource(R.drawable.no_image);
                    }
                } else {
                    img2.setVisibility(View.GONE);
                    cnt_msg_bottom.setVisibility(View.GONE);
                }
                msg_top.setVisibility(View.VISIBLE);
                msg_top.setText(message_top);
                if (message_bottom != "") {
                    cnt_msg_bottom.setVisibility(View.VISIBLE);
                    msg_bottom.setText(message_bottom);
                }
                if (message_result) {
                    msg_top.setTextColor(android.graphics.Color.RED);
                    msg_bottom.setTextColor(android.graphics.Color.RED);
                }
            } else if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new LayoutParams(LayoutParams.FILL_PARENT,
                                LayoutParams.FILL_PARENT));
            }
            dialog.setContentView(layout);
            return dialog;
        }

    }
}
