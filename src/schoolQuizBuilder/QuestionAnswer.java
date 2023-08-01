package schoolQuizBuilder;

import java.io.Serializable;

public class QuestionAnswer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8037131172161444241L;
	private static final String CORRECT_ANSWER = " ----> Correct answer";
	private static final String WRONG_ANSWER = " ----> Wrong answer";
	private Answer answer;
	private boolean isCorrectAnswer;

	public QuestionAnswer(Answer answer, boolean isCorrectAnswer) {
		setAnswer(answer);
		setCorrectAnswer(isCorrectAnswer);
	}

	public QuestionAnswer(QuestionAnswer other) {
		answer = other.answer;
		isCorrectAnswer = other.isCorrectAnswer;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		if (answer instanceof Answer && answer != null) {
			this.answer = answer;
		}
	}

	public boolean isCorrectAnswer() {
		return isCorrectAnswer;
	}

	public void setCorrectAnswer(boolean isCorrectAnswer) {
//		set if correct answer or not
		this.isCorrectAnswer = isCorrectAnswer;
	}

	public String showAnswer(boolean withCorrectAnswer) {
		String formattedAnswer = answer.toString();
		if (withCorrectAnswer) {
			if (isCorrectAnswer) {
				formattedAnswer = formattedAnswer + CORRECT_ANSWER;
			} else {
				formattedAnswer = formattedAnswer + WRONG_ANSWER;
			}
		}
		return formattedAnswer;
	}

	@Override
	public String toString() {
		return answer.toString();
	}

}
