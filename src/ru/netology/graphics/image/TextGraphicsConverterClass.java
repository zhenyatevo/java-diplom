package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TextGraphicsConverterClass implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = Double.MAX_VALUE;
    private TextColorSchema schema;
    private final TextColorSchema defaultSchema = new TextColorSchemaClass();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        // Проверка URL
        if (url == null || url.trim().isEmpty()) {
            throw new IOException("URL изображения не может быть пустым");
        }
        // Загрузка изображения
        BufferedImage img ;
        try {
            img = ImageIO.read(new URL(url));
            if (img == null) {
                throw new IOException("Не удалось загрузить изображение по URL: " + url);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Некорректный URL: " + url, e);
        }
        // Вот так просто мы скачаем картинку из интернета :)
        //BufferedImage img = ImageIO.read(new URL(url));
        // Если конвертер попросили проверять на максимально допустимое
        // соотношение сторон изображения, то вам здесь нужно сделать эту проверку,
        // и, если картинка не подходит, выбросить исключение BadImageSizeException.
        // Чтобы получить ширину картинки, вызовите img.getWidth(), высоту - img.getHeight()
        // Проверка соотношения сторон
        double aspectRatio = (double) img.getWidth() / img.getHeight();
        if (maxRatio > 0 && aspectRatio > maxRatio) {
            throw new BadImageSizeException(aspectRatio, maxRatio);
        }

        // Если конвертеру выставили максимально допустимые ширину и/или высоту,
        // вам нужно по ним и по текущим высоте и ширине вычислить новые высоту
        // и ширину.
        // Соблюдение пропорций означает, что вы должны уменьшать ширину и высоту
        // в одинаковое количество раз.
        // Пример 1: макс. допустимые 100x100, а картинка 500x200. Новый размер
        // будет 100x40 (в 5 раз меньше).
        // Пример 2: макс. допустимые 100x30, а картинка 150x15. Новый размер
        // будет 100x10 (в 1.5 раза меньше).
        // Подумайте, какими действиями можно вычислить новые размеры.
        // Не получается? Спросите вашего руководителя по курсовой, поможем.
        // Получение размеров
        // Расчет новых размеров с сохранением пропорций
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();

        if (maxWidth > 0 && newWidth > maxWidth) {
            double ratio = (double) newWidth / maxWidth;
            newWidth = maxWidth;
            newHeight = (int) Math.round(newHeight / ratio);
        }

        if (maxHeight > 0 && newHeight > maxHeight) {
            double ratio = (double) newHeight / maxHeight;
            newHeight = maxHeight;
            newWidth = (int) Math.round(newWidth / ratio);
        }
        // Теперь нам нужно попросить картинку изменить свои размеры на новые.
        // Последний параметр означает, что мы просим картинку плавно сузиться
        // на новые размеры. В результате мы получаем ссылку на новую картинку, которая
        // представляет собой суженную старую.
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // Теперь сделаем её чёрно-белой. Для этого поступим так:
        // Создадим новую пустую картинку нужных размеров, заранее указав последним
        // параметром чёрно-белую цветовую палитру:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        // Попросим у этой картинки инструмент для рисования на ней:
        Graphics2D graphics = bwImg.createGraphics();
        // А этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки:
        try {
            graphics.drawImage(scaledImage, 0, 0, null);
        } finally {
            graphics.dispose();
        }
        // Теперь в bwImg у нас лежит чёрно-белая картинка нужных нам размеров.
        // Вы можете отслеживать каждый из этапов, в любом удобном для
        // вас моменте сохранив промежуточную картинку в файл через:
        //ImageIO.write(imageObject, "png", new File("out.png"));
        // После вызова этой инструкции у вас в проекте появится файл картинки out.png

        // Теперь давайте пройдёмся по пикселям нашего изображения.
        // Если для рисования мы просили у картинки .createGraphics(),
        // то для прохода по пикселям нам нужен будет этот инструмент:
        WritableRaster bwRaster = bwImg.getRaster();

        // Он хорош тем, что у него мы можем спросить пиксель на нужных
        // нам координатах, указав номер столбца (w) и строки (h)
        // int color = bwRaster.getPixel(w, h, new int[3])[0];
        // Выглядит странно? Согласен. Сам возвращаемый методом пиксель — это
        // массив из трёх интов, обычно это интенсивность красного, зелёного и синего.
        // Но у нашей чёрно-белой картинки цветов нет, и нас интересует
        // только первое значение в массиве. Ещё мы параметром передаём интовый массив на три ячейки.
        // Дело в том, что этот метод не хочет создавать его сам и просит
        // вас сделать это, а сам метод лишь заполнит его и вернёт.
        // Потому что создавать массивы каждый раз слишком медленно. Вы можете создать
        // массив один раз, сохранить в переменную и передавать один
        // и тот же массив в метод, ускорив тем самым программу.

        // Вам осталось пробежаться двойным циклом по всем столбцам (ширина)
        // и строкам (высота) изображения, на каждой внутренней итерации
        // получить степень белого пикселя (int color выше) и по ней
        // получить соответствующий символ c. Логикой превращения цвета
        // в символ будет заниматься другой объект, который мы рассмотрим ниже
        StringBuilder result = new StringBuilder();
        TextColorSchema currentSchema = (schema != null) ? schema : defaultSchema;

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = currentSchema.convert(color);
                result.append(c).append(c);  //запоминаем символ c, например, в двумерном массиве или как-то ещё на ваше усмотрение
            }
            result.append("\n");
        }

        // Осталось собрать все символы в один большой текст.
        // Для того, чтобы изображение не было слишком узким, рекомендую
        // каждый пиксель превращать в два повторяющихся символа, полученных
        // от схемы.

        return result.toString(); // Возвращаем собранный текст.
    }

    @Override
    public void setMaxWidth(int width) {
        if (width <= 0) {
            throw new IllegalArgumentException("Максимальная ширина должна быть положительной");
        }
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        if (height <= 0) {
            throw new IllegalArgumentException("Максимальная высота должна быть положительной");
        }
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        if (maxRatio <= 0) {
            throw new IllegalArgumentException("Максимальное соотношение сторон должно быть положительным");
        }
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {

        this.schema = schema;
    }
}
