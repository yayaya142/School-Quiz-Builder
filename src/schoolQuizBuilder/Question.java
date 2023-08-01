package schoolQuizBuilder;

import java.io.Serializable;

public abstract class Question implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1402863788601299551L;

	enum Difficulty {
		EASY, MEDIUM, HARD
	}

	public static final String UNKNOWN = "unknown";
	private String question;
	private static int ID_Counter;
	private int id;
	private Difficulty difficulty;

	public Question(String question, Difficulty difficulty) {
		setQuestion(question);
		setDifficulty(difficulty);
		setId();
	}

	public Question(Question other) {
		question = other.question;
		difficulty = other.difficulty;
	}

	public void setDifficulty(Difficulty difficulty) {
		if (difficulty instanceof Difficulty) {
			this.difficulty = difficulty;
		} else {
			this.difficulty = Difficulty.MEDIUM;
		}
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public String getQuestion() {
		return question;
	}

	public abstract Object getAnswers();

	public void setQuestion(String question) {
		if (question != null && question.isEmpty() == false) {
			this.question = question;
		} else {
			this.question = UNKNOWN;
		}
	}

	public int getId() {
		return id;
	}

	public void setId() {
		ID_Counter++;
		this.id = ID_Counter;
	}

	public abstract boolean addAnswer(Answer answer, boolean correctAnswer);

	public abstract boolean removeAnswer();

	public abstract String showQuestionWithAnswers(boolean withCorrectAnswer);

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		return (obj instanceof Question) && ((Question) obj).getQuestion().equals(this.getQuestion());
	}

	@Override
	public String toString() {
		String formatedQuestion = showQuestionWithAnswers(false);
		return formatedQuestion.toString();
	}

}
