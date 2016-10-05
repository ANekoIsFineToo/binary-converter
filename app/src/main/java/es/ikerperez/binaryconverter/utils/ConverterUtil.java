package es.ikerperez.binaryconverter.utils;

import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 30/09/2016.
 */

public class ConverterUtil {

    // Limit to avoid never end recursions.
    public static final int DECIMAL_LIMIT = 256;

    public static String decimalFractionToBase(String decimal, int base) {
        double Decimal = Double.parseDouble(String.format("0.%s", decimal));
        String parsedDecimal = "";
        int decimalAmount = 0;
        boolean running = true;

        while (running) {
            double computed = Decimal * base;
            String[] decimalParts = ConverterUtil.getParts(String.valueOf(computed));
            parsedDecimal += Integer.toString(Integer.parseInt(decimalParts[0], 10), base);

            if (decimalParts[1].equals("0")) {
                running = false;
            } else {
                Decimal = Double.parseDouble(String.format("0.%s", decimalParts[1]));
            }

            decimalAmount++;

            if (decimalAmount >= DECIMAL_LIMIT) {
                running = false;
            }
        }

        return parsedDecimal;
    }

    public static String baseFractionToDecimal(String fraction, int base) {
        String decimal = ConverterUtil.removeTrailingZeroes(fraction);
        char[] decimals = decimal.toCharArray();
        double parsedDecimal = 0.0;

        for (int i = 0; i < decimal.length(); i++) {
            int number = Integer.parseInt(String.valueOf(decimals[i]), base);
            int exponent = (i + 1) * -1;

            parsedDecimal += number * Math.pow(base, exponent);
        }

        return new BigDecimal(parsedDecimal).toPlainString().split("\\.")[1];
    }

    public static String parseToDecimalOperation(String value, int base) {
        String parts[] = getParts(value);

        try {
            if (parts == null) {
                return new BigInteger(value, base).toString();
            }

            if (parts.length > 2) {
                return null;
            }

            if (parts.length == 1 || parts[1].matches("0+")) {
                return new BigInteger(parts[0], base).toString();
            } else {
                String decimalFraction = baseFractionToDecimal(parts[1], base);

                return new BigInteger(parts[0], base).toString() + '.' + decimalFraction;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String formatResult(int base, String value, @Nullable String decimals,
                                      @Nullable Character split) {
        int pre = 4;
        int post = 4;

        if (base == 10) {
            pre = 3;
            post = 5;
        }

        List<String> splitValue = splitStringEqually(value, pre, true);
        String returnValue = "";

        for (String sValue: splitValue) {
            if (returnValue.isEmpty()) {
                returnValue += sValue;
            } else {
                returnValue += " " + sValue;
            }
        }

        if (decimals == null || split == null) {
            return returnValue;
        }

        List<String> splitDecimals = splitStringEqually(decimals, post, false);
        String returnDecimals = "";

        for (String sDecimal: splitDecimals) {
            if (returnDecimals.isEmpty()) {
                returnDecimals += sDecimal;
            } else {
                returnDecimals += " " + sDecimal;
            }
        }

        return returnValue + split + returnDecimals;
    }

    public static String fillStart(String text, int segmentsLength) {
        while (text.length() % segmentsLength != 0) {
            text = '0' + text;
        }

        return text;
    }

    public static String fillEnd(String text, int segmentsLength) {
        text = removeTrailingZeroes(text);

        while (text.length() % segmentsLength != 0) {
            text = text + '0';
        }

        return text;
    }

    public static List<String> splitStringEqually(String text, int segmentsLength, boolean reverse) {
        if (reverse) {
            text = new StringBuilder(text).reverse().toString();
        }

        List<String> result =
                new ArrayList<>((text.length() + segmentsLength - 1) / segmentsLength);

        for (int i = 0; i < text.length(); i += segmentsLength) {
            String add = text.substring(i, Math.min(text.length(), i + segmentsLength));

            if (reverse) {
                result.add(new StringBuilder(add).reverse().toString());
            } else {
                result.add(add);
            }
        }

        if (reverse) {
            Collections.reverse(result);
            return result;
        } else {
            return result;
        }
    }

    public static String removeLeadingZeroes(String text) {
        return text.replaceFirst("^0*", "");
    }

    public static String removeTrailingZeroes(String text) {
        return text.replaceFirst("0*$", "");
    }

    public static String[] getParts(String text) {
        String[] result;

        if (text.contains(".")) {
            result = text.split("\\.");
        } else if (text.contains(",")) {
            result = text.split(",");
        } else {
            result = null;
        }

        return result;
    }

    public static Character getSplit(String text) {
        Character result;

        if (text.contains(".")) {
            result = '.';
        } else if (text.contains(",")) {
            result = ',';
        } else {
            result = null;
        }

        return result;
    }
}
