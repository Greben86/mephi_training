package mephi.exercise.reactive;

/**
 * Интерфейс, позволяющий отменять подписку
 */
@FunctionalInterface
public interface Disposable {

    /**
     * Метод для отписки
     */
    void dispose();
}
