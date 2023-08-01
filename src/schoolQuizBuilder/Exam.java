package schoolQuizBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Exam implements Examable {
	protected static final Answer NO_ANSWER = new Answer("There is no correct answer");
	protected static final Answer MORE_THEN_ONE_ANSWER = new Answer("More than one answer is correct");
	private static final int MAX_EXAM_QUESTIONS = 10;
	private static final int MIN_ANSWER_FOR_CLOSE = 4;

	private Question examQuestion[];
	private Question QuestionRepository[];
	protected int examCounter;
	protected int questionsCounter;

	public Exam(Question[] questionRepository, int examSize)
			throws TooManyQuestionsException, ExamSizeBiggerThenRepositoryException {
		this.QuestionRepository = new Question[questionRepository.length];
		setQuestionRepository(questionRepository);
		setExamSize(examSize);
	}

	public Question[] getExamQuestions() {
		return examQuestion;
	}

	public void setExamSize(int examSize) throws TooManyQuestionsException, ExamSizeBiggerThenRepositoryException {
		if (examSize <= 0 || examSize > questionsCounter) {
//			if the user enter invalid exam size
			throw new ExamSizeBiggerThenRepositoryException(questionsCounter, examSize);
		}

		if (examSize > MAX_EXAM_QUESTIONS) {
//			if exam size bigger the the premited size (defalut 10)
			throw new TooManyQuestionsException(MAX_EXAM_QUESTIONS, examSize);
		}
		examQuestion = new Question[examSize];
		return;
	}

	public Question[] getQuestionRepository() {
		return QuestionRepository;
	}

	public void setQuestionRepository(Question[] questionRepository) {
//		save valid question (open question) or close with more then the {prefix} answers
		for (int i = 0; i < questionRepository.length; i++) {
			if (questionRepository[i] instanceof OpenQuestion) {
				this.QuestionRepository[questionsCounter] = new OpenQuestion((OpenQuestion) questionRepository[i]);
				questionsCounter++;
			}

			if (questionRepository[i] instanceof CloseQuestion) {
//				check if the question have at lest {prefix} answers
				if ((((CloseQuestion) questionRepository[i]).getNumOfAnswers()) >= MIN_ANSWER_FOR_CLOSE) {
					this.QuestionRepository[questionsCounter] = new CloseQuestion(
							(CloseQuestion) questionRepository[i]);
					questionsCounter++;
				}
			}
		}
	}

	public void selectQuestion(int index, int selectedAnswers[]) throws LessTheanMinAnswerException {
		if (QuestionRepository[index] instanceof OpenQuestion) {
			this.examQuestion[examCounter] = new OpenQuestion((OpenQuestion) QuestionRepository[index]);
			examCounter++;
		}
		if (QuestionRepository[index] instanceof CloseQuestion) {
			CloseQuestion tempCloseQuestion = (CloseQuestion) QuestionRepository[index];
			this.examQuestion[examCounter] = new CloseQuestion(tempCloseQuestion.getQuestion(),
					tempCloseQuestion.getDifficulty());
			for (int i = 0; i < selectedAnswers.length; i++) {
				QuestionAnswer tempQuestionAnswer = tempCloseQuestion.getAnswers()[selectedAnswers[i]];
				for (int j = 0; j < selectedAnswers.length; j++) {
					this.examQuestion[examCounter].addAnswer(tempQuestionAnswer.getAnswer(),
							tempQuestionAnswer.isCorrectAnswer());
				}
			}
			if (((CloseQuestion) this.examQuestion[examCounter]).getNumOfAnswers() < MIN_ANSWER_FOR_CLOSE) {
				throw new LessTheanMinAnswerException(MIN_ANSWER_FOR_CLOSE,
						((CloseQuestion) this.examQuestion[examCounter]).getNumOfAnswers());
			}
			examCounter++;
		}
		removeSelectedQuestionRepository(index);
	}

	public void removeSelectedQuestionRepository(int index) {
		this.QuestionRepository[index] = null;
		for (int i = index; i < questionsCounter - 1; i++) {
			this.QuestionRepository[i] = QuestionRepository[i + 1];
		}
		questionsCounter--;
	}

	public String getTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formart = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		String formattedTime = now.format(formart);
		return formattedTime;
	}

	public void saveExam() throws IOException {
		String formatExam = formatExam(false);
		String formatSolution = formatExam(true);
		String time = getTime();
		String examName = "Exam_" + time;
		String solutionName = "Solution" + time;
		File examFile = new File(examName + ".txt");
		File solutionFile = new File(solutionName + ".txt");
		PrintWriter examWriter = new PrintWriter(examFile);
		PrintWriter solutionWriter = new PrintWriter(solutionFile);
//		create new file
		examFile.createNewFile();
		solutionFile.createNewFile();
//		print to the files
		examWriter.println(formatExam);
		solutionWriter.println(formatSolution);
		examWriter.close();
		solutionWriter.close();
		System.out.println("Exam save:     " + examName);
		System.out.println("Solution save: " + solutionName);
	}

	private String formatExam(boolean withAnswers) {
		StringBuffer exam = new StringBuffer();
		for (int i = 0; i < examCounter; i++) {
			exam.append(i + 1).append(". ").append(examQuestion[i].showQuestionWithAnswers(withAnswers)).append("\n");
		}
		return exam.toString();
	}

	public abstract void addDefalutAnswers(Question[] question);

	@Override
	public String toString() {
		String formatExam = formatExam(false);
		return formatExam;
	}

}
