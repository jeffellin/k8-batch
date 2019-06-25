package com.ellin.batch.batchk8.batch;

import com.ellin.batch.batchk8.model.Flight;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.IncorrectTokenCountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

public class PatsyJobConfiguration {


    @Bean
    public Step load(StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("load")
                .listener(downloadingStepExecutionListener())
                .<Flight,Flight>chunk(20)
                .reader(reader(null))
                .writer(writer(null))
                .faultTolerant().skipLimit(100)
                .skip(IncorrectTokenCountException.class)
                .skip(FlatFileParseException.class)
                .build();
    }




    @Bean
    @StepScope
    public FlatFileItemReader<Flight> reader(@Value("#{stepExecutionContext['localFile']}")String fileName) {
        FlatFileItemReader<Flight> reader = new FlatFileItemReaderBuilder<Flight>()
                .name("fooReader")
                .resource(new FileSystemResource(fileName))
                .delimited()
                .names(new String[] {"year","month","day","day_of_week","airline","flight_number","tail_number",
                        "origin_airport","destination_airport","scheduled_departure","departure_time",
                        "departure_delay","taxi_out","wheels_off","scheduled_time","elapsed_time",
                        "air_time","distance","wheels_on","taxi_in","scheduled_arrival","arrival_time",
                        "arrival_delay","diverted","cancelled","cancellation_reason","air_system_delay",
                        "security_delay","airline_delay","late_aircraft_delay","weather_delay"
                })
                .targetType(Flight.class)
                .build();

        return reader;
    }

    @Bean
    public DownloadingStepExecutionListener downloadingStepExecutionListener() {
        return new DownloadingStepExecutionListener();
    }


    @Bean
    public JdbcBatchItemWriter<Flight> writer(DataSource dataSource) {

        return new JdbcBatchItemWriterBuilder<Flight>()
                .dataSource(dataSource)
                .beanMapped()
                .sql("INSERT INTO delay_data VALUES (  " +
                        "    :year," +
                        "    :month," +
                        "    :day," +
                        "    :day_of_week," +
                        "    :airline," +
                        "    :flight_number," +
                        "    :tail_number," +
                        "    :origin_airport," +
                        "    :destination_airport," +
                        "    :scheduled_departure," +
                        "    :departure_time," +
                        "    :departure_delay," +
                        "    :taxi_out," +
                        "    :wheels_off," +
                        "    :scheduled_time," +
                        "    :elapsed_time," +
                        "    :air_time," +
                        "    :distance," +
                        "    :wheels_on," +
                        "    :taxi_in," +
                        "    :scheduled_arrival," +
                        "    :arrival_time," +
                        "    :arrival_delay," +
                        "    :diverted," +
                        "    :cancelled," +
                        "    :cancellation_reason," +
                        "    :air_system_delay," +
                        "    :security_delay," +
                        "    :airline_delay," +
                        "    :late_aircraft_delay," +
                        "    :weather_delay)")
                .build();
    }

}
