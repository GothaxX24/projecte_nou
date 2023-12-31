import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Shoe> shoes = new ArrayList<>();
    private static FilesDAO filesDAO = new FilesDAO();

    public static void main(String[] args) {
        boolean opcioInt;
        int opcio = 0;
        while (opcio != 3) {
            System.out.println("\nMenú:");
            System.out.println("1. Ordenació Iterativa");
            System.out.println("2. Ordenació Especial");
            System.out.println("\n3. Sortir");
            System.out.print("Opcio: ");

             opcioInt = scanner.hasNextInt();
             if (opcioInt) {
                 opcio = scanner.nextInt();
             }
             scanner.nextLine();

            switch (opcio) {
                case 1 -> ordenacioIterativaMenu();
                case 2 -> ordenacioRecursiva();
                case 3 -> System.out.println("Bye!");
                default -> System.out.println("Opció no valida. Try again.");
            }
        }
    }

    private static void ordenacioRecursiva() {
        int numberOfLines;
        int x = 200;
        long startTime, endTime, executionQuickTime, executionMergeTime;

        //System.out.print("\nElige numero de muestras a tomar [1 - " + filesDAO.getMaxLines("random") + "]: ");
        //numberOfLines = scanner.nextInt();
        //scanner.nextLine();
        while(true) {
            shoes = filesDAO.readShoeFile("random", x);


            startTime = System.currentTimeMillis();
            quickSort(shoes, 0, shoes.size() - 1);
            endTime = System.currentTimeMillis();
            executionQuickTime = endTime - startTime;

            printShoes(shoes, x);

            shoes = filesDAO.readShoeFile("random", x);
            startTime = System.currentTimeMillis();
            mergeSort(shoes);
            endTime = System.currentTimeMillis();
            executionMergeTime = endTime - startTime;

            printShoes(shoes, x);
            System.out.println("\nExecution Time of Quick Sort: " + executionQuickTime + " milisegundos");
            System.out.println("\nExecution Time of Merge Sort: " + executionMergeTime + " milisegundos");
            guardarTiemposEnArchivo(executionQuickTime, executionMergeTime, x);
            if (x >= 50000) {
                break;
            }
            x += 200;
        }
    }

    public static void printShoes(ArrayList<Shoe> shoes, int num) {
        if (num >= 50000) {
            for (Shoe s : shoes) {
                System.out.println(s.getName() + " - " + s.getCombination());
            }
        }
    }



    private static void ordenacioIterativaMenu() {
        boolean opcioInt;
        int opcio = 0;

        System.out.println("Ordenación Iterativa, quin fitxer vols provar?");
        System.out.println("1. Ascendente");
        System.out.println("2. Descendente");
        System.out.println("3. Aleatorio");
        System.out.print("\nSeleccione el tipo de dataset: ");

        opcioInt = scanner.hasNextInt();
        if (opcioInt) {
            opcio = scanner.nextInt();
        }
        scanner.nextLine();

        String dataset;

        switch (opcio) {
            case 1 -> dataset = "ascending";
            case 2 -> dataset = "descending";
            case 3 -> dataset = "random";
            default -> {
                System.out.println("Opció no válida.");
                return;
            }
        }

        ordenacioIterativa(dataset);
    }

    private static void ordenacioIterativa(String dataset) {
        long startTime, endTime, executionTime;
        int x = 200;
        while(true) {

            // Insertion Sort
            shoes = filesDAO.readShoeFile(dataset, x);
            startTime = System.currentTimeMillis();
            insertionSort(shoes);
            endTime = System.currentTimeMillis();
            long executionInsertionTime = endTime - startTime;
            if (x >= 10000) {
                System.out.println("\nCheck Insertion Order: ");
                for (Shoe shoe : shoes) {
                    System.out.println("Name: " + shoe.getName());
                }
            }
            // Selection Sort
            shoes = filesDAO.readShoeFile(dataset, x);
            startTime = System.currentTimeMillis();
            selectionSort(shoes);
            endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;

            if (x >= 10000) {
                System.out.println("\nCheck Selection Order: ");
                for (Shoe shoe : shoes) {
                    System.out.println("Name: " + shoe.getName());
                }
            }
            guardarTiemposEnArchivo(executionInsertionTime, executionTime, x);
            if (x >= 10000) {
                break;
            }
            x+=200;
        }
    }
    private static void guardarTiemposEnArchivo(long insertionTime, long selectionTime, int x) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("exec_time.txt", true))) {
            writer.write(insertionTime+","+selectionTime+","+x);
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void selectionSort(ArrayList<Shoe> shoes) {
        int size = shoes.size();

        for (int i = 0; i < size - 1; i++) {
            int minimum = i;

            for (int j = i + 1; j < size; j++) {
                if (shoes.get(j).getName().compareToIgnoreCase(shoes.get(minimum).getName()) < 0) {
                    minimum = j;
                }
            }

            Shoe tempShoe = shoes.get(i);
            shoes.set(i, shoes.get(minimum));
            shoes.set(minimum, tempShoe);
        }
    }


    private static void insertionSort(ArrayList<Shoe> shoes) {
        int size = shoes.size();

        for (int i = 1; i < size; i++) {
            Shoe currentShoe = shoes.get(i);
            int j = i - 1;

            while (j >= 0 && shoes.get(j).getName().compareToIgnoreCase(currentShoe.getName()) > 0) {
                shoes.set(j + 1, shoes.get(j));
                j--;
            }

            shoes.set(j + 1, currentShoe);
        }
    }



    public static void quickSort(ArrayList<Shoe> shoes, int min, int max) {
        if (max <= min) {
            return;
        }
        Random rand = new Random();
        int pivotIndex = min + rand.nextInt(max - min + 1);
        Shoe pivot = shoes.get(pivotIndex);

        swap(shoes, pivotIndex, max);

        int pointer = partition(shoes, min, max, pivot);

        quickSort(shoes, min, pointer - 1);
        quickSort(shoes, pointer + 1, max);
    }

    private static int partition(ArrayList<Shoe> shoes, int min, int max, Shoe pivot) {
        int i = min - 1;
        for (int j = min; j <= max - 1; j++) {
            if (shoes.get(j).getCombination() > pivot.getCombination()) {
                i++;
                swap(shoes, i, j);
            }
        }
        i++;

        swap(shoes, i, max);

        return i;
    }

    private static void swap(ArrayList<Shoe> shoes, int i, int j) {
        Shoe temp = shoes.get(i);
        shoes.set(i, shoes.get(j));
        shoes.set(j, temp);
    }

    public static void mergeSort(ArrayList<Shoe> shoes) {
        if (shoes.size() <= 1) {
            return;
        }

        int middle = shoes.size() / 2;

        ArrayList<Shoe> firstHalf = new ArrayList<>(shoes.subList(0, middle));
        ArrayList<Shoe> secondHalf = new ArrayList<>(shoes.subList(middle, shoes.size()));

        mergeSort(firstHalf);
        mergeSort(secondHalf);

        combination(firstHalf, secondHalf, shoes);
    }

    private static void combination(ArrayList<Shoe> firstShoes, ArrayList<Shoe> secondShoes, ArrayList<Shoe> shoes) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < firstShoes.size() && j < secondShoes.size()) {
            if (secondShoes.get(j).getCombination() > firstShoes.get(i).getCombination()) {
                shoes.set(k++, secondShoes.get(j++));
            } else {
                shoes.set(k++, firstShoes.get(i++));
            }
        }

        while (i < firstShoes.size()) {
            shoes.set(k++, firstShoes.get(i++));
        }

        while (j < secondShoes.size()) {
            shoes.set(k++, secondShoes.get(j++));
        }
    }






}
