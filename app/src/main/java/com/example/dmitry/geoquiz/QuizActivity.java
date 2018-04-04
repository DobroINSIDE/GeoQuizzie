package com.example.dmitry.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

//Активити - контроллер
public class QuizActivity extends AppCompatActivity {

    private Button mButtonTrue;
    private Button mButtonFalse;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private TextView mQuestionTextView;

    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String KEY_INDEX = "index";
    private static final String CHEAT_INDEX = "false";
    private static final String RESULT_INDEX = "result";
    private static final String TAG = "QuizActivity";
    private int correctCounter = 0;
    private int qCounter = 0;

    //Массив объектов Question. В каждом элементе массива
    //
    //задается текст из ресурса и ответ
    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
            new Question(R.string.question_mideast, true)
    };

    //индекс массива вопросов
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called ");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(CHEAT_INDEX, false);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        //Кнопка Next. При нажатии выводит в TextView следующий вопрос (объект из массива)
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mIsCheater = false;
                updateQuestion();
            }
        });

        //Кнопка ответа True и False. При нажатии проверяет
        //
        //правильность ответа checkAnswer()
        mButtonTrue = (Button) findViewById(R.id.true_button);
        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);

            }
        });

        mButtonFalse = (Button) findViewById(R.id.false_button);
        mButtonFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

            }
        });

        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start cheat activity
                //создаем объект класса Intent и запускаем активность
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);

                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(CHEAT_INDEX, mIsCheater);
    }

    //Функция обновления текста в TextView. Помещает текст из объекта в TextView
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mButtonFalse.setEnabled(true);
        mButtonTrue.setEnabled(true);
        mNextButton.setEnabled(false);
    }

    //Функция проверки верности ответа.
    private void checkAnswer(boolean userPressedTrue) {
        //берет значение true или false из объекта
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        qCounter += 1;

        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else {
            if (userPressedTrue == answerIsTrue) {
                correctCounter += 1;
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        mButtonFalse.setEnabled(false);
        mButtonTrue.setEnabled(false);
        mNextButton.setEnabled(true);

        if (qCounter == 6) {
           // int percent = (correctCounter % qCounter) * 100;
            Toast toast = Toast.makeText(this, "Верно: " + correctCounter + " из " + qCounter , Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        }

    }
}

