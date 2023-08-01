package schoolQuizBuilder;

import java.io.IOException;

public class ManualExam extends Exam {
	private static final int PREFIX_DEFALUT_ANSWERS = 2;

	public ManualExam(Question[] questions, int examSize)
			throws TooManyQuestionsException, ExamSizeBiggerThenRepositoryException {
		super(questions, examSize);
	}

	public String ShowQuestionsRepository() {
//		return only question with index number
		StringBuffer formattingQuestions = new StringBuffer("Questions:\n");
		for (int i = 0; i < this.questionsCounter; i++) {
			formattingQuestions.append(i + 1).append(". ").append(this.getQuestionRepository()[i].getQuestion())
					.append("\n");
		}
		return formattingQuestions.toString();
	}

	@Override
	public void createExam(Question[] question) throws IOException {
		addDefalutAnswers(question);
		this.saveExam();
	}

	public void addDefalutAnswers(Question[] question) {
//		add the default answers and check if they are correct
		Question[] tempExam = question;
		for (int i = 0; i < examCounter; i++) {
			int correctAnswerCounter = 0;
			if (tempExam[i] instanceof CloseQuestion) {
//				Cycle between all the questions
				for (int j = 0; j < ((CloseQuestion) tempExam[i]).getNumOfAnswers(); j++) {
//					Cycle between all the answers in a question

					if (((CloseQuestion) tempExam[i]).getAnswers()[j].isCorrectAnswer() == true) {
						correctAnswerCounter++;
					}
				}
				if (correctAnswerCounter > 1) {
					setDefalutAnswers(false, true, i, tempExam);
				} else if (correctAnswerCounter == 1) {
					setDefalutAnswers(false, false, i, tempExam);
				} else {
					setDefalutAnswers(true, false, i, tempExam);
				}
			}
		}
	}

	private void setDefalutAnswers(boolean noAnswer, boolean moreThenOne, int index, Question[] question) {
		Question[] tempExam = question;
		if (tempExam[index] instanceof CloseQuestion) {
			tempExam[index].addAnswer(this.NO_ANSWER, noAnswer);
			tempExam[index].addAnswer(this.MORE_THEN_ONE_ANSWER, moreThenOne);
//			change all the other answers to false (keep only the 'more then one' as true;
			if (moreThenOne) {
				QuestionAnswer[] tempQuestionAnswers = ((CloseQuestion) tempExam[index]).getAnswers();
				int numOfAnswers = ((CloseQuestion) tempExam[index]).getNumOfAnswers();
				for (int i = 0; i < numOfAnswers - PREFIX_DEFALUT_ANSWERS; i++) {
					tempQuestionAnswers[i].setCorrectAnswer(false);
				}
			}
		}
	}

}
