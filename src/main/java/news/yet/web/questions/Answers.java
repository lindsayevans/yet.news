package news.yet.web.questions;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class Answers {
    public static String YES = "yes";
    public static String NO = "no";
    public static String MAYBE = "maybe";

    @Getter
    private static List<String> values = Arrays.asList(Answers.YES, Answers.NO, Answers.MAYBE);
}
