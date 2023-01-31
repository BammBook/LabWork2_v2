package Agents;

public class OptimizationFunctions {

    private double x;

    public static double FirstFunction(double x){
        return Math.exp(-0.5 * x);
    }

    public static double SecondFunction(double x){
        return 0.5 * x + 3;
    }

    public static double ThirdFunction(double x){
        return Math.cos(x);
    }
}
