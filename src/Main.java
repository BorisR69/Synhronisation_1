import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    // Коллекция комбинаций повторов и их частоты
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(1000);
        int keyMax = 0;    // Комбинация повторов встречающаяся максимальное количество раз
        int valueMax = 0;  // Значение максимального количества повторов комбинации

        Callable<Integer> call = () -> calcR(generateRoute("RLRFR", 100));
        // Заполнение коллекции комбинаций повторов и их частоты
        for (int i = 1; i < 1000; i++) {
            Future<Integer> future = threadPool.submit(call);
            int countR = future.get();
            synchronized (sizeToFreq) {
                if (sizeToFreq.isEmpty()) {
                    sizeToFreq.put(countR, 1);
                } else {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else sizeToFreq.putIfAbsent(countR, 1);
                }
            }
        }
        // Поиск и вывод в консоль значения максимального количества повторений
        for (Map.Entry<Integer, Integer> key1 : sizeToFreq.entrySet()) {
            if (key1.getValue() > valueMax) {
                valueMax = key1.getValue();
                keyMax = key1.getKey();
            }
        }
        System.out.println("Самое частое количество повторений " + keyMax + " встретилось (" + valueMax + " раз)");
        // Вывод в консоль перечня комбинаций повторов и их частоты
        for (Map.Entry<Integer, Integer> key1 : sizeToFreq.entrySet()) {
            System.out.println("- " + key1.getKey() + " (" + key1.getValue() + " раз)");
        }
        threadPool.shutdown(); // Закрытие потоков
    }

    // Метод генерации строк
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
//        System.out.println(route);
        return route.toString();
    }

    // Метод вычисления частоты буквы R
    public static Integer calcR(String str) {
        int countR = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'R') {
                countR++;
            }
        }
        return countR;
    }
}