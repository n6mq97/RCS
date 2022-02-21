public class Lab3 {
    public static void main(String[] args) {
        int[][] connectionsTable = {{0, 1, 0, 0, 0, 0, 0},
                                    {0, 0, 1, 1, 0, 0, 0},
                                    {0, 0, 0, 0, 1, 0, 1},
                                    {0, 0, 0, 0, 0, 1, 0},
                                    {0, 0, 0, 0, 0, 1, 1},
                                    {0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0}};

        double[] localProbability = {0.8, 0.42, 0.72, 0.3, 0.6, 0.79, 0.7};
        int time = 1417;
        int k1 = 2;
        int k2 = 3;
        boolean k1isGeneral = false;
        boolean k1isLoaded = true;
        boolean k2isGeneral = true;
        boolean k2isLoaded = true;

        double P = Lab2.findProbability(connectionsTable, localProbability);
        double P1;
        double P2;

        P1 = k1isGeneral ? findGeneral(P, k1, k1isLoaded)
                         : findSeparated(connectionsTable, localProbability, k1, k1isLoaded);

        P2 = k2isGeneral ? findGeneral(P, k2, k2isLoaded)
                         : findSeparated(connectionsTable, localProbability, k2, k2isLoaded);

        System.out.printf("### 1. %s %s резервування з кратністю %d ###\n\n",
                k1isGeneral ? "Загальне" : "Роздільне",
                k1isLoaded ? "навантажене" : "ненавантажене",
                k1);
        printWin(P, P1, time);
        System.out.printf("### 2. %s %s резервування з кратністю %d ###\n\n",
                k2isGeneral ? "Загальне" : "Роздільне",
                k2isLoaded ? "навантажене" : "ненавантажене",
                k2);
        printWin(P, P2, time);
    }

    private static double findGeneral(double probability, int k, boolean isLoaded) {
        return isLoaded ? 1 - (1 - Math.pow(1 - probability, k + 1))
                        : 1 - (1 - probability) / factorial(k + 1);
    }

    private static double findSeparated(int[][] connectionsTable, double[] localProbability, int k, boolean isLoaded) {
        double[] newLocalProbability = new double[localProbability.length];

        if (isLoaded) {
            for (int i = 0; i < newLocalProbability.length; i++) {
                newLocalProbability[i] = Math.pow(1 - localProbability[i], k + 1);
                newLocalProbability[i] = 1 - newLocalProbability[i];
            }
        } else {
            for (int i = 0; i < newLocalProbability.length; i++) {
                newLocalProbability[i] = 1 - (1 - localProbability[i]) / factorial(k + 1);
            }
        }

        return Lab2.findProbability(connectionsTable, newLocalProbability);
    }

    private static void printWin(double oldProbability, double newProbability, double time) {
        double GQ = (1 - newProbability) / (1 - oldProbability);
        double GP = newProbability / oldProbability;
        int TOld = - (int) (time / Math.log(oldProbability));
        int TNew = - (int) (time / Math.log(newProbability));
        double GT = (double) TNew / TOld;

        System.out.printf("Ймовірність безвідмовної роботи системи без резервування: %.4f\n\n", oldProbability);
        System.out.printf("Ймовірність відмови системи без резервування: %.4f\n\n", 1 - oldProbability);
        System.out.printf("Середній наробіток на відмову для системи без резервування: %d\n\n", TOld);
        System.out.printf("Ймовірність безвідмовної роботи системи з резервуванням: %.4f\n\n", newProbability);
        System.out.printf("Ймовірність відмови системи з резервуванням: %.4f\n\n", 1 - newProbability);
        System.out.printf("Середній наробіток на відмову для системи з резервуванням: %d\n\n", TNew);
        System.out.printf("Виграш надійності протягом часу t за ймовірністю безвідмовної роботи: %.2f\n\n", GP);
        System.out.printf("Виграш надійності протягом часу t за ймовірністю відмов: %.2f\n\n", GQ);
        System.out.printf("Виграш надійності за середнім часом безвідмовної роботи: %.2f\n\n", GT);
    }

    private static int factorial(int n) {
        int factorial = 1;
        for (int i = 2; i <= n; i++) {
            factorial *= i;
        }
        return factorial;
    }
}