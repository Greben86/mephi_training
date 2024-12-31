package mephi.exercise.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mephi.exercise.entity.Context;
import mephi.exercise.repository.DataSourceDictionary;
import mephi.exercise.service.Printer;

@Getter
@RequiredArgsConstructor
public abstract class AbstractCommand implements Runnable {

    protected final DataSourceDictionary dictionary;
    protected final Context context;
    protected final Printer printer;
}
