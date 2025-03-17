package org.spring.attraction;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Log4j2
@Component
@RequiredArgsConstructor
public class SchemaRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] isExistsTable={"viewAttraction","viewReservation"};
        String[] viewQueries={
                "create view viewAttraction as" +
                        " select a.attraction_id 'id', a.name 'name', a.avgrate 'avgrate', a.price 'price', a.opentime 'open_time'," +
                        " a.closetime 'close_time', group_concat(c.type) 'type', concat(d.country, ' : ', d.city) 'area'" +
                        " from attraction a, attractiontypelist b, attractiontype c, area d" +
                        " where a.attraction_id = b.attraction_id" +
                        " and b.attraction_type_id = c.attraction_type_id" +
                        " and a.area_id = d.area_id" +
                        "group by a.attraction_id",
                "create view viewReservation as" +
                        " select a.reservation_id 'id', b.attraction_id 'attraction_id', b.name 'attraction_name', b.price 'attraction_price'," +
                        " a.peplenum 'reservation_peplenum', d.user_login_id 'user_login_id', a.reservedate 'Reservation_reservedate'," +
                        " e.type 'payment_type_type', b.price * a.peplenum 'total_price'" +
                        " from reservation a, attraction b, payment c, user d, paymenttype e" +
                        " where a.attraction_id = b.attraction_id" +
                        " and a.reservation_id = c.reservation_id" +
                        " and c.payment_type_id = e.payment_type_id" +
                        " and a.user_id = d.user_id"
        };


        for(int i=0;i<isExistsTable.length;i++){
            if(dropTableIfExists(isExistsTable[i])){
                createViewIfNotExists(viewQueries[i]);
            }
            System.out.println("View "+isExistsTable[i]+" created successfully.");
        }
        if(!insertInitData()){
            log.info("이미 데이터가 존재합니다.");
        }

    }

    public boolean dropTableIfExists(String tableName) {
        String checkTableQuery = "SELECT 1 FROM information_schema.tables WHERE table_schema = SCHEMA() AND table_name = '" + tableName + "'";
        Integer result = jdbcTemplate.queryForObject(checkTableQuery, Integer.class);
        if(result!=null || result>0){
            String dropTableQuery = "DROP TABLE IF EXISTS " + tableName;
            jdbcTemplate.execute(dropTableQuery);
            return true;
        }else{
            return false;
        }
    }

    public void createViewIfNotExists(String viewQuery){
        jdbcTemplate.execute(viewQuery);
    }

    //이미 데이터 존재하면 당연히 false 출력하고 나머지 데이터 입력 안됨
    public boolean insertInitData() {
        ClassPathResource resource = new ClassPathResource("data/insert_data.sql");
        try {
            String sql = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            String[] statements = sql.split(";");
            for(String statement : statements) {
                statement = statement.trim();
                if(!statement.isEmpty()){
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
