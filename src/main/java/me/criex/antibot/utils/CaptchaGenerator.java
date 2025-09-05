package me.criex.antibot.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author CrieXD1337
 */

public class CaptchaGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random RANDOM = new Random();
    private static final Map<Character, Character> DISPLAY_TO_INPUT = new HashMap<>();

    static {
        // A-Z
        DISPLAY_TO_INPUT.put('Ⓐ', 'A');
        DISPLAY_TO_INPUT.put('Ⓑ', 'B');
        DISPLAY_TO_INPUT.put('Ⓒ', 'C');
        DISPLAY_TO_INPUT.put('Ⓓ', 'D');
        DISPLAY_TO_INPUT.put('Ⓔ', 'E');
        DISPLAY_TO_INPUT.put('Ⓕ', 'F');
        DISPLAY_TO_INPUT.put('Ⓖ', 'G');
        DISPLAY_TO_INPUT.put('Ⓗ', 'H');
        DISPLAY_TO_INPUT.put('Ⓘ', 'I');
        DISPLAY_TO_INPUT.put('Ⓙ', 'J');
        DISPLAY_TO_INPUT.put('Ⓚ', 'K');
        DISPLAY_TO_INPUT.put('Ⓛ', 'L');
        DISPLAY_TO_INPUT.put('Ⓜ', 'M');
        DISPLAY_TO_INPUT.put('Ⓝ', 'N');
        DISPLAY_TO_INPUT.put('Ⓞ', 'O');
        DISPLAY_TO_INPUT.put('Ⓟ', 'P');
        DISPLAY_TO_INPUT.put('Ⓠ', 'Q');
        DISPLAY_TO_INPUT.put('Ⓡ', 'R');
        DISPLAY_TO_INPUT.put('Ⓢ', 'S');
        DISPLAY_TO_INPUT.put('Ⓣ', 'T');
        DISPLAY_TO_INPUT.put('Ⓤ', 'U');
        DISPLAY_TO_INPUT.put('Ⓥ', 'V');
        DISPLAY_TO_INPUT.put('Ⓦ', 'W');
        DISPLAY_TO_INPUT.put('Ⓧ', 'X');
        DISPLAY_TO_INPUT.put('Ⓨ', 'Y');
        DISPLAY_TO_INPUT.put('Ⓩ', 'Z');
        // 1-9
        DISPLAY_TO_INPUT.put('①', '1');
        DISPLAY_TO_INPUT.put('②', '2');
        DISPLAY_TO_INPUT.put('③', '3');
        DISPLAY_TO_INPUT.put('④', '4');
        DISPLAY_TO_INPUT.put('⑤', '5');
        DISPLAY_TO_INPUT.put('⑥', '6');
        DISPLAY_TO_INPUT.put('⑦', '7');
        DISPLAY_TO_INPUT.put('⑧', '8');
        DISPLAY_TO_INPUT.put('⑨', '9');
        DISPLAY_TO_INPUT.put('⓪', '0');
    }

    public static String generateCaptcha(int length) {
        StringBuilder captcha = new StringBuilder();
        StringBuilder displayCaptcha = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length()));
            captcha.append(c);
            displayCaptcha.append(convertToDisplay(c));
        }
        return captcha.toString() + ":" + displayCaptcha.toString();
    }

    public static char convertToDisplay(char c) {
        for (Map.Entry<Character, Character> entry : DISPLAY_TO_INPUT.entrySet()) {
            if (entry.getValue() == c) {
                return entry.getKey();
            }
        }
        return c;
    }

    public static String convertInputToOriginal(String input) {
        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            result.append(DISPLAY_TO_INPUT.getOrDefault(c, c));
        }
        return result.toString();
    }
}