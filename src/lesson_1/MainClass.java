package lesson_1;

import java.util.ArrayList;

public class MainClass {

    public static void main(String[] args) {
        Integer[] intArr = new Integer[5];
        intArr[0]=1;
        intArr[1]=2;
        intArr[2]=3;
        intArr[3]=4;
        intArr[4]=5;

        String[] strArr = new String[5];
        strArr[0]=("Один");
        strArr[1]=("Два");
        strArr[2]=("Три");
        strArr[3]=("Четыре");
        strArr[4]=("Пять");

        SwapTwoArrEl(intArr,0,2);
        SwapTwoArrEl(strArr,0,1);

        for (int a: intArr){
            System.out.println(a);
        }
        for (String a: strArr){
            System.out.println(a);
        }
        ArrayList<String> modStrArr = new ArrayList<>();
        convertArrayToArrayList(modStrArr,strArr);
        System.out.println("AL check "+modStrArr.get(4));

        //3.
        Apple oneAp = new Apple();
        Apple twoAp = new Apple();
        Apple threeAp = new Apple();

        Apple oneApSecond = new Apple();
        Apple twoApSecond = new Apple();
        Apple threeApSecond = new Apple();

        Orange oneOr = new Orange();
        Orange twoOr = new Orange();
        Orange threeOr = new Orange();

        ArrayList<Orange> threeOrFruit = new ArrayList<>();
        threeOrFruit.add(oneOr);
        threeOrFruit.add(twoOr);
        threeOrFruit.add(threeOr);

        Box<Apple> appleBox1 = new Box<>(oneAp, twoAp, threeAp);
        Box<Apple> appleBox2 = new Box<>(oneApSecond, twoApSecond, threeApSecond);
        Box<Orange> orangeBox3 = new Box<>(threeOrFruit);

        System.out.println("aB1 weigth " + appleBox1.getWeight());
        System.out.println("compare aB1/oB3 "+appleBox1.compare(orangeBox3));

        appleBox1.moveToAnotherBox(appleBox2);

        System.out.println("aB1 + aB2 weigth (print aB2) " + appleBox2.getWeight());
        System.out.println("deleted aB1 weigth " + appleBox1.getWeight());

        appleBox1.add(oneAp);

        System.out.println("added aB1 " + appleBox1.getWeight());

    }
    // 1.
    public static <T> void SwapTwoArrEl(T[] modArray, int elem1, int elem2) {
        T tmp = modArray[elem1];
        modArray[elem1] = modArray[elem2];
        modArray[elem2] = tmp;
    }

    // 2.
    public static <T> void convertArrayToArrayList(ArrayList<T> listArray, T[] convArray) {
        for (T elem : convArray) {
            listArray.add(elem);
        }
    }
}
