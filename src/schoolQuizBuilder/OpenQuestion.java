package schoolQuizBuilder;

public class OpenQuestion extends Question {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4014981530707696307L;
	private static final String UNKNOWN = "The school has not entered an answer for this question";
	private QuestionAnswer answer;

	public OpenQuestion(String question, Answer answer, Difficulty difficulty) {
		super(question, difficulty);
		this.addAnswer(answer, true);
	}

	public OpenQuestion(OpenQuestion other) {
		super(other);
		answer = other.answer;
	}

	@Override
	public String showQuestionWithAnswers(boolean withCorrectAnswer) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getQuestion());
		if (withCorrectAnswer) {
			sb.append(" (").append(this.getDifficulty()).append(")");
			sb.append("\n\t").append("answer: ").append(answer);
		}
		return sb.toString();
	}

	@Override
	public QuestionAnswer getAnswers() {
		return answer;
	}

	@Override
	public boolean addAnswer(Answer answer, boolean correctAnswer) {
//		return true if managed to add
		if (answer == null || answer instanceof Answer == false) {
			return false;
		}
		this.answer = new QuestionAnswer(answer, true);
		return true;
	}

	@Override
	public boolean removeAnswer() {
		Answer answer = new Answer(UNKNOWN);
		QuestionAnswer prefixAnswer = new QuestionAnswer(answer, false);
		this.answer = prefixAnswer;
		return true;
	}

}
