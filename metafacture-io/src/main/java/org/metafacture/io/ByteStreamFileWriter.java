package org.metafacture.io;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Supplier;

import org.metafacture.framework.helpers.DefaultObjectReceiver;

/**
 * Writes byte arrays to regular output files.
 * <p>
 * The module opens a new output file when it receives a <i>reset-stream</i>
 * event. The {@link #setFileNameSupplier(Supplier)} enables users to specify
 * a new file name on each invocation. the {@link #setAppendIfFileExists(boolean)}
 * can be used to control whether existing files should be overwritten or
 * appended to.
 */
public class ByteStreamFileWriter extends DefaultObjectReceiver<byte[]> {

    private Supplier<File> fileNameSupplier;
    private boolean appendIfFileExists;

    private OutputStream outputStream;

    /**
     * Supplier for file names.
     * <p>
     * A new output file is created when {@link #process(byte[])} is called
     * for the first time or whenever {@link #resetStream()} is called. The
     * name of the new file is fetched from the {@code fileNameSupplier}.
     * <p>
     * There is no default value. A file name supplier must be specified.
     * <p>
     * This property can be changed anytime during processing. It becomes
     * effective the next time a new output file is opened.
     *
     * @param fileNameSupplier a supplier that returns file names.
     */
    public void setFileNameSupplier(Supplier<File> fileNameSupplier) {

        this.fileNameSupplier = requireNonNull(fileNameSupplier);
    }

    /**
     * Controls whether to open files in append mode if they exist.
     * <p>
     * The default value is {@code false}.
     * <p>
     * This property can be changed anytime during processing. It becomes
     * effective the next time a new output file is opened.
     *
     * @param appendIfFileExists true if new data should be appended,
     *                           false to overwrite the existing file.
     */
    public void setAppendIfFileExists(boolean appendIfFileExists) {

        this.appendIfFileExists = appendIfFileExists;
    }

    /**
     * Writes {@code bytes} to file.
     *
     * @param bytes to write to file
     * @throws WriteFailed if an IO error occurred
     */
    @Override
    public void process(final byte[] bytes) {

        ensureOpenStream();
        try {
            outputStream.write(bytes);

        } catch (IOException e) {
            throw new WriteFailed("Error while writing bytes to output stream", e);
        }
    }

    /**
     * Opens a new output file.
     *
     * @throws CloseFailed if the current output file could not be closed.
     * @throws OpenFailed if the new output file could not be opened.
     */
    @Override
    public void resetStream() {

        closeStream();
        ensureOpenStream();
    }

    /**
     * Closes the current output file.
     *
     * @throws CloseFailed if the output file could not be closed.
     */
    @Override
    public void closeStream() {

        if (outputStream != null) {
            try {
                outputStream.close();

            } catch (IOException e) {
                throw new CloseFailed("Error while closing output stream", e);
            }
            outputStream = null;
        }
    }

    private void ensureOpenStream() {

        if (outputStream != null) {
            return;
        }
        try {
            outputStream = new FileOutputStream(fileNameSupplier.get(), appendIfFileExists);

        } catch (FileNotFoundException e) {
            throw new OpenFailed("Cannot open output stream. File not found.", e);
        }
    }

}
