import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        String basePath = "C:\\Games\\savegames";

        GameProgress save1 = new GameProgress(100, 5, 3, 125.5);
        GameProgress save2 = new GameProgress(80, 7, 5, 250.0);
        GameProgress save3 = new GameProgress(60, 3, 2, 75.3);


        saveGame(basePath + "\\save1.dat", save1);
        saveGame(basePath + "\\save2.dat", save2);
        saveGame(basePath + "\\save3.dat", save3);

        // Список путей к файлам для архивации
        List<String> filesToZip = Arrays.asList(
                basePath + "\\save1.dat",
                basePath + "\\save2.dat",
                basePath + "\\save3.dat");

        // Создание архива и удаление исходных файлов
        String zipPath = basePath + "\\saves.zip";
        zipFiles(zipPath, filesToZip);

        System.out.println("Архив создан: " + zipPath);


    }


    //1. Метод сохранения игры
    private static void saveGame(String filePath, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Сохранение выполнено: " + filePath);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    //2. Метод для создания архива
    private static void zipFiles(String zipPath, List<String> filePaths) {
        try (FileOutputStream fos = new FileOutputStream(zipPath);
             ZipOutputStream zos = new ZipOutputStream(fos)) {

            for (String filePath : filePaths) {
                File file = new File(filePath);

                if (file.exists()) {
                    try (FileInputStream fis = new FileInputStream(file)) {
                        ZipEntry entry = new ZipEntry(file.getName());
                        zos.putNextEntry(entry);

                        byte[] buffer = new byte[1024];
                        int length;

                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }

                        zos.closeEntry();
                        System.out.println("Файл добавлен в архив: " + file.getName());

                    } catch (IOException e) {
                        System.out.println("Ошибка при добавлении файла в архив: " + e.getMessage());
                    }
                } else {
                    System.out.println("Файл не существует: " + filePath);
                }
                // Удаление исходного файла
                Files.delete(Path.of(filePath));
                System.out.println("Файл удален: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("Ошибка при создании архива: " + e.getMessage());
        }
    }
}