package rut.imdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<Integer> key = new ArrayList<>();
    private static List<Integer> keyTwo = new ArrayList<>();
    public static void main(String[] args) {
        System.out.print("Введите кол-во столбцов таблиц: ");
        int m = Integer.parseInt(sc.nextLine());

        System.out.print("Введите кол-во строк таблиц: ");
        int n = Integer.parseInt(sc.nextLine());

        System.out.printf("Введите код для шифрования (количество = %d): ", m);
        String values = sc.nextLine();
        key = Arrays.stream(values.split(" ")).map(Integer::parseInt).collect(Collectors.toList());

        System.out.printf("Введите координаты пустых ячеек (формат x,y x,y...) столбцы x < %d, строки y < %d): ", m, n);
        String[] inKeyTwo = sc.nextLine().split(" ");
        for (int i = 0; i < inKeyTwo.length; i++) {
            keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[0]));
            keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[1]));
        }
    }
}
