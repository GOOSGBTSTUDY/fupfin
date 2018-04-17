package com.woowahan.goosgbt_study.wordchecker;

import io.vavr.collection.List;

public class WordChecker
{
    private static final char CHAR_CONSONANT = '1';
    private static final char CHAR_VOWEL = '2';
    private static final char CHAR_UNKNOWN = '?';

    public static String check(String word)
    {
        var chars = List.ofAll(word.toCharArray());
        return check(chars.head(), chars.tail()).toString();
    }

    private static Result check(char last, List<Character> rest)
    {
        Result result;

        if(rest.isEmpty())
            result = Result.RIGHT;
        else if(rest.head() == last)
            result = Result.WRONG;
        else if(rest.head() == CHAR_UNKNOWN)
            result = merge(check(last, rest.tail().prepend(CHAR_CONSONANT)),
                           check(last, rest.tail().prepend(CHAR_VOWEL)));
        else
            result = check(rest.head(), rest.tail());

        return result;
    }

    private static Result merge(Result left, Result right)
    {
        return left == right ? left : Result.UNKNOWN;
    }

    private enum Result {
        RIGHT("right word"),
        WRONG("wrong word"),
        UNKNOWN("unknown word");

        private final String name;

        Result(String name) { this.name = name; }

        public String toString() { return name; }
    }
}
