package logic;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class CellAdapter extends TypeAdapter<Cell> {
    @Override
    public void write(JsonWriter writer, Cell cell) throws IOException {
        if (cell == null) {
            writer.nullValue();
            return;
        }
        String string = cell.toString();
        writer.value(string);

    }

    @Override
    public Cell read(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        }
        String string = reader.nextString();
     //   Cell cell =new Cell;
        return null;
    }
}
