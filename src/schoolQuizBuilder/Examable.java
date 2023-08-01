package schoolQuizBuilder;

import java.io.IOException;

public interface Examable {
	void createExam(Question [] question) throws IOException;
}
