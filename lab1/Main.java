public class Main {
    public static void main(String[] args) {
        double[] inputSample = {41, 295, 842, 1365, 66, 1093, 102, 5340,
                343, 1539, 333, 933, 854, 1630, 1434, 279,
                1029, 574, 787, 2701, 1644, 853, 456, 846,
                490, 328, 2360, 384, 1202, 1061, 342, 246,
                445, 715, 736, 295, 345, 772, 41, 1510, 315,
                889, 409, 809, 66, 64, 752, 746, 45, 1774,
                469, 1508, 212, 581, 1136, 266, 186, 720,
                295, 4668, 139, 586, 254, 2023, 1502, 292,
                946, 1779, 152, 291, 7, 979, 646, 58, 2115,
                957, 897, 5961, 392, 832, 2091, 2, 265, 187,
                823, 1464, 449, 258, 533, 36, 525, 1571,
                707, 1160, 1576, 25, 609, 486, 48, 230};

        double gamma = 0.95;
        double uptime = 5695;
        double intensityTime = 1670;

        System.out.print("Початкова вибірка:");
        printSample(inputSample);
        System.out.print("Вибірка після сортування:");
        bubbleSort(inputSample);
        printSample(inputSample);
        findAverage(inputSample);
        double[] intervals = findIntervals(inputSample);
        double[] failureChanceDensity = findFailureChanceDensity(inputSample, intervals);
        double[] uptimeProbability = findUptimeProbability(failureChanceDensity, intervals);
        findStatisticalMTBF(uptimeProbability, intervals, gamma);
        findUptimeProbability(failureChanceDensity, intervals, uptime);
        findFailureRate(failureChanceDensity, intervals, intensityTime);

    }

    private static void findAverage(double[] input) {
        double average = 0;

        for (double number : input) {
            average += number / input.length;
        }
        System.out.printf("Середнє значення вибірки: %.2f\n\n", average);
    }

    private static void bubbleSort(double[] input) {
        boolean isEnd;
        double temp;
        while (true) {
            isEnd = true;
            for (int j = 0; j < input.length - 1; j++) {
                if (input[j] > input[j + 1]) {
                    temp = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = temp;
                    isEnd = false;
                }
            }
            if (isEnd) {
                break;
            }
        }
    }

    private static double[] findIntervals(double[] input) {
        double max = 0;
        double min = 0;
        double step;
        double[] intervals = new double[11];

        for (int i = input.length - 1; i >= 0; i--) {
            if (input[i] > max) {
                max = input[i];
            }
        }

        step = (max - min) / 10;

        for (int i = 0; i < intervals.length; i++) {
            intervals[i] = i * step;
        }

        System.out.printf("Максимальне значення вибірки: %.2f\n", max);
        System.out.printf("Довжина інтервалу: %.2f\n", step);
        System.out.println("Отримали наступні інтервали:");
        for (int i = 0; i < intervals.length - 1; i++) {
            System.out.printf("%d-й інтервал від %.2f до %.2f\n", i + 1, intervals[i], intervals[i + 1]);
        }
        System.out.println();
        return intervals;
    }

    private static double[] findFailureChanceDensity(double[] input, double[] intervals) {
        double[] failureChanceDensity = new double[10];

        for (int i = 0; i < failureChanceDensity.length; i++) {
            failureChanceDensity[i] = countOnInterval(input, intervals[i], intervals[i + 1]);
            failureChanceDensity[i] = failureChanceDensity[i] / (input.length * (intervals[i + 1] - intervals[i]));
        }

        System.out.println("Значення статистичної щільності розподілу ймовірності відмови для кожного інтервалу:");
        for (int i = 0; i < failureChanceDensity.length; i++) {
            System.out.printf("Для %d-го інтервалу f%d = %.6f\n", i + 1, i + 1, failureChanceDensity[i]);
        }
        System.out.println();

        return failureChanceDensity;
    }

    private static int countOnInterval(double[] input, double start, double end) {
        int count = 0;

        for (double number : input) {
            if (number > start && number <= end) {
                count++;
            }
        }

        return count;
    }

    private static double[] findUptimeProbability(double[] failureChanceDensity, double[] intervals) {
        double[] uptimeProbability = new double[11];

        uptimeProbability[0] = 1;
        double p = 0;
        for (int i = 0; i < failureChanceDensity.length; i++) {
            p += failureChanceDensity[i] * (intervals[i + 1] - intervals[i]);
            uptimeProbability[i + 1] = 1 - p;
        }

        System.out.println("Значення ймовірності безвідмовної роботи пристрою на час правої границі для кожного інтервалу:");
        System.out.printf("P(0) = %.4f\n", uptimeProbability[0]);
        for (int i = 1; i < uptimeProbability.length; i++) {
            System.out.printf("Для %d-го інтервалу P(%.2f) = %.4f\n", i, intervals[i], uptimeProbability[i]);
        }
        System.out.println();

        return uptimeProbability;
    }

    private static double findUptimeProbability(double[] failureChanceDensity, double[] intervals, double time) {
        double probability = 1;

        for (int i = 0; i < intervals.length - 1; i++) {
            if (time <= intervals[i + 1] && time >= intervals[i]) {
                double step = intervals[i + 1] - intervals[i];
                for (int j = 0; j < i; j++) {
                    probability -= failureChanceDensity[j] * step;
                }
                probability -= failureChanceDensity[i] * (time - intervals[i]);
            }
        }

        System.out.printf("Ймовірність безвідмовної роботи на час %.2f годин: P(%.2f) = %.4f\n\n", time, time, probability);

        return probability;
    }

    private static double findStatisticalMTBF(double[] uptimeProbability, double[] intervals, double gamma) {
        double statisticalMTBF = -1;

        for (int i = 0; i < uptimeProbability.length - 1; i++) {
            if (uptimeProbability[i] > gamma && uptimeProbability[i + 1] < gamma) {
                double d = (uptimeProbability[i + 1] - gamma) / (uptimeProbability[i + 1] - uptimeProbability[i]);
                double step = intervals[i + 1] - intervals[i];
                statisticalMTBF = step - step * d;
            }
        }

        System.out.printf("Статистичний γ-відсотковий наробіток на відмову: %.4f\n\n", statisticalMTBF);

        return statisticalMTBF;
    }

    private static double findFailureRate(double[] failureChanceDensity, double[] intervals, double time) {
        double failureRate = 0;
        double probabilityOverTime = findUptimeProbability(failureChanceDensity, intervals, time);

        for (int i = 0; i < intervals.length - 1; i++) {
            if (time <= intervals[i + 1] && time >= intervals[i]) {
                failureRate += failureChanceDensity[i] / probabilityOverTime;
            }
        }

        System.out.printf("Інтенсивність відмов на час %.2f годин: λ(%.2f) = %.4f\n\n", time, time, failureRate);

        return failureRate;
    }

    private static void printSample(double[] input) {
        for (int i = 0; i < input.length; i++) {
            if (i % 10 == 0) {
                System.out.println();
                System.out.print('\t');
            }
            if (i != input.length - 1) {
                System.out.print(input[i] + ", ");
            } else {
                System.out.println(input[i] + "\n");
            }
        }
    }
}