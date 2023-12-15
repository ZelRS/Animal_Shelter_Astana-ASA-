package pro.sky.telegramBot.utils.statistic;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ReportSumCalculator {
    public Integer calculateReportSum(int[] values) {

        int totalSum = 0;

        int[] b = new int[values.length];


        for (int i = 0; i < values.length; i++) {
            b[i] = values[i] * values[i];
        }

        long nonZeroCount = Arrays.stream(values).filter(val -> val != 0).count();

        int[] a2 = new int[(int) nonZeroCount];
        int[] b2 = new int[(int) nonZeroCount];

        for (int i = 0, j = 0; i < values.length && j < a2.length; i++) {
            if (values[i] != 0) {
                a2[j] = values[i];
                b2[j++] = b[i];
            }
        }

        for (int i = 0; i < b2.length; i++) {
            int sum = b2[i];
            for (int j = i + 1; j < a2.length && j - (i + 1) < a2[i]; j++) {
                sum += a2[j];
            }
            totalSum += sum;
        }
        return totalSum;
    }
}