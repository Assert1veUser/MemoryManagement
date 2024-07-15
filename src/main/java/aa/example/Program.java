package aa.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Random;

public class Program {
    public static final long fileMaxSizeBytes = 1024;
    public static final String dataFilePath = "src/main/resources/data.txt";
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayDeque<NewProcess> processes = new ArrayDeque<>();
        int processesAmount = 10;
        int id = 1;
        boolean exitProgram = false;
        while(!exitProgram){
            for(int i = 0; i < processesAmount; i++) {
                int choice = new Random().nextInt(2);
                if(choice == 1 && processes.size() == 0){
                    choice = 0;
                }
                if (choice == 0) {
                    processes.addLast(new NewProcess(id));
                    System.out.println("Новый процесс добавлен");
                    id++;
                } else {
                    NewProcess process = processes.removeFirst();
                    process.run();
                    System.out.println("Запускаем первый в очереди процесс");
                    i--;
                }
            }
            String choiceToContinue = "";
            boolean continueChoice = true;
            while(continueChoice){
                System.out.println("""
                        Выберете операцию: 1 - показать процессы, 2 - получить информацию из файла(жёсткого диска), 3 - продолжить,
                        4 - выход, 5 - получить информацию о памяти файла(доступной, занятой), 6 - очистить файл с информацией(жёсткий жиск)""");
                choiceToContinue = reader.readLine();
                switch (choiceToContinue){
                    case "1":
                        ArrayDeque<NewProcess> temp = new ArrayDeque<>();
                        int processesCount = processes.size();
                        for(int i = 0; i < processesCount; i++){
                            NewProcess process = processes.removeFirst();
                            System.out.println(process);
                            temp.addLast(process);
                        }
                        for(int i = 0; i < processesCount; i++){
                            processes.addFirst(temp.removeLast());
                        }
                        break;
                    case "2":
                        try(FileReader fileReader = new FileReader(dataFilePath)) {
                            int c;
                            while((c=fileReader.read())!=-1){
                                System.out.print((char)c);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "3":
                        continueChoice = false;
                        break;
                    case "4":
                        continueChoice = false;
                        exitProgram = true;
                        break;
                    case "5":
                        long sizeBytes = Files.size(Paths.get(dataFilePath));
                        double sizeKbytes = sizeBytes / 1024.0;
                        System.out.printf("Максимум файл может занимать %d байт%n", fileMaxSizeBytes);
                        System.out.printf("Файл занимает %d байт (%f кбайт)%n", sizeBytes, sizeKbytes);
                        System.out.printf("Осталось места для записи: %f Кбайт или же %d байт", (fileMaxSizeBytes - sizeBytes)/1024.0, fileMaxSizeBytes - sizeBytes);
                        break;
                    case "6":
                        try {
                            FileWriter writer = new FileWriter(Program.dataFilePath);
                            writer.close();
                            System.out.println("Файл очищен");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        System.out.println("Неправильный выбор");
                        break;
                }
            }
        }
        reader.close();
    }

}
