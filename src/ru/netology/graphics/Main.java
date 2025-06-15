package ru.netology.graphics;

import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextColorSchemaClass;
import ru.netology.graphics.image.TextGraphicsConverter;
import ru.netology.graphics.image.TextGraphicsConverterClass;
import ru.netology.graphics.server.GServer;

import java.io.File;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws Exception {
        TextGraphicsConverter converter = new TextGraphicsConverterClass(); // Создайте тут объект вашего класса конвертера

        GServer server = new GServer(converter); // Создаём объект сервера
        server.start(); // Запускаем

        // Или то же, но с выводом на экран:
        //String url = "https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png";
        //String imgTxt = converter.convert(url);
        //System.out.println(imgTxt);

        //тестируем, этап 3
        //TextColorSchemaClass schema = new TextColorSchemaClass();
        //System.out.println(schema.convert( 0));

        //тестируем, этап 6
        //TextColorSchemaClass schema = new TextColorSchemaClass();
        //TextGraphicsConverter converter = new TextGraphicsConverterClass();
        //converter.setTextColorSchema(schema);
        //System.out.println(converter.convert("https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png"));

        //ещё тест на проверку соотношения сторон
        //TextGraphicsConverter converter = new TextGraphicsConverterClass(); // Создайте тут объект вашего класса конвертера
        //converter.setMaxRatio(1);  // выставляет максимально допустимое соотрношение сторон картинки
        //String imgTxt = converter.convert("https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png");

        //тест на макс параметры
        //TextGraphicsConverter converter = new TextGraphicsConverterClass();
        //converter.setMaxHeight(5);
        //converter.setMaxWidth(5);
        //String imgTxt = converter.convert("https://raw.githubusercontent.com/netology-code/java-diplom/main/pics/simple-test.png");
        //System.out.println(imgTxt);



    }
}
