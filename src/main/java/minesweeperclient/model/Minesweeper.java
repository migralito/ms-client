package minesweeperclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import static java.util.stream.Collectors.toList;

public class Minesweeper {

    private String id;
    private Instant creationDateTime;
    private Instant updatedDateTime;
    private String status;
    private List<List<String>> field;

    @JsonCreator
    Minesweeper(@JsonProperty("id") String id,
                @JsonProperty("creation_date_time") Instant creationDateTime,
                @JsonProperty("updated_date_time") Instant updatedDateTime,
                @JsonProperty("status") String status,
                @JsonProperty("field") Vector<Vector<String>> field) {
        this.id = id;
        this.creationDateTime = creationDateTime;
        this.updatedDateTime = updatedDateTime;
        this.status = status;
        this.field = Collections.unmodifiableList(field.stream().map(Collections::unmodifiableList).collect(toList()));
    }

    public String getId() {
        return id;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public Instant getUpdatedDateTime() {
        return updatedDateTime;
    }

    public String getStatus() {
        return status;
    }

    public List<List<String>> getField() {
        return field;
    }
}
