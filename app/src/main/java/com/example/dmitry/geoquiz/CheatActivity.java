package com.example.dmitry.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockApplication;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.example.dmitry.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.example.dmitry.geoquiz.answer_shown";
    private static final String KEY_CHEAT = "IS CHEAT";

    private boolean mAnswerIsTrue;
    private boolean mAnswerShown = false;
    private String sdkVersion;

    private TextView mAnswerTextView;
    private TextView mAPITextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context packageContext, boolean answerIstrue){
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIstrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null){
            mAnswerShown = savedInstanceState.getBoolean(KEY_CHEAT, false);
        }

        mAPITextView = (TextView) findViewById(R.id.api_lvl);
        mAPITextView.setText(getAndroidVersion());

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = (TextView) findViewById(R.id.answer_text_view);

        mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAnswerShown = true;
                if (mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                //если версия СДК больше чем LOLLIPOP воспроихводить анимацию
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth() / 2;
                    int cy = mShowAnswerButton.getHeight() / 2;
                    float radius = mShowAnswerButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            mShowAnswerButton.setVisibility(View.VISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        //сохраняет переменную mAnswerShown после обновления активности
        savedInstanceState.putBoolean(KEY_CHEAT, mAnswerShown);
    }

    //При нажатии кнопки назад вызывает функцию setAnswerShownResult
    //и передает ей сохраненное значение mAnswerShown
    @Override
    public void onBackPressed(){
        setAnswerShownResult(mAnswerIsTrue);
        super.onBackPressed();
    }

    public String getAndroidVersion(){
        int sdkVer = Build.VERSION.SDK_INT;
        return "API level: " + sdkVer;
    }

    //передает isAnswerShown в QuizActivity
    private void setAnswerShownResult(boolean isAnswerShown){
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }
}
