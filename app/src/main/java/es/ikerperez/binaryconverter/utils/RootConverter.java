package es.ikerperez.binaryconverter.utils;

import android.support.annotation.Nullable;

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
                String decimalFraction =
                        ConverterUtil.baseFractionToDecimal(parts[1], parsedOrigin);
                String baseFraction =
                        ConverterUtil.decimalFractionToBase(decimalFraction, parsedTarget);

                return ConverterUtil.formatResult(parsedTarget,
                        new BigInteger(parts[0], parsedOrigin).toString(parsedTarget),
                        baseFraction, split);
            }
        } catch (NumberFormatException e) {
            return null;
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
