package com.ellin.batch.batchk8.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Flight {

    private String year;
    private String month;
    private String day;
    private String day_of_week;
    private String airline;
    private String flight_number;
    private String tail_number;
    private String origin_airport;
    private String destination_airport;
    private String scheduled_departure;
    private String departure_time;
    private String departure_delay;
    private String taxi_out;
    private String wheels_off;
    private String scheduled_time;
    private String elapsed_time;
    private String air_time;
    private String distance;
    private String wheels_on;
    private String taxi_in;
    private String scheduled_arrival;
    private String arrival_time;
    private String arrival_delay;
    private String diverted;
    private String cancelled;
    private String cancellation_reason;
    private String air_system_delay;
    private String security_delay;
    private String airline_delay;
    private String late_aircraft_delay;
    private String weather_delay;
}


