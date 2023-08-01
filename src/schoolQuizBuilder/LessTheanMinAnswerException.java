package schoolQuizBuilder;

public class LessTheanMinAnswerException extends ExamException {

	public LessTheanMinAnswerException(int minAnswer, int userAnswers) {
		super("Error: the minimum answers are (" + minAnswer + ")" + " you selected: " + userAnswers);
	}
}
