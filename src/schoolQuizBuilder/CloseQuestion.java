package schoolQuizBuilder;

public class CloseQuestion extends Question {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3047720451895217063L;
	private static final int MAX_ANSWERS = 10;
	private QuestionAnswer[] answers;
	private int numOfAnswers;

	public CloseQuestion(String question, Difficulty difficulty) {
		super(question, difficulty);
		this.answers = new QuestionAnswer[MAX_ANSWERS];

	}

	public CloseQuestion(CloseQuestion other) {
		super(other);
		answers = new QuestionAnswer[MAX_ANSWERS];
		for (int i = 0; i < other.numOfAnswers; i++) {
			answers[i] = new QuestionAnswer(other.answers[i]);
		}
		numOfAnswers = other.numOfAnswers;
	}

	public QuestionAnswer[] getAnswers() {
		return answers;
	}

	public int getNumOfAnswers() {
		return numOfAnswers;
	}

	public boolean addAnswer(Answer answer, boolean correctAnswer) {
//		return true if managed to add
		if (answer == null || answer instanceof Answer == false) {
			return false;
		}
		if (numOfAnswers < MAX_ANSWERS && hasAnswer(answer) == false) {
//			check if have more space in array and check if already have this answer
			answers[numOfAnswers] = new QuestionAnswer(answer, correctAnswer);
			numOfAnswers++;
			return true;
		}
		return false;
	}

	public boolean removeAnswer(int index) {
//		min index is 1 not 0 (for the user comfort)
		index--;
//		return true if remove answer
		if (index >= 0 && index < numOfAnswers) {
			answers[index] = null;
			if (index < numOfAnswers) {
//				need to move array to left
				moveAnswersToLeft(index);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean removeAnswer() {
		return false;
	}

	private void moveAnswersToLeft(int index) {
		for (int i = index; i < numOfAnswers - 1; i++) {
			answers[i] = answers[i + 1];
		}
		answers[numOfAnswers - 1] = null;
		numOfAnswers--;
	}

	private boolean hasAnswer(Answer answer) {
//		check if answer already in answers array
		for (int i = 0; i < numOfAnswers; i++) {
			Answer tempCompare = answers[i].getAnswer();
			if (answer.equals(tempCompare)) {
				return true;
			}
		}
		return false;
	}

	public int getNumOfCorrectAnswers() {
		int correctAnswerCounter = 0;
		for (int i = 0; i < numOfAnswers; i++) {
			if (answers[i].isCorrectAnswer()) {
				correctAnswerCounter++;
			}
		}
		return correctAnswerCounter;

	}

	public String showQuestionWithAnswers(boolean withCorrectAnswer) {
		char answerChars = 'A';
		StringBuffer formatedQuestion = new StringBuffer();
		formatedQuestion.append(this.getQuestion());
		if (withCorrectAnswer) {
			formatedQuestion.append(" (").append(this.getDifficulty()).append(")");
		}
		for (int i = 0; i < numOfAnswers; i++) {
			formatedQuestion.append("\n\t").append(answerChars).append(") ")
					.append(answers[i].showAnswer(withCorrectAnswer));
			answerChars++;
		}
		return formatedQuestion.toString();
	}

}
