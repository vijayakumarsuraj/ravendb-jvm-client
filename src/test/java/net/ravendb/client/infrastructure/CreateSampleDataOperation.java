package net.ravendb.client.infrastructure;

import net.ravendb.client.documents.conventions.DocumentConventions;
import net.ravendb.client.documents.operations.IVoidMaintenanceOperation;
import net.ravendb.client.documents.smuggler.DatabaseItemType;
import net.ravendb.client.http.IRaftCommand;
import net.ravendb.client.http.ServerNode;
import net.ravendb.client.http.VoidRavenCommand;
import net.ravendb.client.primitives.Reference;
import net.ravendb.client.primitives.SharpEnum;
import net.ravendb.client.util.RaftIdGenerator;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.EnumSet;
import java.util.stream.Collectors;

public class CreateSampleDataOperation implements IVoidMaintenanceOperation {

    private final EnumSet<DatabaseItemType> _operateOnTypes;

    public CreateSampleDataOperation() {
        this(EnumSet.of(DatabaseItemType.DOCUMENTS));
    }

    public CreateSampleDataOperation(EnumSet<DatabaseItemType> operateOnTypes) {
        _operateOnTypes = operateOnTypes;
    }

    @Override
    public VoidRavenCommand getCommand(DocumentConventions conventions) {
        return new CreateSampleDataCommand(_operateOnTypes);
    }

    private static class CreateSampleDataCommand extends VoidRavenCommand implements IRaftCommand {

        private final EnumSet<DatabaseItemType> _operateOnTypes;

        public CreateSampleDataCommand(EnumSet<DatabaseItemType> operateOnTypes) {
            _operateOnTypes = operateOnTypes;
        }

        @Override
        public boolean isReadRequest() {
            return false;
        }

        @Override
        public HttpRequestBase createRequest(ServerNode node, Reference<String> url) {
            url.value = node.getUrl() + "/databases/" + node.getDatabase() + "/studio/sample-data";

            url.value += "?" + _operateOnTypes
                    .stream()
                    .map(x -> "operateOnTypes=" + SharpEnum.value(x))
                    .collect(Collectors.joining("&"));

            return new HttpPost();
        }

        @Override
        public String getRaftUniqueRequestId() {
            return RaftIdGenerator.newId();
        }
    }
}
