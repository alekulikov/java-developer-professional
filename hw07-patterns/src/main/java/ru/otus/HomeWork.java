package ru.otus;

import java.time.LocalTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.homework.SwapFieldsProcessor;
import ru.otus.processor.homework.ThrowsExceptionProcessor;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        /*
          по аналогии с Demo.class
          из элеменов "to do" создать new ComplexProcessor и обработать сообщение
        */
        var processors =
                List.of(new SwapFieldsProcessor(), new LoggerProcessor(new ThrowsExceptionProcessor(LocalTime::now)));
        var complexProcessor = new ComplexProcessor(processors, exception -> {});
        var historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);

        var objectForMessage = new ObjectForMessage();
        objectForMessage.setData(List.of("data1", "data2"));
        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(objectForMessage)
                .build();

        var result = complexProcessor.handle(message);
        logger.info("result:{}", result);
        logger.info("historyListener:{}", historyListener.findMessageById(1).orElse(null));

        complexProcessor.removeListener(historyListener);
    }
}
