package org.dandelion.radiot.util;

public class ProgrammerError extends RuntimeException {
    public ProgrammerError(Exception e) {
        super(e);
    }

    public ProgrammerError(String message) {
        super(message);
    }
}
