package com.woowahan.goosgbt_study.wordchecker;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static com.woowahan.goosgbt_study.wordchecker.WordChecker.check;
import static org.assertj.core.api.Assertions.assertThat;

class WordCheckerTest
{
    private static final String RIGHT_WORD = "right word";
    private static final String WRONG_WORD = "wrong word";
    private static final String UNKOWN_WORD = "unknown word";

    @Test void singleChar()
    {
        var result = check("1");
        assertThat(result).isEqualTo(RIGHT_WORD);

        result = check("2");
        assertThat(result).isEqualTo(RIGHT_WORD);

        result = check("?");
        assertThat(result).isEqualTo(RIGHT_WORD);
    }

    @Test void sameTwoChars()
    {
        var result = check("11");
        assertThat(result).isEqualTo(WRONG_WORD);

        result = check("22");
        assertThat(result).isEqualTo(WRONG_WORD);
    }

    @Test void clearlyRightWord()
    {
        var result = check("12");
        assertThat(result).isEqualTo(RIGHT_WORD);

        result = check("2121");
        assertThat(result).isEqualTo(RIGHT_WORD);
    }

    @Test void sameTwoCharsInTheMiddle()
    {
        var result = check("1221");
        assertThat(result).isEqualTo(WRONG_WORD);

        result = check("211");
        assertThat(result).isEqualTo(WRONG_WORD);
    }

    @Test void hasQuestionMark()
    {
        var result = check("1?2");
        assertThat(result).isEqualTo(WRONG_WORD);
    }

    @ParameterizedTest
    @MethodSource("examples")
    void exampleList(Tuple2<String, String> param)
    {
        var result = check(param._1);
        assertThat(result).isEqualTo(param._2);
    }

    private static List<Tuple2<String, String>> examples()
    {
        return List.of(Tuple.of("?", RIGHT_WORD), Tuple.of("22", WRONG_WORD), Tuple.of("12", RIGHT_WORD),
                       Tuple.of("1?2", WRONG_WORD), Tuple.of("1?1", UNKOWN_WORD), Tuple.of("1?11", WRONG_WORD));
    }
}
