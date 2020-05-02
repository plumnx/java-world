package com.plumnix.boot.service;

import com.plumnix.boot.entity.BatchRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CREATE TABLE batch_record (
 * id varchar not null primary key,
 * job_instance_id int default null,
 * status int default null,
 * version int default null,
 * error_message varchar
 * );
 */
@Service
public class RecordService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void create(BatchRecord batchRecord) {
        jdbcTemplate.update("insert into batch_record (id, job_instance_id, status, version, error_message) values (?,?,?,?,?)",
                batchRecord.getId(), batchRecord.getJobInstanceId(), batchRecord.getStatus(), batchRecord.getVersion(), batchRecord.getErrorMessage());
    }

    @Transactional
    public BatchRecord selectByTaskId(String id) {
        return jdbcTemplate.queryForObject("select id, job_instance_id, status, version, error_message from batch_record where id = ?",
                (rs, rowNum) -> {
                    BatchRecord batchRecord = new BatchRecord();
                    batchRecord.setId(rs.getString("id"));
                    batchRecord.setJobInstanceId(rs.getInt("job_instance_id"));
                    batchRecord.setStatus(rs.getInt("status"));
                    batchRecord.setVersion(rs.getInt("version"));
                    batchRecord.setErrorMessage(rs.getString("error_message"));

                    return batchRecord;
                }, id);
    }

    @Transactional
    public int update(BatchRecord batchRecord) {
        return jdbcTemplate.update("update batch_record " +
                        "set job_instance_id = ?, " +
                        " status = ?," +
                        " version = ?, " +
                        " error_message = ? " +
                        " where id = ? and version = ?",
                batchRecord.getJobInstanceId(),
                batchRecord.getStatus(),
                batchRecord.getVersion() + 1,
                batchRecord.getErrorMessage(),
                batchRecord.getId(),
                batchRecord.getVersion()
        );
    }

    @Transactional
    public int updateForce(BatchRecord batchRecord) {
        return jdbcTemplate.update("update batch_record " +
                        "set job_instance_id = ?, " +
                        " status = ?," +
                        " error_message = ? " +
                        " where id = ?",
                batchRecord.getJobInstanceId(),
                batchRecord.getStatus(),
                batchRecord.getErrorMessage(),
                batchRecord.getId()
        );
    }
}
