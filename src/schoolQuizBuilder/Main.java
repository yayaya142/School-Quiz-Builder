package schoolQuizBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Scanner;

import schoolQuizBuilder.Question.Difficulty;

public class Main {
	private static final int MAX_ANSWERS = 8;

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Scanner input = new Scanner(System.in);
		String fileName = "DataBaseRepo.dat";
		QuestionsRepository questionsDataBaseArray[] = readQuestionsRepository(fileName);
		QuestionsRepository questionsDataBase = questionsDataBaseArray[0];
		
		PrintRepositorys(questionsDataBaseArray);
		int repositoryIndex = input.nextInt() - 1;
		if (repositoryIndex >= 0 && repositoryIndex < questionsDataBaseArray.length) {
//				check if valid repo
			questionsDataBase = questionsDataBaseArray[repositoryIndex];
		}
		if (repositoryIndex == questionsDataBaseArray.length) {
//				if user press to add repo
			questionsDataBaseArray = Arrays.copyOf(questionsDataBaseArray, questionsDataBaseArray.length + 1);
			System.out.println("Enter new repository name: ");
			input.nextLine(); // clean buffer
			String repoName = input.nextLine();
			questionsDataBaseArray[repositoryIndex] = new QuestionsRepository(repoName);
			questionsDataBase = questionsDataBaseArray[repositoryIndex];
		}

		int menuOption = 0;
		while (menuOption != 8) {
			printMenuOptions();
			menuOption = input.nextInt();
			switch (menuOption) {
			case 1:
//				Display all system questions and answers.
				System.out.println(questionsDataBase.showRepository());
				break;
			case 2:
//				Add new answer to the repository.
				addNewAnswer(input, questionsDataBase);
				break;
			case 3:
//				Add a new answer to an existing question (from the repository) .
				addAnswerToQuestion(input, questionsDataBase);
				break;
			case 4:
//				Add new question.
				addNewQuestion(input, questionsDataBase);
				break;
			case 5:
//				Delete an answer from a question.
				removeAnswerFromQuestion(input, questionsDataBase);
				break;
			case 6:
//				Delete a question (and all its answers).
				deleteQuestion(input, questionsDataBase);
				break;
			case 7:
//				create exam
				createExam(input, questionsDataBase.getQuestions());
				break;
			case 8:
				saveRepositoryToFile(questionsDataBaseArray, fileName);
//				Exit menu and save binary file
				break;
			}
		}
		input.close();
	}

	public static void PrintRepositorys(QuestionsRepository[] questionsDataBaseArray) {
		for (int i = 0; i < questionsDataBaseArray.length; i++) {
			System.out.println("press " + (i + 1) + " for " + questionsDataBaseArray[i].getName());
		}
		System.out.println("press " + (questionsDataBaseArray.length + 1) + " to create new repository");
	}

	public static QuestionsRepository[] readQuestionsRepository(String fileName)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream file = new ObjectInputStream(new FileInputStream(fileName));
		QuestionsRepository questionsDataBaseArray[] = (QuestionsRepository[]) file.readObject();
		file.close();
		return questionsDataBaseArray;
	}

	public static void saveRepositoryToFile(QuestionsRepository[] questionsDataBaseArray, String fileName)
			throws FileNotFoundException, IOException {
		ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(fileName));
		file.writeObject(questionsDataBaseArray);
		file.close();
	}

	public static void addNewAnswer(Scanner input, QuestionsRepository questionsDataBase) {
//		case 2
//		add new answer to the repository.
		System.out.println("Enter your answer:");
		input.nextLine();
		String answerStr = input.nextLine();
		Answer answer = new Answer(answerStr);
		boolean answerAdded = questionsDataBase.addAnswerToRepository(answer);
		if (answerAdded) {
			System.out.println("The answer has been successfully added");
		} else {
			System.out.println("The answer has not been added to the database");
		}
	}

	public static void addAnswerToQuestion(Scanner input, QuestionsRepository questionsDataBase) {
//		case 3
//		this function add answer (from the repository) to an existing question in the repository.
		int selectQuestion = selectQuestion(input, questionsDataBase);
		boolean isQuestionAdded = false;
		if (checkRepositoryRange(selectQuestion, false, questionsDataBase)) {
			Question tempQuestion = questionsDataBase.questions[selectQuestion - 1];
			if (tempQuestion instanceof CloseQuestion) {
				if (((CloseQuestion) tempQuestion).getNumOfAnswers() == MAX_ANSWERS) {
					System.out.println("\nYou have reached the limit of possible answers\n");
					return;
				}
				System.out.println("Your question:");
				System.out.println(tempQuestion.showQuestionWithAnswers(true));
				System.out.println("\nYou can add these questions:");
//				show only unused answers
				Answer[] tempRepositoryAnswers = questionsDataBase.getAnswers();
				QuestionAnswer[] tempQuestionAnswers = ((CloseQuestion) tempQuestion).getAnswers();
				int[] tempIndex = new int[tempRepositoryAnswers.length
						- ((CloseQuestion) tempQuestion).getNumOfAnswers()];
				int counter = 0;
				for (int i = 0; i < tempRepositoryAnswers.length; i++) {
					boolean showAnswer = true;
					for (int j = 0; j < ((CloseQuestion) tempQuestion).getNumOfAnswers(); j++) {
						if (tempRepositoryAnswers[i].equals(tempQuestionAnswers[j].getAnswer())) {
							showAnswer = false;
							break;
						}
					}
					if (showAnswer) {
						tempIndex[counter] = i;
						counter++;
						System.out.println(counter + ". " + tempRepositoryAnswers[i]);
					}
				}
				System.out.println("Select answer from the list");
				int selectAnswer = input.nextInt();
				if (selectAnswer < 0 || selectAnswer > tempIndex.length) {
					System.out.println("Invalid input");
					return;
				}
				selectAnswer = tempIndex[selectAnswer - 1] + 1;
//				check if answer is correct
				System.out.println("Is that the correct answer?");
				System.out.println("press 1 if correct");
				System.out.println("press 2 if wrong");
				int isCorrectSelect = input.nextInt();
				boolean isCorrect = false;
				if (isCorrectSelect == 1) {
					isCorrect = true;
				}
				Answer tempAnswer = questionsDataBase.answers[selectAnswer - 1];
				isQuestionAdded = tempQuestion.addAnswer(tempAnswer, isCorrect);

			} else if (tempQuestion instanceof OpenQuestion) {
				System.out.println("This is the current answer, would you like to change it?");
				System.out.println(tempQuestion.showQuestionWithAnswers(true));
				System.out.println("press 1 to write new answer");
				System.out.println("press 2 exit");
				int userChoise = input.nextInt();
				input.nextLine();
				if (userChoise == 1) {
					System.out.println("Write the school answer for that question:");
					String answer = input.nextLine();
					Answer openAnswer = new Answer(answer);
					isQuestionAdded = tempQuestion.addAnswer(openAnswer, true);
				}
			}
			if (isQuestionAdded) {
				System.out.println("The answer has been successfully added");
			} else {
				System.out.println("The answer has not been added");
			}
		}
	}

	public static void addNewQuestion(Scanner input, QuestionsRepository questionsDataBase) {
//		case 4
//		add new question to the repository (from the user input)
		boolean questionAdded = false;
		System.out.println("Choose if open question or close question:");
		System.out.println("press 1 if close question");
		System.out.println("press 2 if open question");
		int questionType = input.nextInt();
		System.out.println("Enter your question:");
		input.nextLine();
		String questionStr = input.nextLine();

//		choose defecult level
		Difficulty difficultyLevel = difficultyLevel(input);

		if (questionType == 1) {
			CloseQuestion closeQuestion = new CloseQuestion(questionStr, difficultyLevel);
			questionAdded = questionsDataBase.addQuestionsToRepository(closeQuestion);
		} else if (questionType == 2) {
			System.out.println("Write the school answer for that question:");
			String answer = input.nextLine();
			Answer openAnswer = new Answer(answer);

			OpenQuestion openQuestion = new OpenQuestion(questionStr, openAnswer, difficultyLevel);
			questionAdded = questionsDataBase.addQuestionsToRepository(openQuestion);
		}

		if (questionAdded) {
			System.out.println("The question has been successfully added");
		} else {
			System.out.println("The question has not been added to the database");
		}
	}

	public static Difficulty difficultyLevel(Scanner input) {
		System.out.println("Choose a difficulty level");
		System.out.println("1) Easy");
		System.out.println("2) Medium");
		System.out.println("3) Hard");
		int difficultyLevel = input.nextInt();
		input.nextLine(); // clean buffer
		switch (difficultyLevel) {
		case 1:
			return Question.Difficulty.EASY;
		case 2:

			return Question.Difficulty.MEDIUM;
		case 3:
			return Question.Difficulty.HARD;
		default:
			return Question.Difficulty.MEDIUM;

		}
	}

	public static void removeAnswerFromQuestion(Scanner input, QuestionsRepository questionsDataBase) {
//		case 5
//		this function let the user remove answer from question (by using menu)
		int selectQuestion = selectQuestion(input, questionsDataBase);
		if (checkRepositoryRange(selectQuestion, false, questionsDataBase)) {
			Question tempQuestion = questionsDataBase.questions[selectQuestion - 1];
			if (tempQuestion instanceof CloseQuestion) {
				if (((CloseQuestion) tempQuestion).getNumOfAnswers() == 0) {
					System.out.println("There are no answers to the question");
					return;
				}
			}
			removeAnswerQuestion(input, tempQuestion);
		}
	}

	public static void removeAnswerQuestion(Scanner input, Question tempQuestion) {
		System.out.println(tempQuestion.showQuestionWithAnswers(true));
		boolean removeAnswer = false;
		if (tempQuestion instanceof CloseQuestion) {
			System.out.println("Write the letter of the answer, for example: 'A' or 'a'");
			String selectAnswer = input.next();
			input.nextLine();
			int answerIndex = convertCharToNum(selectAnswer.charAt(0));
			System.out.println(answerIndex);
			removeAnswer = ((CloseQuestion) tempQuestion).removeAnswer(answerIndex);

		} else if (tempQuestion instanceof OpenQuestion) {
			System.out.println("Do you want to delete this answer?");
			System.out.println("press 1 to remove");
			System.out.println("press 2 to keep this question");
			int userChoise = input.nextInt();
			if (userChoise == 1) {
				removeAnswer = ((OpenQuestion) tempQuestion).removeAnswer();
			}
		}
		if (removeAnswer) {
			System.out.println("The question was successfully removed");
		} else {
			System.out.println("The question has not been removed");
		}
	}

	public static void deleteQuestion(Scanner input, QuestionsRepository questionsDataBase) {
//		case 6
//		Deletes an entire question from the repository. 
		int selectQuestion = selectQuestion(input, questionsDataBase);
		if (checkRepositoryRange(selectQuestion, false, questionsDataBase)) {
			boolean hasDeleted = questionsDataBase.removeQuestionsFromRepository(selectQuestion);
			if (hasDeleted) {
				System.out.println("The question was successfully deleted");
			} else {
				System.out.println("The question has not been deleted");
			}
		}
	}

	public static void createExam(Scanner input, Question[] questions) throws IOException {
//		case 7
//		let the user create an exam file and solution file (navigation by menu)
		System.out.println("How many questions do you want on the exam?");
		int examSize = input.nextInt();
		System.out.println("Do you want to create the exam automaticly or manualy?");
		System.out.println("Press 1 for Manual");
		System.out.println("Press 2 for Automatic");
		int userOption = input.nextInt();
		if (userOption == 1) {
//			 user choose Manual exam
			try {
				ManualExam exam = new ManualExam(questions, examSize);
				while (exam.examCounter < examSize) {
					System.out.println(exam.ShowQuestionsRepository());
					System.out.println("Choose question from the list:");
					int selectedQuestion = input.nextInt();
					if (exam.getQuestionRepository()[selectedQuestion - 1] instanceof OpenQuestion) {
						exam.selectQuestion(selectedQuestion - 1, null);
					}
					if (exam.getQuestionRepository()[selectedQuestion - 1] instanceof CloseQuestion) {
//						need to select answers
						System.out.println(
								exam.getQuestionRepository()[selectedQuestion - 1].showQuestionWithAnswers(true));
						System.out.println("Choose answers from the list: \nfor example write: 'a,c,d,e'");
						input.nextLine();
						String selectedChars = input.nextLine();
						int[] selectedAnswers = selectAnswers(selectedChars);
						exam.selectQuestion(selectedQuestion - 1, selectedAnswers);
					}

				}
				exam.createExam(exam.getExamQuestions());

			} catch (TooManyQuestionsException | ExamSizeBiggerThenRepositoryException
					| LessTheanMinAnswerException e) {
				System.out.println(e.getMessage());
			}
		}

		if (userOption == 2) {
//			 user choose Automatic exam
			try {
				AutomaticExam exam = new AutomaticExam(questions, examSize);

				exam.createExam(exam.getExamQuestions());

			} catch (TooManyQuestionsException | ExamSizeBiggerThenRepositoryException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static int[] selectAnswers(String answersChars) {
		String CharsList[] = answersChars.split(",");
		int numberOfNulls = 0;
		int numberList[] = new int[CharsList.length];
		for (int i = 0; i < CharsList.length; i++) {
			numberList[i] = (convertCharToNum(CharsList[i].charAt(0))) - 1;
			if (numberList[i] == -1) {

			}
		}
//		copy the array again to make sure there is no nulls.
		int CopynumberList[] = new int[numberList.length - numberOfNulls];
		int counter = 0;
		for (int i = 0; i < numberList.length; i++) {
			if (numberList[i] >= 0) {
				CopynumberList[counter] = numberList[i];
				counter++;
			}
		}
		return CopynumberList;
	}

	public static int convertCharToNum(char charIndex) {
		charIndex = Character.toLowerCase(charIndex);
		if (charIndex > 96 && charIndex < 123) {
//			if char is >= a and <= z
			return charIndex - 'a' + 1;
		}
		System.out.println("Invalid input");
		return -1;
	}

	public static int selectAnswer(Scanner input, QuestionsRepository questionsDataBase) {
		System.out.println("Select answer from the list");
		System.out.println(questionsDataBase.showAnswers());
		int selectAnswer = input.nextInt();
		return selectAnswer;
	}

	public static int selectQuestion(Scanner input, QuestionsRepository questionsDataBase) {
		System.out.println("Select question from the list");
		System.out.println(questionsDataBase.showQuestions());
		int selectQuestion = input.nextInt();
		return selectQuestion;
	}

	public static boolean checkRepositoryRange(int select, boolean checkAnswersRange,
			QuestionsRepository questionsDataBase) {
//		this function check if the 'select' int is in range in Answer[] or in question[]
//		min index is 1 not 0 (for the user comfort
		select--;
		if (select < 0) {
			System.out.println("Invalid input");
			return false;
		}
		if (checkAnswersRange) {
			if (select < questionsDataBase.answers.length) {
				return true;
			}
		} else {
			if (select < questionsDataBase.questions.length) {
				return true;
			}
		}
		System.out.println("Invalid input");
		return false;
	}

	public static void printMenuOptions() {
		System.out.println("\nMenu:");
		System.out.println("1) Display all system questions and answers.");
		System.out.println("2) Add new answer.");
		System.out.println("3) Add a new answer to an existing question.");
		System.out.println("4) Add new question.");
		System.out.println("5) Delete an answer from question.");
		System.out.println("6) Delete a question (and all its answers).");
		System.out.println("7) Create a exam");
		System.out.println("8) Exit menu");
	}
}
