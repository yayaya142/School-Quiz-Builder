package schoolQuizBuilder;

public class ExamSizeBiggerThenRepositoryException extends ExamException {

	public ExamSizeBiggerThenRepositoryException(int repositorySize, int userExamSize) {
		super("Error: exam is bigger the the repository size (valid questions in repo are: " + repositorySize + ")"
				+ " you enter: " + userExamSize);
	}
}
