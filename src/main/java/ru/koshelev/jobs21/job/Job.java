package ru.koshelev.jobs21.job;

import ru.koshelev.jobs21.Result;

@FunctionalInterface
public interface Job<T> {
    Result<T> execute();
}
