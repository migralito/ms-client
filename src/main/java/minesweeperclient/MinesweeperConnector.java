package minesweeperclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import minesweeperclient.exceptions.GameAlreadyOverException;
import minesweeperclient.exceptions.GamePausedException;
import minesweeperclient.exceptions.IllegalMoveException;
import minesweeperclient.exceptions.UnexpectedStatusCodeException;
import minesweeperclient.model.Minesweeper;
import minesweeperclient.model.MinesweeperMove;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UncheckedIOException;

class MinesweeperConnector {
    private static final TypeReference<Minesweeper> MINESWEEPER_TYPEREF = new TypeReference<Minesweeper>() {};
    private static final TypeReference<MinesweeperMove> MINESWEEPER_MOVE_TYPEREF = new TypeReference<MinesweeperMove>() {};

    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUri;
    private final CloseableHttpClient client;

    public MinesweeperConnector(boolean https, String host) {
        this(https, host, HttpClients.createDefault());
    }

    public MinesweeperConnector(boolean https, String host, CloseableHttpClient client) {
        this.baseUri = (https ? "https://" : "http://") + host;
        this.client = client;
    }

    public Minesweeper createGame() {
        return execute(RequestBuilder.post(createGameUri()).build(), MINESWEEPER_TYPEREF);
    }

    public MinesweeperMove shovel(String id, int x, int y) {
        return execute(RequestBuilder.post(shovelUri(id, x, y)).build(), MINESWEEPER_MOVE_TYPEREF);
    }

    public MinesweeperMove mark(String id, int x, int y, boolean question) {
        return execute(RequestBuilder.post(markUri(id, x, y, question)).build(), MINESWEEPER_MOVE_TYPEREF);
    }

    public MinesweeperMove unmark(String id, int x, int y) {
        return execute(RequestBuilder.delete(markUri(id, x, y, false)).build(), MINESWEEPER_MOVE_TYPEREF);
    }

    // /////////////////////////////////////////////////////
    // HELPERS
    // /////////////////////////////////////////////////////

    private <T> T execute(HttpUriRequest request, TypeReference<T> valueTypeRef) {
        try (CloseableHttpResponse response = client.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            String entity = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                return mapper.readValue(entity, valueTypeRef);
            } else if (statusCode == 400) {
                throw new IllegalMoveException(entity);
            } else if (statusCode == 409) {
                JsonNode jsonNode = mapper.readTree(entity);
                String reason = jsonNode.get("reason").textValue();
                switch (reason) {
                    case "game over": throw new GameAlreadyOverException();
                    case "game paused": throw new GamePausedException();
                    case "game not paused": throw new GamePausedException();
                    default: throw new RuntimeException("Unexpected 409 reason: " + reason);
                }
            } else {
                throw new UnexpectedStatusCodeException(statusCode, entity);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String createGameUri() {
        return baseUri + "/minesweepers";
    }

    private String shovelUri(String id, int x, int y) {
        return baseUri + "/minesweepers/" + id + "/" + x + "," + y + "/shovel";
    }

    private String markUri(String id, int x, int y, boolean question) {
        return baseUri + "/minesweepers/" + id + "/" + x + "," + y + "/mark" + (question ? "?question" : "");
    }
}
