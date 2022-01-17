package persistence;

import org.json.JSONObject;

// This interface requires all implementing classes have a method to return this as JSON object
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
