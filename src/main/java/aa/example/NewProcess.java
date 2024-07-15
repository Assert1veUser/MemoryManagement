package aa.example;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class NewProcess implements Runnable{
    private final Integer id;

    public NewProcess(Integer id) {
        this.id = id;
    }

    @Override
    public void run() {
        int countOperations = new Random().nextInt(3) + 2;
        String processInf = toString();
        System.out.println(processInf);
        for(int i = 0; i < countOperations; i++){
            String randomStr =  generateRandomString();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try(FileWriter writer = new FileWriter(Program.dataFilePath, true)) {
                if(Files.size(Paths.get(Program.dataFilePath)) + randomStr.length()* 2L > Program.fileMaxSizeBytes){
                    System.out.println("Невозможно записать данные в файл: будет превышено количество занимаемой памяти жёсткого диска");
                }
                else
                {
                    System.out.println("Сгенерированная информация : " + randomStr);
                    writer.write(randomStr);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private String generateRandomString(){
        byte[] array = new byte[10];
        new Random().nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "MyProcess{" + "name = Process:" + id + '}';
    }

}
