package schoolQuizBuilder;

import java.io.Serializable;
import java.util.Arrays;

public class QuestionsRepository implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8371665527912848022L;
	private static final String UNKNOWN = "Unknown";
	private String name;
	protected Question questions[];
	protected Answer answers[];

	public QuestionsRepository(String name) {
		setName(name);
		this.questions = new Question[0];
		this.answers = new Answer[0];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name != null && !name.isEmpty()) {
			this.name = name;
		} else {
			this.name = UNKNOWN;
		}
	}

	public QuestionsRepository(QuestionsRepository other) {
		// Copy questions
		this.questions = new Question[other.questions.length];
		for (int i = 0; i < other.questions.length; i++) {
			if (other.questions[i] instanceof CloseQuestion) {
				this.questions[i] = new CloseQuestion((CloseQuestion) other.questions[i]);
			}
			if (other.questions[i] instanceof OpenQuestion) {
				this.questions[i] = new OpenQuestion((OpenQuestion) other.questions[i]);
			}
		}
		// Copy answers
		this.answers = new Answer[other.answers.length];
		for (int i = 0; i < other.answers.length; i++) {
			this.answers[i] = new Answer(other.answers[i]);
		}
	}

	public Question[] getQuestions() {
		return questions;
	}

	public Answer[] getAnswers() {
		return answers;
	}

	public boolean addQuestionsToRepository(Question question) {
		if (question != null && question instanceof Question && hasQuestion(question) == false) {
			questions = Arrays.copyOf(questions, questions.length + 1);
			questions[questions.length - 1] = question;
			return true;
		}
		return false;
	}

	private boolean hasQuestion(Question question) {
//		check if question already in questions array
		for (int i = 0; i < questions.length; i++) {
			Question tempCompare = questions[i];
			if (question.equals(tempCompare)) {
				return true;
			}
		}
		return false;
	}

	public boolean addAnswerToRepository(Answer answer) {
		if (answer != null && answer instanceof Answer && hasAnswer(answer) == false) {
			answers = Arrays.copyOf(answers, answers.length + 1);
			answers[answers.length - 1] = answer;
			return true;
		}
		return false;
	}

	private boolean hasAnswer(Answer answer) {
//		check if answer already in answers array
		for (int i = 0; i < answers.length; i++) {
			Answer tempCompare = answers[i];
			if (answer.equals(tempCompare)) {
				return true;
			}
		}
		return false;
	}

	public boolean removeQuestionsFromRepository(int index) {
//		min index is 1 not 0 (for the user comfort)
		index--;
//		return true if remove answer
		if (index >= 0 && index < questions.length) {
			questions[index] = null;
			if (index < questions.length) {
//				need to move array to left
				moveQuestionsToLeft(index);
			}
			return true;
		}
		return false;
	}

	private void moveQuestionsToLeft(int index) {
		for (int i = index; i < questions.length - 1; i++) {
			questions[i] = questions[i + 1];
		}
		questions = Arrays.copyOf(questions, questions.length - 1);
	}

	public String showRepository() {
//		return question with correct answers
		StringBuffer repository = new StringBuffer();
		for (int i = 0; i < questions.length; i++) {
			repository.append("Q. ").append(questions[i].showQuestionWithAnswers(true)).append("\n");
		}
		return repository.toString();
	}

	public String showQuestions() {
//		return only question with index number
		StringBuffer formattingQuestions = new StringBuffer("Questions:\n");
		for (int i = 0; i < questions.length; i++) {
			formattingQuestions.append(i + 1).append(". ").append(questions[i].getQuestion()).append("\n");
		}
		return formattingQuestions.toString();
	}

	public String showAnswers() {
//		return only answers with index number
		StringBuffer formattingAnswers = new StringBuffer("Answers:\n");
		for (int i = 0; i < answers.length; i++) {
			formattingAnswers.append(i + 1).append(". ").append(answers[i]).append("\n");
		}
		return formattingAnswers.toString();
	}

	@Override
	public String toString() {
		StringBuffer formattingRepository = new StringBuffer();
		formattingRepository.append(this.showQuestions()).append("\n").append(this.showAnswers());
		return formattingRepository.toString();
	}

}
