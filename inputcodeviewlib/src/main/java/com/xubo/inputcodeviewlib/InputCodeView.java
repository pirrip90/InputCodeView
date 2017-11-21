package com.xubo.inputcodeviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：xubo
 * Time：2017-11-20
 * Description：
 */

public class InputCodeView extends ViewGroup {
    private static final int CODE_TEXT_COLOR = Color.BLACK;
    private static final float CODE_TEXT_SIZE = 16;
    private static final int CODE_SPACE = 3;
    private static final int CODE_BACKGROUND = R.drawable.code_background_n;
    private static final int CODE_BACKGROUND_F = R.drawable.code_background_f;
    private static final int CODE_NUMBER = 4;

    private int codeTextColor;
    private float codeTextSize;
    private int codeSpace;
    private int codeBackground;
    private int codeBackgroundF;
    private int codeWidth;
    private int codeHeight;
    private int codeNumber;

    private EditText editText;
    private List<TextView> textViews = new ArrayList<TextView>();

    public InputCodeView(Context context) {
        this(context, null);
    }

    public InputCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InputCodeView, defStyleAttr, 0);
        if (typedArray != null) {
            codeTextColor = typedArray.getColor(R.styleable.InputCodeView_codeTextColor, CODE_TEXT_COLOR);
            codeTextSize = typedArray.getFloat(R.styleable.InputCodeView_codeTextSize, CODE_TEXT_SIZE);
            codeSpace = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_codeSpace, (int) dp2px(context, CODE_SPACE));
            codeBackground = typedArray.getResourceId(R.styleable.InputCodeView_codeBackground, -1);
            codeBackgroundF = typedArray.getResourceId(R.styleable.InputCodeView_codeBackgroundF, -1);
            if (codeBackground == -1) {  //如果code背景图没设置,则code背景图和code选中背景图都为默认图
                codeBackground = CODE_BACKGROUND;
                codeBackgroundF = CODE_BACKGROUND_F;
            } else if (codeBackground != -1 && codeBackgroundF == -1) {  //如果code被选中背景图没设置,则code选中图为设置的code背景图
                codeBackgroundF = codeBackground;
            }
            codeNumber = typedArray.getInteger(R.styleable.InputCodeView_codeNumber, CODE_NUMBER);
            if (codeNumber <= 0) {
                codeNumber = 1;
            }

            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(sp2px(context, codeTextSize));
            BoringLayout.Metrics boring = BoringLayout.isBoring("", textPaint);
            int textHeight = boring.bottom - boring.top;

            codeWidth = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_codeWidth, textHeight);
            codeHeight = typedArray.getDimensionPixelSize(R.styleable.InputCodeView_codeHeight, textHeight);
            //如果设置的code高度小于文字高度,则code高度为文字的高度
            if (codeHeight < textHeight) {
                codeHeight = textHeight;
            }
            typedArray.recycle();
        } else {
            codeTextColor = CODE_TEXT_COLOR;
            codeTextSize = CODE_TEXT_SIZE;
            codeSpace = (int) dp2px(context, CODE_SPACE);
            codeBackground = CODE_BACKGROUND;
            codeBackgroundF = CODE_BACKGROUND_F;
            codeNumber = CODE_NUMBER;

            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(sp2px(context, codeTextSize));
            BoringLayout.Metrics boring = BoringLayout.isBoring("", textPaint);
            int textHeight = boring.bottom - boring.top;
            codeWidth = textHeight;
            codeHeight = textHeight;
        }
        init(context);
    }

    private void init(Context context) {
        editText = new EditText(context);
        editText.setCursorVisible(false);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(codeNumber)});
        editText.setTextSize(0.1f);

        for (int i = 0; i < codeNumber; i++) {
            TextView textView = new TextView(context);
            textView.setTextSize(codeTextSize);
            textView.setTextColor(codeTextColor);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(codeBackground);
            textViews.add(textView);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addView(editText);
        inputListener();
        for (TextView textView : textViews) {
            addView(textView);
        }
        setText("");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int minWidth = codeNumber * codeWidth + (codeNumber - 1) * codeSpace + getPaddingLeft() + getPaddingRight();
        int minHeight = codeHeight + getPaddingTop() + getPaddingBottom();
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) { //宽度不确定则等于可容纳所有code的最小宽度
            measuredWidth = minWidth;
        } else {   //宽度确定,但设置的宽度不足以容纳所有code,则把宽度设置为最小宽度
            if (measuredWidth < minWidth) {
                measuredWidth = minWidth;
            }
        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {  //高度不确定则等于最小高度
            measuredHeight = minHeight;
        } else {  //高度确定,但设置的高度不足以容纳code高度,则把高度设置为最小高度
            if (measuredHeight < minHeight) {
                measuredHeight = minHeight;
            }
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            View editText = getChildAt(0);
            editText.layout(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
            int minWidth = codeNumber * codeWidth + (codeNumber - 1) * codeSpace + getPaddingLeft() + getPaddingRight();
            for (int i = 1; i <= codeNumber; i++) {
                View textView = getChildAt(i);
                int left = (width - minWidth) / 2 + (i - 1) * (codeSpace + codeWidth);
                int top = (height - codeHeight) / 2;
                int right = left + codeWidth;
                int bottom = top + codeHeight;
                textView.layout(left, top, right, bottom);
            }
        }
    }

    /**
     * 获取输入的内容
     * @return
     */
    public String getInputText() {
        return editText.getText().toString().trim();
    }

    /**
     * 清空输入
     */
    public void clear() {
        setInputText("");
    }


    /**
     * 设置输入的内容
     * @param inputText
     */
    public void setInputText(String inputText) {
        inputText = inputText.trim();
        int length = inputText.length();
        length = length > codeNumber ? codeNumber : length;
        inputText = inputText.substring(0, length );
        editText.setText(inputText);
    }

    private void setText(String inputText) {
        inputText = inputText.trim();
        int length = inputText.length();
        length = length > codeNumber ? codeNumber : length;
        for (int i = 0; i < textViews.size(); i++) {
            TextView textView = textViews.get(i);
            if (i <= length - 1) {
                textView.setText(String.valueOf(inputText.charAt(i)));
            } else {
                textView.setText("");
            }
            if (i == length) {
                textView.setBackgroundResource(codeBackgroundF);
            } else {
                textView.setBackgroundResource(codeBackground);
            }
        }
    }

    private void inputListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = s.toString();
                if (inputStr != null) {
                    setText(inputStr.trim());
                }
            }
        });
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String inputText = editText.getText().toString().toString();
                    if (inputText.length() > 0) {
                        editText.getEditableText().delete(inputText.length() - 1, inputText.length());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private float sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return spValue * fontScale;
    }

    private float dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }
}
