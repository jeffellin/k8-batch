Create table if not exists delay_data (
     year VARCHAR(50),
     month VARCHAR(50),
     day VARCHAR(50),
     day_of_week VARCHAR(50),
     airline VARCHAR(50),
     flight_number VARCHAR(50),
     tail_number VARCHAR(50),
     origin_airport VARCHAR(50),
     destination_airport VARCHAR(50),
     scheduled_departure VARCHAR(50),
     departure_time VARCHAR(50),
     departure_delay VARCHAR(50),
     taxi_out VARCHAR(50),
     wheels_off VARCHAR(50),
     scheduled_time VARCHAR(50),
     elapsed_time VARCHAR(50),
     air_time VARCHAR(50),
     distance VARCHAR(50),
     wheels_on VARCHAR(50),
     taxi_in VARCHAR(50),
     scheduled_arrival VARCHAR(50),
     arrival_time VARCHAR(50),
     arrival_delay VARCHAR(50),
     diverted VARCHAR(50),
     cancelled VARCHAR(50),
     cancellation_reason VARCHAR(50),
     air_system_delay VARCHAR(50),
     security_delay VARCHAR(50),
     airline_delay VARCHAR(50),
     late_aircraft_delay VARCHAR(50),
     weather_delay VARCHAR(50)

);