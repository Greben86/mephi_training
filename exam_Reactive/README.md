# Курсовая работа. Многопоточное и асинхронное программирование на Java.
## RxJava, реализация аналогичной RxJava-библиотеки

### Что нужно делать

Вам необходимо реализовать собственную версию библиотеки RxJava, используя основные концепции реактивного программирования. Проект должен включать базовые компоненты реактивного потока, поддерживать асинхронное выполнение, обработку ошибок и предоставлять операторы преобразования данных.

Цель работы — создать систему реактивных потоков с возможностью управления потоками выполнения (Schedulers) и обработки событий с использованием паттерна «Наблюдатель» (Observer pattern).

#### Блоки заданий
1. Реализация базовых компонентов

   Что нужно сделать:

   Реализовать интерфейс Observer с методами:
onNext(T item) — получает элементы потока.
onError(Throwable t) — обрабатывает ошибки.
onComplete() — вызывается при завершении потока.
Реализовать класс Observable с поддержкой подписки (subscribe).
Реализовать статический метод create(), позволяющий создавать объекты Observable.

2. Операторы преобразования данных

   Что нужно сделать:

   Реализовать оператор map(Function mapper), который преобразует поток данных.
   Реализовать оператор filter(Predicate predicate), который отфильтровывает ненужные элементы.

3. Управление потоками выполнения

   Что нужно сделать:

   Реализовать интерфейс Scheduler с методом execute(Runnable task).
   Создать три варианта Scheduler:
   * IOThreadScheduler (аналог Schedulers.io(), использующий CachedThreadPool).
   * ComputationScheduler (аналог Schedulers.computation(), использующий FixedThreadPool).
   * SingleThreadScheduler (аналог Schedulers.single(), использующий один поток).
   
   Реализовать методы subscribeOn(Scheduler scheduler), чтобы подписка выполнялась в заданном потоке, и observeOn(Scheduler scheduler), чтобы обработка элементов происходила в нужном потоке.

4. Дополнительные операторы и управление подписками

   Что нужно сделать:

   Реализовать оператор flatMap(Function<t, observable<r="">> mapper)</t,>, который преобразует элементы в новый Observable.
   Реализовать интерфейс Disposable, позволяющий отменять подписку.
   Добавить обработку ошибок с возможностью передачи их в метод onError().

5. Тестирование

   Что нужно сделать:

   Написать юнит-тесты для всех ключевых компонентов системы.
   Проверить корректность обработки ошибок.
   Проверить работу Schedulers в многопоточной среде.
   Проверить работу операторов map, filter, flatMap.

## Архитектура

Библиотека следует паттерну Наблюдатель и реализует следующие ключевые компоненты:

### Основные интерфейсы и классы

1. **Observer<T>**
   - Интерфейс для получения уведомлений от Observable
   - Методы:
      - `onNext(T item)`: Получает элементы от Observable
      - `onError(Throwable t)`: Обрабатывает ошибки
      - `onComplete()`: Обрабатывает завершение

2. **Observable<T>**
   - Основной класс для создания и манипуляции потоками данных
   - Поддерживает создание, преобразование и подписку
   - Реализует основной паттерн реактивного программирования

3. **Disposable**
   - Функциональный интерфейс для управления подписками
   - Методы:
      - `dispose()`: Отменяет подписку

## Основные компоненты

### Создание Observable
```java
Observable<Integer> observable = Observable.create(observer -> {
    observer.onNext(1)
            .onNext(2)
            .onComplete();
});
```

### Подписка
```java
Disposable subscription = observable.subscribe(
    item -> System.out.println("Получено: " + item),
    error -> System.out.println("Ошибка: " + error.getMessage()),
    () -> System.out.println("Завершено!")
);
```

## Планировщики (Schedulers)

Библиотека предоставляет три типа планировщиков для управления выполнением потоков:

1. **IOThreadScheduler**
   - Использует `CachedThreadPool`
   - Идеален для I/O операций
   - Создает новые потоки по мере необходимости
   - Переиспользует неактивные потоки
   - Лучше всего подходит для: сетевых вызовов, операций с файлами

2. **ComputationScheduler**
   - Использует `FixedThreadPool`
   - Размер можно задать, например исходя из количества процессоров
   - Лучше всего подходит для: обработки данных, вычислений

3. **SingleThreadScheduler**
   - Использует исполнитель с одним потоком
   - Обеспечивает последовательное выполнение
   - Лучше всего подходит для: обновлений UI, упорядоченных операций

### Использование планировщиков
```java
// Подписка в IO потоке
observable.subscribeOn(new IOThreadScheduler())
          .subscribe(...);

// Наблюдение в вычислительном потоке
observable.observeOn(new ComputationScheduler())
          .subscribe(...);
```

## Операторы

### Map
Преобразует элементы, испускаемые Observable
```java
observable.map(x -> x * 2)
          .subscribe(...);
```

### Filter
Фильтрует элементы на основе предиката
```java
observable.filter(x -> x % 2 == 0)
          .subscribe(...);
```

### FlatMap
Преобразует элементы в Observable и объединяет их
```java
observable.flatMap(x -> Observable.create(observer -> {
    observer.onNext(x * 10)
            .onNext(x * 20)
            .onComplete();
}))
.subscribe(...);
```

## Обработка ошибок

Библиотека предоставляет комплексную обработку ошибок:

1. Распространение ошибок через цепочки операторов
2. Обработка ошибок в Observer
3. Очистка ресурсов при ошибках
4. Обработка прерывания потоков

Пример:
```java
observable.subscribe(
    item -> System.out.println("Получено: " + item),
    error -> System.out.println("Ошибка: " + error.getMessage()),
    () -> System.out.println("Завершено!")
);
```

## Тестирование

Проект включает комплексные модульные тесты, охватывающие:

1. Базовую функциональность Observable
2. Реализации операторов
3. Поведение планировщиков
4. Обработку ошибок
5. Управление подписками

## Примеры использования

### Базовое использование
```java
Observable.create(observer -> {
    observer.onNext(1)
            .onNext(2)
            .onComplete();
})
.subscribe(
    item -> System.out.println("Получено: " + item),
    error -> System.out.println("Ошибка: " + error.getMessage()),
    () -> System.out.println("Завершено!")
);
```

### Сложная цепочка
```java
Observable.create(observer -> {
    observer.onNext(1)
            .onNext(2)
            .onNext(3)
            .onComplete();
})
.filter(x -> x % 2 == 0)
.map(x -> x * 2)
.flatMap(x -> Observable.create(observer -> {
    observer.onNext(x * 10)
            .onNext(x * 20)
            .onComplete();
}))
.subscribeOn(new IOThreadScheduler())
.observeOn(new ComputationScheduler())
.subscribe(
    item -> System.out.println("Результат: " + item),
    error -> System.out.println("Ошибка: " + error.getMessage()),
    () -> System.out.println("Завершено!")
);
```

### Обработка ошибок
```java
Observable.create(observer -> {
    try {
        observer.onNext(1);
        throw new RuntimeException("Тестовая ошибка");
    } catch (Exception e) {
        observer.onError(e);
    }
})
.subscribe(
    item -> System.out.println("Получено: " + item),
    error -> System.out.println("Обработана ошибка: " + error.getMessage()),
    () -> System.out.println("Это не будет вызвано")
);
```

### Управление ресурсами
```java
Disposable subscription = Observable.create(observer -> {
    int i = 0;
    while (true) {
       observer.onNext(i++);
       TimeUnit.MILLISECONDS.sleep(100);
    }
})
.subscribe(
    item -> System.out.println("Получено: " + item),
    error -> System.out.println("Ошибка: " + error.getMessage()),
    () -> System.out.println("Это не будет вызвано")
);

// Отмена подписки через 500мс
TimeUnit.MILLISECONDS.sleep(500);
subscription.dispose();
```
