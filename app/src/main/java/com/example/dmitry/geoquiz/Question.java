package com.example.dmitry.geoquiz;

//Класс - модель. MVC
//
//Объект типа вопрос. Содержит в себе текст вопроса и булево значение ответа.
public class Question {
    private int mTextResId;//ссылка на ресурс с текстом
    private boolean mAnswerTrue;

    //конструктор объекта Question. задает текст и ответ
    public Question(int textResId, boolean answerTrue){
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
    }

    //получаем текст из ресурсов
    public int getTextResId() {
        return mTextResId;
    }

    //ставим текст (для представления
    public void setTextResId(int textResId) {
        mTextResId = textResId;
    }

    //проверяем верен ли ответ
    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    //устанавливаем ответу значение True или false
    public void setAnswerTrue(boolean answerTrue) {
        mAnswerTrue = answerTrue;
    }
}
