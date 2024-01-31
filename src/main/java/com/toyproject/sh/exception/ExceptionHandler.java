package com.toyproject.sh.exception;

public class ExceptionHandler extends RuntimeException{

    public ExceptionHandler (String message){
        super(message);
    }

    public static class DuplicateEmailException extends ExceptionHandler {
        public DuplicateEmailException() {
            super("이메일이 이미 존재합니다.");
        }
    }

    public static class PostNotFoundException extends ExceptionHandler {
        public PostNotFoundException() {
            super("게시글이 없습니다.");
        }
    }

    public static class TagNotStartWithSharpException extends ExceptionHandler {
        public TagNotStartWithSharpException() {
            super("태그가 #으로 시작하지 않습니다.");
        }
    }

    public static class AllFriendException extends ExceptionHandler {
        public AllFriendException(String message){
            super(message);
        }
    }

    public static class AllTagException extends ExceptionHandler {
        public AllTagException(String message) {
            super(message);
        }
    }
}
