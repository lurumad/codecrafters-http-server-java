package files;

import java.util.Optional;

public interface FileProvider {
    Optional<String> read(String path);

    void write(String path, byte[] body);
}
