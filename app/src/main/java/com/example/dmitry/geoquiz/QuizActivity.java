package com.example.dmitry.geoquiz;

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
    private ImageButton mNextButton;
    private ImageButton mBackButton;
    private TextView mQuestionTextView;

    private static final String KEY_INDEX = "index";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called ");
        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null){
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        //Кнопка Next. При нажатии выводит в TextView следующий вопрос (объект из массива)
        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
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

        updateQuestion();
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop() called ");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    //Функция обновления текста в TextView. Помещает текст из объекта в TextView
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        mButtonFalse.setEnabled(true);
        mButtonTrue.setEnabled(true);
    }

    //Функция проверки верности ответа.
    private void checkAnswer(boolean userPressedTrue) {
        //берет значение true или false из объекта
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        qCounter += 1;
        //Если юзер нажал True и правильный ответ True выводит уведомление.
        if (userPressedTrue == answerIsTrue) {
            correctCounter += 1;
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        mButtonFalse.setEnabled(false);
        mButtonTrue.setEnabled(false);

        //Создание уведомления

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

