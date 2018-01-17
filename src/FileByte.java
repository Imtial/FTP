import java.io.Serializable;
import java.util.Vector;

public class FileByte implements Serializable {
    private Vector<Byte> fileBytes;
    private String name;

    FileByte(Vector<Byte> fileBytes, String name) {
        this.fileBytes = fileBytes;
        this.name = name;
    }

    public Vector<Byte> getFileBytes() {
        return fileBytes;
    }

    public String getName() {
        return name;
    }
}
