package ru.koshelev.StreamApi;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * StreamExamples
 * Java 8+.
 */
public class StreamExamples {

    public static void main(String[] args) {
        basicFilterAndMap();
        findFirstExample();
        sumExample();
        distinctAndSort();
        objectFiltering();
        groupingExample();
        flatMapExample();
        matchExamples();
        realLifeExample();
    }

    /**
     * Пример 1.
     * Фильтрация чётных чисел и возведение их в квадрат.
     */
    public static void basicFilterAndMap() {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

        List<Integer> result = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .toList();

        System.out.println(result);
    }

    /**
     * Пример 2.
     * Поиск первого элемента, удовлетворяющего условию.
     *
     * findFirst() → порядок важен
     *
     * findAny() → может быть быстрее в parallel stream
     */
    public static void findFirstExample() {
        List<Integer> numbers = List.of(3, 7, 12, 5);

        Optional<Integer> firstGreaterThanTen = numbers.stream()
                .filter(n -> n > 10)
                .findFirst();

        System.out.println("First > 10: " + firstGreaterThanTen.orElse(null));
    }

    /**
     * Пример 3.
     * Подсчёт суммы элементов.
     * <p>
     * Используется primitive stream (mapToInt),
     * что предпочтительнее reduce.
     */
    public static void sumExample() {
        List<Integer> numbers = List.of(1, 2, 3, 4);

        int sum = numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Sum: " + sum);
    }

    /**
     * Пример 4.
     * Удаление дубликатов и сортировка.
     */
    public static void distinctAndSort() {
        List<Integer> numbers = List.of(5, 1, 2, 2, 3, 5);

        List<Integer> result = numbers.stream()
                .distinct()
                .sorted()
                .toList();

        System.out.println("Distinct & sorted: " + result);
    }

    /**
     * Пример 5.
     * Работа со стримами объектов.
     * Фильтрация активных пользователей старше 18 лет.
     */
    public static void objectFiltering() {
        List<User> users = List.of(
                new User("Alex", 20, true),
                new User("Bob", 17, true),
                new User("John", 25, false)
        );

        List<String> names = users.stream()
                .filter(User::isActive)
                .filter(u -> u.getAge() > 18)
                .map(User::getName)
                .toList();

        System.out.println("Active adults: " + names);
    }

    /**
     * Пример 6.
     * Группировка объектов по полю.
     */
    public static void groupingExample() {
        List<User> users = List.of(
                new User("Alex", 20, true),
                new User("Bob", 20, false),
                new User("John", 25, true)
        );

        Map<Integer, List<User>> usersByAge = users.stream()
                .collect(Collectors.groupingBy(User::getAge));

        System.out.println("Users by age: " + usersByAge);
    }

    /**
     * Пример 7.
     * flatMap — преобразование вложенных коллекций в плоскую структуру.
     */
    public static void flatMapExample() {
        List<List<String>> lists = List.of(
                List.of("a", "b"),
                List.of("c", "d")
        );

        List<String> result = lists.stream()
                .flatMap(Collection::stream)
                .toList();

        System.out.println("Flat list: " + result);
    }

    /**
     * Пример 8.
     * Проверка условий с помощью match-операций.
     */
    public static void matchExamples() {
        List<User> users = List.of(
                new User("Alex", 20, true),
                new User("Bob", 17, true)
        );

        boolean hasMinor = users.stream()
                .anyMatch(u -> u.getAge() < 18);

        System.out.println("Has minor: " + hasMinor);
    }

    /**
     * Пример 9.
     *
     * <p>
     * Есть список заказов, каждый заказ содержит товары.
     * Нужно посчитать общую стоимость товаров
     * только для активных заказов.
     */
    public static void realLifeExample() {
        List<Order> orders = List.of(
                new Order(true, List.of(
                        new Item("Book", 10.0),
                        new Item("Pen", 2.0)
                )),
                new Order(false, List.of(
                        new Item("Laptop", 1000.0)
                ))
        );

        double total = orders.stream()
                .filter(Order::isActive)
                .flatMap(o -> o.getItems().stream())
                .mapToDouble(Item::getPrice)
                .sum();

        System.out.println("Total price: " + total);
    }

    /* ======================= */
    /* ====== MODELS ========= */
    /* ======================= */

    /**
     * Простейшая модель пользователя.
     */
    static class User {
        private final String name;
        private final int age;
        private final boolean active;

        public User(String name, int age, boolean active) {
            this.name = name;
            this.age = age;
            this.active = active;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    /**
     * Заказ, содержащий список товаров.
     */
    static class Order {
        private final boolean active;
        private final List<Item> items;

        public Order(boolean active, List<Item> items) {
            this.active = active;
            this.items = items;
        }

        public boolean isActive() {
            return active;
        }

        public List<Item> getItems() {
            return items;
        }
    }

    /**
     * Товар с ценой.
     */
    static class Item {
        private final String name;
        private final double price;

        public Item(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public double getPrice() {
            return price;
        }
    }
}