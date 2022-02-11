import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        int[][] connectionsTable = {{0, 1, 1, 0, 0, 0, 0, 0},
//                                    {0, 0, 0, 1, 1, 0, 0, 0},
//                                    {0, 0, 0, 1, 0, 1, 0, 1},
//                                    {0, 0, 0, 0, 1, 1, 0, 1},
//                                    {0, 0, 0, 0, 0, 1, 1, 0},
//                                    {0, 0, 0, 0, 0, 0, 1, 1},
//                                    {0, 0, 0, 0, 0, 0, 0, 0},
//                                    {0, 0, 0, 0, 0, 0, 0, 0}};
//
//        double[] localProbability = {0.5, 0.6, 0.7, 0.8, 0.85, 0.9, 0.92, 0.94};
        int[][] connectionsTable = {{0, 1, 0, 0, 0, 0, 0},
                                    {0, 0, 1, 1, 0, 0, 0},
                                    {0, 0, 0, 0, 1, 0, 1},
                                    {0, 0, 0, 0, 0, 1, 0},
                                    {0, 0, 0, 0, 0, 1, 1},
                                    {0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0}};

        double[] localProbability = {0.8, 0.42, 0.72, 0.3, 0.6, 0.79, 0.7};
//        int[][] connectionsTable = {{0, 0, 1, 1, 1, 0},
//                {0, 0, 1, 1, 0, 1},
//                {0, 0, 0, 0, 1, 1},
//                {0, 0, 0, 0, 1, 1},
//                {0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0}};
//
//        double[] localProbability = {0.74, 0.14, 0.56, 0.35, 0.2, 0.21};

        printConnectionsTable(connectionsTable);
        printLocalProbability(localProbability);
        List<String> ways = new ArrayList<>();
        for (int i = 0; i < connectionsTable.length; i++) {
            boolean isStart = true;
            for (int j = 0; j < connectionsTable.length; j++) {
                if (connectionsTable[j][i] == 1) {
                    isStart = false;
                    break;
                }
            }
            if (isStart) {
                findWays(connectionsTable, i, "0".repeat(i), ways);
            }
        }
        printWays(ways);
        List<String> suitableCombinations = enumerate(ways);
        findProbability(suitableCombinations, localProbability);
    }

    private static void findWays(int[][] connectionsTable, int line, String pathAccumulator, List<String> ways) {
        pathAccumulator += 1;
        boolean isFinal = true;

        for (int i = line + 1; i < connectionsTable.length; i++) {
            if(connectionsTable[line][i] == 1) {
                findWays(connectionsTable, i, pathAccumulator, ways);
                isFinal = false;
            }
            pathAccumulator += 0;
        }

        if (isFinal) {
            ways.add(pathAccumulator);
        }
    }

    private static List<String> enumerate(List<String> masks) {
        List<String> suitableCombinations = new ArrayList<>();

        for (int bin = (int) Math.pow(2, masks.get(0).length()) - 1; bin > 0; bin--) {
            String combination = Integer.toBinaryString(bin);
            combination = "0".repeat(masks.get(0).length() - combination.length()) + combination;

            for (String mask : masks) {
                boolean isMatches = true;
                for (int i = 0; i < mask.length(); i++) {
                    if (Character.digit(mask.charAt(i), 10) == 1 &&
                        Character.digit(combination.charAt(i), 10) != 1) {
                        isMatches = false;
                        break;
                    }
                }

                if (isMatches) {
                    suitableCombinations.add(combination);
                    break;
                }
            }
        }

        return suitableCombinations;
    }

    private static void findProbability(List<String> suitableCombinations, double[] localProbability) {
        double probability = 0;
        double partialProbability;

        System.out.println("Таблиця працездатних станів системи:");
        for (int i = 0; i < suitableCombinations.get(0).length(); i++) {
            System.out.print("E" + (i + 1) + "\t");
        }
        System.out.println("\tЙмовірність");

        for (String combination : suitableCombinations) {
            partialProbability = 1;
            for (int i = 0; i < combination.length(); i++) {
                if (Character.digit(combination.charAt(i), 10) == 1) {
                    partialProbability *= localProbability[i];
                    System.out.print("+\t");
                } else {
                    partialProbability *= 1 - localProbability[i];
                    System.out.print("-\t");
                }
            }
            probability += partialProbability;
            System.out.printf("\t%f\n", partialProbability);
        }

        System.out.println("\nЙмовірність безвідмовної роботи системи: " + probability);
    }

    private static void printWays(List<String> ways) {
        System.out.println("Шляхи якими можна пройти від початку до кінця:");

        for (String way : ways) {
            boolean isFirstElement = true;
            for (int i = 0; i < way.length(); i++) {
                if (Character.digit(way.charAt(i), 10) == 1) {
                    if (isFirstElement) {
                        System.out.print("E" + (i + 1));
                        isFirstElement = false;
                    } else {
                        System.out.print(" -> E" + (i + 1));
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printConnectionsTable(int[][] connectionsTable) {
        System.out.println("Таблиця зв'язків системи:");
        System.out.print("\t");

        for (int i = 0; i < connectionsTable.length; i++) {
            System.out.print("E" + (i + 1) + "\t");
        }
        System.out.println();

        for (int i = 0; i < connectionsTable.length; i++) {
            for (int j = 0; j < connectionsTable[0].length; j++) {
                if (j == 0) {
                    System.out.print("E" + (i + 1) + "\t" + connectionsTable[i][j]);
                } else {
                    System.out.print("\t" + connectionsTable[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void printLocalProbability(double[] localProbability) {
        System.out.println("Ймовірності безвідмовної роботи системи:");
        for (int i = 0; i < localProbability.length; i++) {
            System.out.printf("P%d = %.2f\n", i + 1, localProbability[i]);
        }
        System.out.println();
    }
}
