package es.ikerperez.binaryconverter.utils;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

import timber.log.Timber;

/**
 * Creado por Iker Pérez Brunelli <DarkerTV> a fecha de 30/09/2016.
 */

public class RootConverter {

    @Nullable
    public static String parseNumber(String value, String origin, String target) {
        Timber.i("Conversion from base %s to base %s using value %s.", origin, target, value);

        value = value.replaceAll("\\s+", "");
        String[] parts = ConverterUtil.getParts(value);
        Character split = ConverterUtil.getSplit(value);
        int parsedOrigin = Integer.parseInt(origin);
        int parsedTarget = Integer.parseInt(target);

        try {
            if (parts == null) {
                return ConverterUtil.formatResult(parsedTarget,
                        new BigInteger(value, parsedOrigin).toString(parsedTarget), null, null);
            }

            if (parts.length > 2) {
                return null;
            }

            if (parts.length == 1 || parts[1].matches("0+")) {
                return ConverterUtil.formatResult(parsedTarget,
                        new BigInteger(parts[0], parsedOrigin).toString(parsedTarget), null, null);
            } else {
                String baseFraction;

                if (parsedOrigin == 10) {
                    baseFraction =
                            ConverterUtil.decimalFractionToBase(parts[1], parsedTarget);
                } else {
                    String decimalFraction =
                            ConverterUtil.baseFractionToDecimal(parts[1], parsedOrigin);
                    baseFraction =
                            ConverterUtil.decimalFractionToBase(decimalFraction, parsedTarget);
                }

                return ConverterUtil.formatResult(parsedTarget,
                        new BigInteger(parts[0], parsedOrigin).toString(parsedTarget),
                        baseFraction, split);
            }
        } catch (NumberFormatException|ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static String operation(String firstValue, String secondValue, String base,
                                   String operator) {
        Timber.i("Operation using %s and %s on base %s and operator %s.", firstValue, secondValue,
                base, operator);

        int parsedBase = Integer.parseInt(base);
        Character split = ConverterUtil.getSplit(firstValue);

        if (split == null) {
            split = '.';
        }

        if (parsedBase != 10) {
            firstValue = ConverterUtil.parseToDecimalOperation(firstValue, parsedBase);
            secondValue = ConverterUtil.parseToDecimalOperation(secondValue, parsedBase);
        }

        if (firstValue == null || secondValue == null) {
            return null;
        }

        if (firstValue.contains(",") || secondValue.contains(",")) {
            firstValue = firstValue.replace(",", ".");
            secondValue = secondValue.replace(",", ".");
        }

        BigDecimal parsedFirstValue = new BigDecimal(firstValue);
        BigDecimal parsedSecondValue = new BigDecimal(secondValue);
        BigDecimal result = null;

        switch (operator) {
            case "+":
                result = parsedFirstValue.add(parsedSecondValue);
                break;
            case "-":
                result = parsedFirstValue.subtract(parsedSecondValue);
                break;
            case "*":
                result = parsedFirstValue.multiply(parsedSecondValue);
                break;
            case "/":
                result = parsedFirstValue.divide(parsedSecondValue, ConverterUtil.SIMPLE_MANTISSA, //TODO Change this
                        BigDecimal.ROUND_HALF_EVEN);
                break;
            case "^":
                try {
                    result = new BigDecimal(
                            Math.pow(parsedFirstValue.doubleValue(),
                                    parsedSecondValue.doubleValue()));
                } catch (NumberFormatException e) {
                    result = null;
                }
        }

        if (result == null) {
            return null;
        } else {
            String parsed = parseNumber(result.toPlainString(), "10", base);

            if (parsed == null) {
                return null;
            }

            return TextUtils.join(String.valueOf(split), parsed.split("\\."));
        }
    }

    public static String parseOrigin(String value, String target) {
        value = value.replaceAll("\\s+", "");
        String[] parts = ConverterUtil.getParts(value);
        Character split = ConverterUtil.getSplit(value);
        int parsedTarget = Integer.parseInt(target);

        if (parts == null) {
            return ConverterUtil.formatResult(parsedTarget, value, null, null);
        }

        if (parts.length == 1 || parts[1].matches("0+")) {
            return ConverterUtil.formatResult(parsedTarget, parts[0], null, null);
        } else {
            return ConverterUtil.formatResult(parsedTarget, parts[0], parts[1], split);
        }
    }
}
