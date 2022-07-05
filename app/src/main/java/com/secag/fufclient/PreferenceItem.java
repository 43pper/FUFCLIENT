package com.secag.fufclient;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreferenceItem extends LinearLayout {

    boolean isChecked = false;
    ItemType itemType;

    public enum ItemType {
        PREFERENCE,
        LOCATION,
        BLOCKED_PREFERENCE
    }

    public PreferenceItem(Context context, AttributeSet attrs, ItemType itemType, String emoji_symbol, String title) {
        super(context, attrs);

        this.itemType = itemType;

        if (itemType == ItemType.LOCATION) {
            emoji_symbol = "📍";
        }

        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundResource(R.drawable.preference_item);
        LinearLayout.LayoutParams wrap = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wrap.setMargins(0, toPixels(5), 0, toPixels(5));
        setLayoutParams(wrap);
        setPadding(0, toPixels(3), toPixels(15), toPixels(3));

        wrap = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView emoji = new TextView(context);
        emoji.setText(emoji_symbol);
        emoji.setLayoutParams(wrap);
        emoji.setGravity(Gravity.CENTER_VERTICAL);
        emoji.setPadding(toPixels(15), toPixels(3), toPixels(10), toPixels(3));
        emoji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        addView(emoji);

        TextView text = new TextView(context);
        text.setText(title);
        text.setLayoutParams(wrap);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setTypeface(Typeface.DEFAULT_BOLD);
        addView(text);
    }

    public void toggleChecked(){
        isChecked = !isChecked;
        if (isChecked) {
            int background = R.drawable.preference_item_checked;
            if (itemType == ItemType.BLOCKED_PREFERENCE) {
                background = R.drawable.preference_item_blocked_checked;
            }
            setBackgroundResource(background);
        } else {
            setBackgroundResource(R.drawable.preference_item);
        }
        setPadding(0, toPixels(3), toPixels(15), toPixels(3));
    }

    int toPixels(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }
}
