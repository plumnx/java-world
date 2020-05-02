package com.plumnix.boot.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BatchRecord {

    private String id;
    private Integer jobInstanceId;
    private Integer status;
    private Integer version;
    private String errorMessage;

    public static enum RecordStatus {
        UNDO(0), TODO(1), DOING(2), DONE(3), FAIL(4);

        @Getter
        private int status;

        RecordStatus(int status) {
            this.status = status;
        }
    }

}
