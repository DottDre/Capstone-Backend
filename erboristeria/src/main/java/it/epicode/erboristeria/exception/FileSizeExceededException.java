package it.epicode.erboristeria.exception;

public class FileSizeExceededException extends RuntimeException{
    public FileSizeExceededException(String message) {
        super(message);
}
}
