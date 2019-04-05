package net.ravendb.client.test;

import com.google.common.base.Stopwatch;
import net.ravendb.client.RemoteTestBase;
import net.ravendb.client.documents.IDocumentStore;
import net.ravendb.client.documents.operations.backups.*;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class BackupsTest extends RemoteTestBase {

    @Test
    public void canBackupDatabase() throws Exception {
        try (IDocumentStore store = getDocumentStore()) {
            Path backup = Files.createTempDirectory("backup");

            try {
                PeriodicBackupConfiguration backupConfiguration = new PeriodicBackupConfiguration();
                backupConfiguration.setName("myBackup");
                backupConfiguration.setBackupType(BackupType.SNAPSHOT);
                backupConfiguration.setFullBackupFrequency("20 * * * *");

                LocalSettings localSettings = new LocalSettings();
                localSettings.setFolderPath(backup.toAbsolutePath().toString());

                backupConfiguration.setLocalSettings(localSettings);
                UpdatePeriodicBackupOperation operation = new UpdatePeriodicBackupOperation(backupConfiguration);
                UpdatePeriodicBackupOperationResult backupOperationResult = store.maintenance().send(operation);

                StartBackupOperation startBackupOperation = new StartBackupOperation(true, backupOperationResult.getTaskId());
                StartBackupOperationResult send = store.maintenance().send(startBackupOperation);
                int backupOperation = send.getOperationId();

                waitForBackup(backup);
                waitForBackupStatus(store, backupOperationResult.getTaskId());
            } finally {
                backup.toAbsolutePath().toFile().deleteOnExit();

                // make sure backup was finished
                Thread.sleep(500);
                backup.toAbsolutePath().toFile().delete();
            }
        }

    }

    private void waitForBackup(Path backup) throws Exception {
        Stopwatch sw = Stopwatch.createStarted();
        while (sw.elapsed(TimeUnit.MILLISECONDS) < 10_000) {

            if (Files.list(backup).count() > 0) {
                return;
            }

            Thread.sleep(200);
        }

        throw new IllegalStateException("Unable to find backup files in: " + backup.toAbsolutePath());
    }

    private void waitForBackupStatus(IDocumentStore store, long taskId) throws Exception {
        Stopwatch sw = Stopwatch.createStarted();

        while (sw.elapsed(TimeUnit.MILLISECONDS) < 10_000) {
            GetPeriodicBackupStatusOperationResult backupStatus = store.maintenance()
                    .send(new GetPeriodicBackupStatusOperation(taskId));

            if (backupStatus != null
                    && backupStatus.getStatus() != null
                    && backupStatus.getStatus().getLastFullBackup() != null) {
                return;
            }

            Thread.sleep(200);
        }

        throw new IllegalStateException("Unable to get expected backup status");
    }
}
