package mephi.exercise;

import java.io.IOException;

public interface PropertyLoader {

    String getProperty(String param) throws IOException;
}
