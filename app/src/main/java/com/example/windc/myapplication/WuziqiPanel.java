package com.example.windc.myapplication;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WuziqiPanel extends View {
    private int mPanelWidth;
    private float mLineHight;
    private static int MAX_LINE = 10;
    private Paint mPaint = new Paint();
    private String TAG = WuziqiPanel.class.getSimpleName();
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float ratioPieceOfLineHight = 7 * 1.0f / 8;
    private List<Point> mWhiteArray = new ArrayList<>();
    private List<Point> mBlackArray = new ArrayList<>();
    private boolean isWhite = true;
    private boolean isWhiteWin = false;
    private boolean isGameOver = false;
    public MainActivity parentActivity = null;
    private MediaPlayer md = null;

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44440000);
        init();
        Log.i(TAG, "WuziqiPanel: init" );
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white_new);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_new);
    }

    public void start() {
        mBlackArray.clear();
        mWhiteArray.clear();
        isGameOver = false;
        isWhiteWin = false;
        isWhite = true;
        setStatusText("等候 " + (isWhite ? "白棋" : "黑棋") + " 落子");
        setCountText("棋子总数：" + (mBlackArray.size() + mWhiteArray.size()));
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, hightSize);

        if (hightMode == MeasureSpec.UNSPECIFIED) {
            width  = widthSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            width = hightSize;
        }
        setMeasuredDimension(width, width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: begin");
        drawBoard(canvas);
        drawPieces(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        if (isGameOver)
        {
            String text = isWhiteWin ? "白棋胜利" : "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private void drawPieces(Canvas canvas) {
        for (int i = 0; i < mWhiteArray.size(); i++)
        {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece, (whitePoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
                    (whitePoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
        }
        for (int i = 0; i < mBlackArray.size(); i++)
        {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece, (blackPoint.x + (1 - ratioPieceOfLineHight) / 2) * mLineHight,
                    (blackPoint.y + (1 - ratioPieceOfLineHight) / 2) * mLineHight, null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHight = mLineHight;

        Log.i(TAG, "drawBoard: begin");
        for (int i = 0; i < MAX_LINE; i++) {
            int start_x = (int) (lineHight / 2);
            int end_x = (int) (w - lineHight / 2);
            int y = (int) ((0.5 + i ) * lineHight);

            canvas.drawLine(start_x, y, end_x, y, mPaint);
//            Log.e(TAG, "drawBoard: start_x = " + start_x + " end_x = " + end_x + " y = " + y);
        }

        for (int i = 0; i < MAX_LINE; i++) {
            int start_y = (int) (lineHight / 2);
            int end_y = (int) (w - lineHight / 2);
            int x = (int) ((0.5 + i ) * lineHight);

            canvas.drawLine(x, start_y, x, end_y, mPaint);
//            Log.e(TAG, "drawBoard: start_y = " + start_y + " end_y = " + end_y + " x = " + x);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHight = mPanelWidth * 1.0f / MAX_LINE;

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, (int)(mLineHight * ratioPieceOfLineHight),(int)(mLineHight * ratioPieceOfLineHight), false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, (int)(mLineHight * ratioPieceOfLineHight),(int)(mLineHight * ratioPieceOfLineHight), false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isGameOver)
            return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_UP: {
                int x = (int) event.getX();
                int y = (int) event.getY();

                Point p = getValidPoint(x, y);

                if (mBlackArray.contains(p) || mWhiteArray.contains(p)){
                    return true;
                }
                parentActivity.playMusic();
//                playMusic();
                if (!isWhite) {
                    mBlackArray.add(p);
                } else {
                    mWhiteArray.add(p);
                }
                boolean iswin = checkWin(p, isWhite);
                if (iswin) {
                    Log.i(TAG, "onTouchEvent: " + (isWhite ? "White" : "Black") + " win!!!!!!!!!!!!");
                    isGameOver = true;
                    isWhiteWin = isWhite;
                }
                invalidate();
                isWhite = !isWhite;
                setStatusText("等候 " + (isWhite ? "白棋" : "黑棋") + " 落子");
                setCountText("棋子总数：" + (mBlackArray.size() + mWhiteArray.size()));

                if (iswin) {
                    setStatusText((isWhiteWin ? "白棋" : "黑棋") + " 获胜！");
                    playWinMusic();
                }
                return true;
            }
        }
        return true;
    }

    public void setStatusText(String text) {
        TextView status_text =  parentActivity.findViewById(R.id.status_view);
        status_text.setText(text);
    }

    public void setCountText(String text) {
        TextView status_text =  parentActivity.findViewById(R.id.count);
        status_text.setText(text);
    }

    private Point getValidPoint(int x, int y) {
        return new Point((int)(x/mLineHight), (int)(y / mLineHight));
    }

    private boolean checkByStep(Point p, List<Point> array, int xdiff, int ydiff) {
        Point tmp = new Point(0, 0);
        int i;
        int cnt = 0;

        //向反方向找到颜色相同的点
        for (i = 1;  i < 5; i++){
            tmp.x = p.x - xdiff * i;
            tmp.y = p.y - ydiff * i;
            if (!array.contains(tmp))
                break;
            cnt++;
        }
        Log.i(TAG, "checkByStep: reverse cnt = " + cnt);
//        Log.e(TAG, "checkByStep: reverse i = " + i + " begin (" + tmp.x + ", " + tmp.y + ")" + " diff = (" + xdiff + ", " + ydiff + ")");

        int x = tmp.x;
        int y = tmp.y;

        for (i = 1;  i < 5; i++){
            tmp.x = p.x + xdiff * i;
            tmp.y = p.y + ydiff * i;
            if (!array.contains(tmp))
                break;
            cnt++;
        }
        Log.i(TAG, "checkByStep: no reverse cnt = " + cnt);
        if (cnt >= 4)
            return true;
        return false;
    }

    private boolean checkWin(Point p, boolean isWhite) {
        Log.i(TAG, "checkWin: Now is x = " + p.x + " y = " + p.y + " node color = " + (isWhite ? "white" : "black"));
        List<Point> array;
        if (isWhite) {
            array = mWhiteArray;
        } else
        {
            array = mBlackArray;
        }

        for (Point pt : array) {
            Log.i(TAG, "checkWin: " + (isWhite ? "white" : "black") + " array:(" + pt.x +", "+ pt.y + ")");
        }

        if (checkByStep(p, array, 0, 1))    //上下直线判断
            return true;
        if (checkByStep(p, array, 1, 0))    //左右直线判断
            return true;
        if (checkByStep(p, array, 1, 1))    //右朝上直线判断
            return true;
        if (checkByStep(p, array, -1, 1))   //右朝下直线判断
            return true;
        Log.i(TAG, "checkWin: false");
        return false;
    }

    public boolean getIsWhiteNow() {
        return this.isWhite;
    }

    private void playWinMusic()
    {
        try {
            if (null == md) {
                md = MediaPlayer.create(parentActivity.getBaseContext(), R.raw.victory_music);
            }
            md.start();
        } catch (Exception e) {
            Log.e(TAG, "playMusic: exception = " + e.toString());
        }

    }

}