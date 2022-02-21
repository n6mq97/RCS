import java.util.ArrayList;
import java.util.List;

public class Lab2 {
    public static double findProbability(int[][] connectionsTable, double[] localProbability) {
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
                findWays(connectionsTable, i, -1, "0".repeat(i), ways);
            }
        }

        List<String> suitableCombinations = enumerate(ways);
        return findProbability(suitableCombinations, localProbability);
    }

    private static void findWays(int[][] connectionsTable, int line, int parent, String pathAccumulator, List<String> ways) {
        pathAccumulator += 1;
        boolean isFinal = true;

        for (int i = 0; i < connectionsTable.length; i++) {
            if(connectionsTable[line][i] == 1 && parent != i) {
                findWays(connectionsTable, i, line, pathAccumulator, ways);
                isFinal = false;
            }
            if (i > line) {
                pathAccumulator += 0;
            }
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

    private static double findProbability(List<String> suitableCombinations, double[] localProbability) {
        double probability = 0;
        double partialProbability;

        for (String combination : suitableCombinations) {
            partialProbability = 1;
            for (int i = 0; i < combination.length(); i++) {
                if (Character.digit(combination.charAt(i), 10) == 1) {
                    partialProbability *= localProbability[i];
                } else {
                    partialProbability *= 1 - localProbability[i];
                }
            }
            probability += partialProbability;
        }

        return probability;
    }
}