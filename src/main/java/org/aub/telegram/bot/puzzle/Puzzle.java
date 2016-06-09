package org.aub.telegram.bot.puzzle;

public class Puzzle {
    private String question;
    private String answer;

    public Puzzle(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Puzzle puzzle = (Puzzle) o;

        if (question != null ? !question.equals(puzzle.question) : puzzle.question != null) return false;
        return !(answer != null ? !answer.equals(puzzle.answer) : puzzle.answer != null);

    }

    @Override
    public int hashCode() {
        int result = question != null ? question.hashCode() : 0;
        result = 31 * result + (answer != null ? answer.hashCode() : 0);
        return result;
    }
}

