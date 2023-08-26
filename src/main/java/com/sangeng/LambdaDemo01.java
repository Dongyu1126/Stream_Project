package com.sangeng;

import java.util.function.IntBinaryOperator;

public class LambdaDemo01 {
//    public static void main(String[] args) {
//        new Thread(() -> System.out.println("小张子，不老实！")).start();
//    }

    public static int calculateNum(IntBinaryOperator operator){
        int a = 10;
        int b = 20;
        return operator.applyAsInt(a, b);
    }

    public static void main(String[] args) {
        int i = calculateNum((left, right) -> left + right);
        System.out.println(i);
    }
}
