alter TABLE rental_history
add foreign key (Cust_ID)
REFERENCES customers(Cust_ID);

alter TABLE rental_history
add foreign key (DVD_ID)
REFERENCES dvd_specific_info(DVD_ID);

alter TABLE Customers
add foreign key (Plan_type)
REFERENCES Plan(Plan_type);

alter TABLE payment_history
add foreign key (Cust_ID)
REFERENCES customers(Cust_ID);

alter TABLE payment_history
add foreign key (Plan_type)
REFERENCES Plan(Plan_type);

alter TABLE payment_history
add foreign key (Plan_type)
REFERENCES Plan(Plan_type);

alter TABLE dvd_specific_info
add foreign key (Movie_ID)
REFERENCES Movies(Movie_ID);

alter TABLE dvd_specific_info
add foreign key (Series_ID)
REFERENCES Series(Series_ID);

alter TABLE dvd_specific_info
add foreign key (DVD_Series_Split_ID)
REFERENCES dvd_series_split(DVD_Series_Split_ID);

alter TABLE cust_online_viewing_history
add foreign key (Cust_ID)
REFERENCES customers(Cust_ID);

alter TABLE cust_online_viewing_history
add foreign key (Movie_ID)
REFERENCES Movies(Movie_ID);

alter TABLE cust_online_viewing_history
add foreign key (Ep_ID)
REFERENCES episode(Ep_ID);


alter TABLE cust_ratings
add foreign key (Movie_ID)
REFERENCES Movies(Movie_ID);

alter TABLE cust_ratings
add foreign key (EP_ID)
REFERENCES episode(Ep_ID);

alter TABLE cust_ratings
add foreign key (Cust_ID)
REFERENCES customers(Cust_ID);

alter TABLE dvd_series_split
add foreign key (Season_ID)
REFERENCES season(Season_ID);


alter TABLE person
add foreign key (Movie_ID)
REFERENCES Movies(Movie_ID);

alter TABLE person
add foreign key (Season_ID)
REFERENCES Season(Season_ID);

alter TABLE person
add foreign key (Person_ID)
REFERENCES person_info(Person_ID);

alter TABLE Genre
add foreign key (Movie_ID)
REFERENCES Movies(Movie_ID);

alter TABLE Genre
add foreign key (Series_ID)
REFERENCES Series(Series_ID);


alter table Season
add foreign key (Series_ID)
references series(series_ID);


alter TABLE episode
add foreign key (Season_ID)
REFERENCES season(Season_ID);







-- DESCRIBE rental_history;
