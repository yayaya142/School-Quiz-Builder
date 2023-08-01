package schoolQuizBuilder;

public class TooManyQuestionsException extends ExamException {

	public TooManyQuestionsException(int maxExamSize, int userExamSize) {
		super("Error: exam is bigger the the permitted size (" + maxExamSize + ")"
				+ " you enter: " + userExamSize);
	}
}
