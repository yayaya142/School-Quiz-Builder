package schoolQuizBuilder;

import java.io.IOException;
import java.util.Random;

public class AutomaticExam extends Exam {
	private static final int DEAFULT_ANSWERS = 4; // one spot is for the deafult answer

	public AutomaticExam(Question[] questions, int examSize)
			throws TooManyQuestionsException, ExamSizeBiggerThenRepositoryException {
		super(questions, examSize);
		removeNotValidQuestions();
	}

	@Override
	public void createExam(Question[] question) throws IOException {
		Random random = new Random();
		for (int i = 0; i < question.length; i++) {
			int max = questionsCounter;
			int selectedQuestion = random.nextInt(max) + 1;
			addQuestionToExam(selectedQuestion - 1);
		}
		addDefalutAnswers(question);
		this.saveExam();
	}

	private void removeNotValidQuestions() throws ExamSizeBiggerThenRepositoryException {
		Question tempQuestionsR[] = this.getQuestionRepository();
		for (int i = 0; i < questionsCounter; i++) {
			if (tempQuestionsR[i] instanceof CloseQuestion) {
				CloseQuestion tempCloseQuestion = ((CloseQuestion) tempQuestionsR[i]);
				if (tempCloseQuestion.getNumOfAnswers()
						- tempCloseQuestion.getNumOfCorrectAnswers() < (DEAFULT_ANSWERS - 1)) {
					this.removeSelectedQuestionRepository(i);
					i--;
				}
			}
		}
		if (this.getExamQuestions().length > questionsCounter) {
			throw new ExamSizeBiggerThenRepositoryException(questionsCounter, this.getExamQuestions().length);
		}
	}

	private void addQuestionToExam(int selectedQuestion) {
		if (this.getQuestionRepository()[selectedQuestion] instanceof OpenQuestion) {
			try {
				this.selectQuestion(selectedQuestion, null);
			} catch (LessTheanMinAnswerException e) {
				System.out.println(e.getMessage());
			}
		}
		if (this.getQuestionRepository()[selectedQuestion] instanceof CloseQuestion) {
			int answers[] = addAnswerToCloseQuestion(this.getQuestionRepository()[selectedQuestion]);
			try {
				this.selectQuestion(selectedQuestion, answers);
			} catch (LessTheanMinAnswerException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private int[] addAnswerToCloseQuestion(Question question) {
		Random random = new Random();
		CloseQuestion tempQuestion = (CloseQuestion) question;
		int[] selectedAnswers = new int[DEAFULT_ANSWERS];
		int numOfAnswers = 0;
		boolean hasCorrectAnswer = false;
		while (numOfAnswers < selectedAnswers.length) {
			int selectedAnswer = random.nextInt(tempQuestion.getNumOfAnswers());
			if (hasThisAnswer(selectedAnswer, numOfAnswers, selectedAnswers) == false) {
				boolean correctAnswer = tempQuestion.getAnswers()[selectedAnswer].isCorrectAnswer();
				if (correctAnswer && hasCorrectAnswer == false) {
					hasCorrectAnswer = true;
					selectedAnswers[numOfAnswers] = selectedAnswer;
					numOfAnswers++;
				}
				if (correctAnswer == false) {
					selectedAnswers[numOfAnswers] = selectedAnswer;
					numOfAnswers++;
				}
			}
		}
		return selectedAnswers;
	}

	private boolean hasThisAnswer(int selectedAnswer, int numOfAnswers, int[] selectedAnswers) {
		for (int i = 0; i < numOfAnswers; i++) {
			if (selectedAnswers[i] == selectedAnswer) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addDefalutAnswers(Question[] question) {
		for (int i = 0; i < question.length; i++) {
			if (question[i] instanceof CloseQuestion) {
				CloseQuestion tempQuestion = (CloseQuestion) question[i];
//				Cycle between all the questions
				for (int j = 0; j < tempQuestion.getNumOfAnswers(); j++) {
//					Cycle between all the answers in a question
					if (tempQuestion.getAnswers()[j].isCorrectAnswer()) {
//						if question have correct answer
						tempQuestion.addAnswer(this.NO_ANSWER, false);
						break;
					}
					if (j == tempQuestion.getNumOfAnswers() - 1) {
						tempQuestion.addAnswer(this.NO_ANSWER, true);
					}
				}

			}

		}

	}
}
