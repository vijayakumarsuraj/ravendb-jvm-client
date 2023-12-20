package net.ravendb.client.documents.commands;

import net.ravendb.client.documents.identity.HiLoResult;
import net.ravendb.client.http.RavenCommand;
import net.ravendb.client.http.ServerNode;
import net.ravendb.client.primitives.NetISO8601Utils;
import net.ravendb.client.primitives.Reference;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import java.io.IOException;
import java.util.Date;

public class NextHiLoCommand extends RavenCommand<HiLoResult> {

    private final String _tag;
    private final long _lastBatchSize;
    private final Date _lastRangeAt;
    private final char _identityPartsSeparator;
    private final long _lastRangeMax;

    public NextHiLoCommand(String tag, long lastBatchSize, Date lastRangeAt, char identityPartsSeparator, long lastRangeMax) {
        super(HiLoResult.class);

        if (tag == null) {
            throw new IllegalArgumentException("tag cannot be null");
        }

        _tag = tag;
        _lastBatchSize = lastBatchSize;
        _lastRangeAt = lastRangeAt;
        _identityPartsSeparator = identityPartsSeparator;
        _lastRangeMax = lastRangeMax;
    }

    @Override
    public HttpRequestBase createRequest(ServerNode node, Reference<String> url) {
        StringBuilder pathBuilder = new StringBuilder();
        pathBuilder
                .append(node.getUrl())
                .append("/databases/")
                .append(node.getDatabase())
                .append("/hilo/next?tag=")
                .append(urlEncode(_tag))
                .append("&lastBatchSize=")
                .append(_lastBatchSize);

        if (_lastRangeAt != null) {
            pathBuilder.append("&lastRangeAt=")
                    .append(NetISO8601Utils.format(_lastRangeAt, true));
        }

        pathBuilder
                .append("&identityPartsSeparator=")
                .append(urlEncode(String.valueOf(_identityPartsSeparator)))
                .append("&lastMax=")
                .append(_lastRangeMax);

        url.value = pathBuilder.toString();

        return new HttpGet();
    }

    @Override
    public void setResponse(String response, boolean fromCache) throws IOException {
        result = mapper.readValue(response, resultClass);
    }

    @Override
    public boolean isReadRequest() {
        return true;
    }
}
