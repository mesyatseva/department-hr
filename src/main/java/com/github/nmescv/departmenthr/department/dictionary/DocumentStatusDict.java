package com.github.nmescv.departmenthr.department.dictionary;

public enum DocumentStatusDict {

    DRAFT("Черновик"),
    OPEN("Открыт"),
    IN_PROCESS("В процессе"),
    CLOSED("Закрыт");

    private final String status;

    DocumentStatusDict(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
