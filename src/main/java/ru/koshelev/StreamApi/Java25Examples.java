package ru.koshelev.StreamApi;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Java25Examples
 *
 * <p>Примеры использования новых возможностей Java 25+
 */
public class Java25Examples {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        recordPatternExample();
        virtualThreadsExample();
        virtualThreadsWithIOExample();
        test();

    }

    /**
     * Пример использования паттернов для рекордов (Record Patterns).
     * Упрощает извлечение данных из объектов.
     */
    public static void recordPatternExample() {
        record Person(String name, int age) {}

        Object obj = new Person("Alice", 25);

        String info = switch (obj) {
            case Person(String name, int age) -> "Name: " + name + ", age: " + age;
            default -> "Unknown";
        };

        System.out.println("Record pattern: " + info);
    }

    /**
     * Пример виртуальных потоков (Project Loom).
     * Позволяет писать многопоточку проще и эффективнее.
     */
    public static void virtualThreadsExample() {
        try {
            Thread.startVirtualThread(() -> System.out.println("Hello from virtual thread"))
                    .join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Пример с виртуальными потоками для IO-bound задач
     */
    public static void virtualThreadsWithIOExample() throws InterruptedException {
        List<Runnable> ioTasks = List.of(
                () -> simulateIO("Task 1", 1000),
                () -> simulateIO("Task 2", 1500),
                () -> simulateIO("Task 3", 500)
        );

        List<Thread> threads = ioTasks.stream()
                .map(Thread::startVirtualThread)
                .toList();

        for (Thread t : threads) {
            t.join();
        }
    }

    public static void test() throws InterruptedException, ExecutionException {
        List<String> names = List.of("Alice", "Bob", "Charlie", "Diana");

        // Используем Executor для виртуальных потоков
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = names.stream()
                    .map(name -> executor.submit(() -> {
                        Thread.sleep(500); // имитация работы
                        return "Processed " + name;
                    }))
                    .toList();

            for (Future<String> f : futures) {
                System.out.println(f.get());
            }
        }
    }

    private static void simulateIO(String taskName, int millis) {
        System.out.println(taskName + " started on " + Thread.currentThread());
        try {
            Thread.sleep(millis); // имитация задержки ввода-вывода
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(taskName + " finished on " + Thread.currentThread());
    }
}
