package schoolQuizBuilder;

import java.io.Serializable;

public class Answer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7680853279890863406L;
	public static final String UNKNOWN = "unknown";
	private String answer;

	public Answer(String answer) {
		setAnswer(answer);
	}

	public Answer(Answer other) {
		answer = other.answer;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		if (answer != null && answer.isEmpty() == false) {
			this.answer = answer;
		} else {
			this.answer = UNKNOWN;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		return (obj instanceof Answer) && ((Answer) obj).getAnswer().equals(this.getAnswer());
	}

	@Override
	public String toString() {
		return answer;
	}

}
