package org.spring.attraction;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Component
@RequiredArgsConstructor
public class SchemaRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Value("${app.sql.filepath}")
    private String SqlFilePath;
    @Value("${app.sql.local}")
    private String Local;

    @Override
    public void run(ApplicationArguments args){
        String[] isExistsTable={"viewattraction","viewreservation"};
        String[] viewQueries={
                "create view viewattraction as" +
                        " select a.attraction_id 'id', a.name 'name', a.avgrate 'avgrate', a.price 'price', a.opentime 'open_time'," +
                        " a.closetime 'close_time', group_concat(c.type) 'type', concat(d.country, ' : ', d.city) 'area'" +
                        " from attraction a, attractiontypelist b, attractiontype c, area d" +
                        " where a.attraction_id = b.attraction_id" +
                        " and b.attraction_type_id = c.attraction_type_id" +
                        " and a.area_id = d.area_id" +
                        " group by a.attraction_id",
                "create view viewreservation as" +
                        " select a.reservation_id 'id', b.attraction_id 'attraction_id', b.name 'attraction_name', b.price 'attraction_price'," +
                        " a.peplenum 'reservation_peplenum', d.user_login_id 'user_login_id', a.reservedate 'Reservation_reservedate'," +
                        " e.type 'payment_type_type', b.price * a.peplenum 'total_price'" +
                        " from reservation a, attraction b, payment c, user d, paymenttype e" +
                        " where a.attraction_id = b.attraction_id" +
                        " and a.reservation_id = c.reservation_id" +
                        " and c.payment_type_id = e.payment_type_id" +
                        " and a.user_id = d.user_id"
        };

    try {
        for (int i = 0; i < isExistsTable.length; i++) {
            if (dropTableIfExists(isExistsTable[i])) {
                createViewIfNotExists(viewQueries[i]);
                log.info("View " + isExistsTable[i] + " created successfully.");
            } else {
                log.info("Table " + isExistsTable[i] + " Not exists.");
                createViewIfNotExists(viewQueries[i]);
            }
        }
    }catch (Exception e){
        e.printStackTrace();
        log.info("뷰 생성 도중 오류 발생");
    }
        if(!insertInitData()){
            log.info("이미 데이터가 존재합니다.");
        }

    }

    public boolean dropTableIfExists(String tableName) {
        try {
            String checkTableQuery = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = '" + tableName + "'";
            Integer result = jdbcTemplate.queryForObject(checkTableQuery, Integer.class);
            if (result != null || result > 0) {
                String dropTableQuery = "DROP TABLE IF EXISTS " + tableName;
                log.info(dropTableQuery);
                jdbcTemplate.execute(dropTableQuery);
                return true;
            }
        } catch (EmptyResultDataAccessException e) {
            // 테이블이 존재하지 않으므로 아무 작업도 하지 않음
            e.printStackTrace();
            log.info("에러발생!!!");
            return false;
        }
        return false;
    }

    public void createViewIfNotExists(String viewQuery){
        jdbcTemplate.execute(viewQuery);
    }

    //이미 데이터 존재하면 당연히 false 출력하고 나머지 데이터 입력 안됨
    public boolean insertInitData() {

        String sql;
        Path externalFilePath = Paths.get(SqlFilePath);
        Resource resource;

        try {
            if (Local.compareTo("yes")!=0&&Files.exists(externalFilePath)) {
                log.info("외부 SQL 파일 사용: {}", SqlFilePath);
                resource = new FileSystemResource(SqlFilePath);
            } else {
                log.warn("외부 SQL 파일 없음. 기본 SQL 파일 사용: src/main/resources/data/insert_data.sql");
                resource = new ClassPathResource("data/insert_data.sql");
            }
            sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            String[] statements = sql.split(";");
            for(String statement : statements) {
                statement = statement.trim();
                if(!statement.isEmpty()){
                    log.info("Insert data : " + statement);
                    jdbcTemplate.execute(statement);
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            log.error("오류로 초기 데이터 삽입 실패");
            return false;
        }
    }
}
