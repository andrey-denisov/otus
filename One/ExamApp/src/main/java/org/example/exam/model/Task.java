package org.example.exam.model;

// абстрактный родительский класс задачи. Хранит в себе id, вопрос и ответ. От него наследуются
// классы конкретных задач. Для примера сделан класс с несколькими вариантами ответов.
@SuppressWarnings("unused")
public abstract class Task {
    protected long id;
    protected String question;
    protected String rightAnswer;

    // нужен для маршалера
    protected Task() {
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public Task(long id, String question, String rightAnswer) {
        this.id = id;
        this.question = question;
        this.rightAnswer = rightAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public abstract TaskResult checkAnswer(String answer);
}
