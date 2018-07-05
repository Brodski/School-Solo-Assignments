
/*
ALTER TABLE person
ADD Person_Occupation varchar(30);
*/

/*
Alter table series
ADD Series_Num_of_Seasons INT;
*/
/*
Create table `Disc_description`
(
  `Disc_desc_ID` INT NOT NULL AUTO_INCREMENT,
  `Disc_language` VARCHAR(20) NULL,
  `Disc_video_quality` VARCHAR(30) NULL,
  `Disc_audio_quality` VARCHAR(30) NULL,
  `Disc_description` VARCHAR(200) NULL,
  PRIMARY KEY (`Disc_desc_ID` ))
ENGINE = InnoDB; 


ALTER TABLE dvd_specific_info
ADD Disc_desc_ID INT NOT NULL;

ALTER TABLE dvd_series_split
ADD Disc_desc_ID INT NOT NULL;


ALTER TABLE dvd_specific_info
ADD FOREIGN KEY (disc_desc_ID)
REFERENCES Disc_description('Disc_descr_ID');


ALTER TABLE dvd_series_split
ADD FOREIGN KEY (disc_desc_ID)
REFERENCES Disc_description('Disc_descr_ID');


ALTER TABLE dvd_description
ADD Disc_format VARCHAR(10) NULL;

ALTER TABLE disc_description
ADD Disc_format VARCHAR(10) NULL;


ALTER table dvd_specific_info
DROP COLUMN series_ID, 
DROP COLUMN DVD_language;


CREATE TABLE Disc_Languages(
 Disc_Languages VARCHAR(30),
 Disc_desc_ID INT
);

ALTER TABLE Disc_Languages
ADD FOREIGN KEY (Disc_desc_ID)
REFERENCES disc_description(Disc_desc_ID);


CREATE TABLE `newSchoolProj`.`Cust_Queue` (
  `Cust_ID` INT NOT NULL,
  `Movie_ID` INT NULL,
  `DVD_Series_Split` INT NULL
  `EP_ID` INT NULL;
  )
ENGINE = InnoDB;


ALTER TABLE cust_queue
ADD foreign key (EP_ID)
references episode(ep_ID);


ALTER TABLE cust_queue
ADD foreign key (DVD_Series_split_ID)
references DVD_Series_split(DVD_Series_split_ID);


ALTER TABLE cust_queue
ADD foreign key (Movie_ID)
references movies(movie_ID);


--------------------------------------------------


/*INSERT INTO customers (plan_type, Cust_FName, Cust_LName, Cust_State, Cust_Zip, Cust_Address, Cust_payment)
VALUES 
('plan A', 'John', 'Jackson', 	'CO', '80222', '123 Elms St', '4123000'),
('plan A', 'Bruce', 'Banner', 	'CO', '80123', '33 Clover St', '2222222'),
('plan B', 'Frank', 'Castle', 	'NY', '55322', '54 Rose Dr', '3333333'),
('plan B', 'Eddie', 'Brock', 	'NY', '12345', '333 S Oak St', '44444444'),
('plan B', 'Peter', 'Parker',	'NY', '43222', '777 Evergreen St', '5555555'),
('plan A', 'Bruce', 'Wayne', 	'CA', '76538', '3888 E Goth St', '66666666'),
('plan C', 'Kent', 'Clark', 	'NY', '80222', '321 Tech St', '7777777'),
('plan A', 'Barbra', 'Streisand', 'TX', '69838', '123 Elms St', '123341234'),
('plan A', 'Austin', 'Jones', 	'CO', '80232', '82033 W Jewel Ave', '888888888');
*/

/*
INSERT INTO plan 
VALUES 
('plan A', 'HD', 0, 7.99),
('plan B', 'HD', 2, 10.99),
('plan C', 'Ultra HD', 2, 13.99),
('plan D', 'None', 2, 7.99);
*/

/*
INSERT INTO movies (movie_title, movie_yearrelease, movie_description)
VALUES 
('Mad Max: Fury Road', 2015, 'Epic post-apoc movie'),
('Star Wars: A New Hope', 1997, 'Epic space opera film'),
('Star Wars: The Empire Strikes Back', 1980, 'After the destruction of the Death Star...'),
('Star Wars: Return of the Jedi', 1993, 'After rescuing Han Solo from the palace of Jabba ....'),
('Terminator 2: Jugment Day', 1991, 'Arnold Schw. goes back in time to save the world '),
('Lord of the Rings: The Fellowship of the Ring', 2001, 'Mr Frodo must destroy a ring to save the world'),
('Lord of the Rings: The Two Towers', 2002, 'Mr Frodo is still trying to destroy a ring to to save the world'),
('Lord of the Rings: The Return of the King', 2003, 'Saurons forces have laid Siege to Minas Tirith, in their...'),
('The Matrix', 1999, 'A computer hacker learns from mysterious rebels about the ....'),
('300', 2006, ' Epic fantasy war film about King Leonidas and some sweet battle'),
('The Godfather', 1972, 'The epic tale of a 1940s New York Mafia family and their struggle to protect their empire'),
('First BLood', 1982, 'Vetnam veteran and drift John rambo wanders into a....'),
('300', 2006, ' Epic fantasy war film about King Leonidas and some sweet battle'),
('Black Swan', 2010, 'Black Swan follows the stor of Nina, a ballerina in...'),
('Serenity', 2005, 'An American space Western film ....'),
('V for Vendetta', 2006, 'Following world war, London is a police state occupied...');
*/


/*
INSERT INTO series (Series_Title, Series_Description, Series_Num_of_Seasons)
VALUES 
('Game of Thrones', 'A fantasy drama television series created by David Benioff and D B Weiss...', 6),
('Firefly', 'A space westeren drama television series created by writer and director Joss Whedon...',1),
('Seinfeld', 'Four single friends deal with the absurdities of everyday life in New York',9)
*/

/*
ALTER TABLE season
change description season_description varchar(300);
*/


/*
INSERT INTO season (Series_ID, season_num, season_description)
VALUES 
(1, 1, 'Season 1 of Game of Thrones'),
(1, 2, 'Season 2 of Game of Thrones'),
(1, 3, 'Season 3 of Game of Thrones'),
(2, 1, 'Season 1 of Firefly'),
(3, 1, 'Season 1 of Seinfeld'),
(3, 2, 'Season 2 of Seinfeld'),
(3, 3, 'Season 3 of Seinfeld'),
(3, 4, 'Season 4 of Seinfeld'),
(3, 5, 'Season 5 of Seinfeld');

*/

/*
INSERT INTO episode (season_ID, Ep_Num, Ep_title, Ep_description)
VALUES
(1, 1, 'Episode 1 GoT', 'In episode 1, __ happens'),
(1, 2, 'Episode 2 GoT', 'In episode 2, __ happens'),
(1, 3, 'Episode 3 GoT', 'In episode 3, __ happens'),
(1, 4, 'Episode 4 GoT', 'In episode 4, __ happens'),
(1, 5, 'Episode 5 GoT', 'In episode 5, __ happens'),
(1, 6, 'Episode 6 GoT', 'In episode 6, __ happens'),
(1, 7, 'Episode 7 GoT', 'In episode 7, __ happens'),
(1, 8, 'Episode 8 GoT', 'In episode 8, __ happens'),
(1, 9, 'Episode 9 GoT', 'In episode 9, __ happens'),
(1, 10, 'Episode 10 GoT', 'In episode 10, __ happens'),
(2, 1, 'Episode 1 GoT, s2', 'In episode 1 of season 2, __ happens'),
(2, 2, 'Episode 2 GoT, s2', 'In episode 2 of season 2, __ happens'),
(2, 3, 'Episode 3 GoT, s2', 'In episode 3 of season 2, __ happens'),
(2, 4, 'Episode 4 GoT, s2', 'In episode 4 of season 2, __ happens'),
(2, 5, 'Episode 5 GoT, s2', 'In episode 5 of season 2, __ happens'),
(2, 6, 'Episode 6 GoT, s2', 'In episode 6 of season 2, __ happens'),
(3, 1, 'Episode 1 GoT, s2', 'In episode 1 of season 3, __ happens'),
(4, 1, 'Episode 1 Firefly', 'In episode 1 of firefly, __ happens'),
(4, 2, 'Episode 2 Firefly', 'In episode 2 of firefly, __ happens'),
(4, 3, 'Episode 3 Firefly', 'In episode 3 of firefly, __ happens'),
(4, 4, 'Episode 4 Firefly', 'In episode 4 of firefly, __ happens');
*/


/*
INSERT INTO person_info (person_fname, person_LName,  person_description)
VALUES 
('Arnold', 'Schwarzenegger', 'Body builder from Austria...'),
('Joss', 'Whedon', 'He writes sci fi and fantasy....'),
('James', 'Cameron',  'James was born on Aug 17, 1954 in Canada...'),
('Linda', 'Jamilton', 'Born in Salisbury, Maryland, USA following highschool Linda...'),
('Peter', 'Dinklage', 'Peter was born in Morristown NewJersey to an elentary school...'),
('Lena', 'Headey', 'Lena is a British actress born in Bermuda to parent...'),
('Steven', 'Spielberg', 'One of the most influential film personalities in film'),
('David', 'Benioff', 'An American novelist, screenwriter, tv producer. Born in ...'),
('Daniel', 'Brett', 'An American author and screenwriter Famous for ...');
('Jerry', 'Seinfeld', 'A new york comedian, he is pretty funny');

*/

/*
INSERT INTO person (person_ID, movie_ID, season_ID, person_occupation)
VALUES 
-- (1, 5, NULL, 'ActorActress'),
-- (2, 15, null, 'Writer');
(2, NULL, 4, 'Writer'),
-- (2, NULL, 4, 'Director')
(3, 5, NULL, 'Writer'),
(4, 5, NULL, 'ActorActress'),
(5, NULL, 1, 'ActorActress'),  -- petter
(5, NULL, 2, 'ActorActress'),
(5, NULL, 3, 'ActorActress'),
(6, NULL, 1, 'ActorActress'), -- Lena
(6, NULL, 2, 'ActorActress'),
(6, NULL, 3, 'ActorActress'),

(7, 2, NULL, 'Director'), -- spielburg
(7, 3, NULL, 'Director'),
(7, 4, NULL, 'Director'),

(8, NULL, 1, 'Writer'), -- writer 1
(8, NULL, 2, 'Writer'),
(8, NULL, 3, 'Writer'),

(9, NULL, 1, 'Writer'),
(9, NULL, 2, 'Writer'),
(9, NULL, 3, 'Writer');

(10, NULL, 5, 'ActorActress'), -- Jerry Seinfeld
(10, NULL, 6, 'ActorActress'),
(10, NULL, 7, 'ActorActress'),
(10, NULL, 8, 'ActorActress'),
(10, NULL, 9, 'ActorActress'),

(10, NULL, 5, 'Writer'),
(10, NULL, 6, 'Writer'),
(10, NULL, 7, 'Writer'),
(10, NULL, 8, 'Writer'),
(10, NULL, 9, 'Writer');

*/


/*
INSERT INTO dvd_specific_info (movie_id, series_id, dvd_series_split_id, dvd_status, dvd_new_as_of_x)
VALUES 
/*
(1, NULL, NULL, 'Ready', '2010-01-10'),
(1, NULL, NULL, 'Ready', '2016-01-10'),
(1, NULL, NULL, 'Shipped', '2016-01-10'),
(2, NULL, NULL, 'Ready', '2010-03-20'), -- starwars
(2, NULL, NULL, 'Broken', '2010-03-20'),
(2, NULL, NULL, 'Shipped', '2010-03-20'),
(2, NULL, NULL, 'Shipped', '2010-02-01'),
(3, NULL, NULL, 'Shipped', '2013-02-01'),
(3, NULL, NULL, 'Shipped', '2013-01-01'),
(3, NULL, NULL, 'Shipped', '2014-01-01');
(4, NULL, NULL, 'Ready', '2001-6-03'),
(4, NULL, NULL, 'Broken', '2001-6-03');
*/

/*

INSERT INTO disc_description (disc_language, disc_format, disc_video_quality, disc_audio_quality)
VALUES 
('English', 'DVD', 'Widescreen Anamorphic 1.78:1', 'Dolboy Digital 5.1'),
('English', 'Blue Ray','Widescreen Anamorphic 1.78:1', 'Dolboy Digital 5.1');


INSERT INTO dvd_series_split(season_id, disc_num, disc_desc_ID)
VALUES
(4, 1, 1), -- GoT season 1 DVD
(4, 2, 1), -- GoT season 1
(4, 3, 1), -- GoT season 1

(4, 1, 2),  -- GoT season 1 blue ray?
(4, 2, 2),  -- GoT season 1
(4, 3, 2); -- GoT season 1


INSERT INTO cust_online_viewing_history
VALUES 
(10, 2, NULL, '2015-05-15'),
(10, 3, NULL, '2015-05-16'),
(10, 4, NULL, '2015-05-17'),
(11, NULL, 1, '2015-04-01'),
(11, NULL, 2, '2015-04-01'),
(11, NULL, 3, '2015-04-01');


INSERT INTO Genre -- (movie id, series id, genre type)
VALUES 
(null, 1, 'Drama'),
(null, 1, 'Dark'),
(null, 1, 'Fantasy'),
(null, 1, 'War'),
(null, 1, 'HBO'),
(null, 1, 'Adventure'),
(null, 2, 'Sci fi'),
(null, 2, 'Fox'),
(null, 2, 'Adventure'),
(1, null, 'Action'),
(1, null, 'Sci fi'),
(1, null, 'Thriller');
(6, null, 'Adventure'),
(6, null, 'Fantasy');

  


UPDATE dvd_specific_info
SET Disc_desc_ID = 1
WHERE DVD_ID=12;


INSERT INTO dvd_specific_info (movie_id, series_id, dvd_series_split_id, dvd_status, dvd_new_as_of_x, disc_desc_ID)
VALUES 
(NULL, NULL, 1, 'Ready', '2014-01-10', 1);
(NULL, NULL, 1, 'Ready', '2014-01-10', 1);
(NULL, NULL, 2, 'shipped', '2014-01-10', 1);



INSERT INTO rental_history (cust_ID, DVD_ID, DVD_Rented_Date, DVD_Return_date)
VALUES 
(13, 7, '2016-05-15', '2016-05-24'),
(13, 10, '2016-05-15', '2016-05-24'),
(13, 2, '2016-05-15', '2016-05-24'),
(13, 3, '2016-05-15', '2016-05-24'),
(13, 10, '2016-05-25', '2016-05-30'),
(14, 3, '2016-05-24', '2016-05-28'),
(14, 7, '2016-05-24', '2016-05-28'),
(17, 15, '2016-05-24', '2016-05-30');



INSERT INTO cust_queue
VALUES ( 15, 5, NULL, NULL), (15, 13, NULL, NULL), (16, NULL, 4, NULL),(16, NULL, NULL, 5);

INSERT INTO payment_history
VALUES 
(11, 'plan B', '2013-05-05', 9.99), 
(11, 'plan B', '2013-06-05', 9.99),
(11, 'plan B', '2013-07-05', 9.99),
(12, 'plan C', '2015-01-01', 12.99),
(12, 'plan C', '2015-02-01', 12.99),
(12, 'plan C', '2016-04-10', 12.99),
(12, 'plan C', '2015-05-10', 12.99);

*/
/*
---------------------------------------
--- 	SELECT ALL PAYMENT HISTORY's FOR EACH CUSTOMER
---
--------------------------------------
select c.Cust_ID, Cust_FName, c.Cust_LName, p.plan_type, p.payment_date, p.payment_amount
FROM customers AS c
INNER JOIN payment_history AS p 
ON c.Cust_ID=p.Cust_ID
order by c.Cust_LName, c.Cust_FName;

*/
/*
---------------------------------
-----	SELECT ALL CUSTOMERS DVD RENTAL HISTORY just movies?
-----
-----------------------------
SELECT c.Cust_FName, c.cust_LName, M.Movie_Title, M.Movie_YearRelease, RH.DVD_Rented_Date, RH.DVD_Return_Date, DD.Disc_format
FROM customers AS c
INNER JOIN rental_history AS RH
	ON RH.Cust_ID = C.Cust_ID
INNER JOIN dvd_specific_info AS DSI
	ON RH.DVD_ID = DSI.DVD_ID
INNER JOIN movies AS M
	ON m.Movie_ID = DSI.Movie_ID
INNER JOIN disc_description AS DD
	ON DSI.Disc_desc_ID = DD.Disc_desc_ID
ORDER BY c.Cust_LName, c.Cust_FName;


----------------------------------
----	SELECT ALL CUSTOMERS RENTAL HISTORY movies and dvd
----
----------------------------------

SELECT c.Cust_FName, c.cust_LName, M.Movie_Title, M.Movie_YearRelease, DSS.Disc_Num, S.Season_Num, Series_Title, RH.DVD_Rented_Date, RH.DVD_Return_Date, DD.Disc_format
FROM customers AS c
INNER JOIN rental_history AS RH		-- RentalHist & CustID
	ON RH.Cust_ID = C.Cust_ID
INNER JOIN dvd_specific_info AS DSI -- ...& DVD_specific_Info
	ON DSI.DVD_ID = RH.DVD_ID
LEFT JOIN dvd_series_split AS DSS	-- ... & dvd_series_split
	ON DSS.DVD_Series_Split_ID = DSI.DVD_Series_Split_ID
LEFT JOIN season AS S			 	-- ... & season
	ON S.Season_ID = DSS.Season_ID
LEFT JOIN series					-- ... & series
	ON series.Series_ID = S.Series_ID  
LEFT JOIN movies AS M				-- ... OR Movies
	ON m.Movie_ID = DSI.Movie_ID 
INNER JOIN disc_description AS DD	-- ... & disc description
	ON DD.Disc_desc_ID = DSI.Disc_desc_ID 
ORDER BY c.Cust_LName, c.Cust_FName;

--------------------------------
----	SELECT ALL CUSTOMERS ONLINE HISTORY
----
--------------------------------
SELECT c.cust_LName, c.cust_FNAme, COnline.Cust_Online_Date_watched, Series.series_title, S.Season_Num,E.Ep_Num, E.Ep_Title,   M.Movie_Title
FROM Customers AS c
INNER JOIN cust_online_viewing_history AS COnline
	ON COnline.Cust_ID = c.Cust_ID
LEFT JOIN episode AS E
	ON E.Ep_ID = COnline.Ep_ID
LEFT JOIN Season AS S
	ON S.Season_ID = E.season_ID
LEFT JOIN series
	ON series.series_ID= S.series_ID
LEFT JOIN Movies AS M
	ON M.Movie_ID = COnline.movie_ID
ORDER BY c.Cust_LName, c.Cust_FName,COnline.Cust_Online_Date_watched;


    -----------------------------------
    ----	SELECT_ALL_CUSTOMERS_ QUEUE
    ----
    ------------------------------------
  SELECT  CQ.cust_ID, Series.series_title, S.Season_Num, E.Ep_Num, DSS.Disc_Num, E.Ep_Title,   M.Movie_Title
  FROM cust_queue AS CQ
  
  LEFT JOIN dvd_series_split AS DSS			-- season and disc. --This is not perfect code b/c it repeats LEFT JOIN dvd_specific_info but I did it for clarity
  ON DSS.DVD_Series_Split_ID = CQ.DVD_Series_Split_ID 
  
  LEFT JOIN dvd_specific_info AS DSI		-- DVD INFO
  ON DSI.DVD_Series_Split_ID = DSS.DVD_Series_Split_ID
  LEFT JOIN disc_description AS DD
  ON DD.Disc_desc_ID = DSI.Disc_desc_ID
  LEFT JOIN disc_languages AS DL
  ON DL.Disc_desc_ID = DD.Disc_desc_ID  

  LEFT JOIN episode AS E 		-- episode FROM customers
  ON e.Ep_ID = CQ.EP_ID
  LEFT JOIN Season AS S
  ON S.Season_ID = E.season_ID 
  OR DSS.Season_ID = S.Season_ID
  LEFT JOIN series
  ON series.series_ID= S.series_ID
  
  LEFT JOIN Movies AS M			-- movie FROM customers
  on M.movie_ID=CQ.movie_ID;
  
    
---------------------------------------------
---	SELECT_BY_GENRE
--- (get movies & series back)
--------------------------------------------
SELECT g.genre_type, m.movie_title, series.series_title
FROM genre AS g
LEFT JOIN series 
	on series.series_ID = g.series_ID
LEFT JOIN Movies AS M
	ON M.movie_ID = g.movie_ID
 WHERE g.genre_type = 'Fantasy';

-----------------------------------------
--- SELECT_BY_PERSON
---
---------------------------------------

SELECT p.person_ID, PI.Person_LName, PI.Person_FName, P.Person_Occupation, M.Movie_Title, S.Season_Num, series.series_title
FROM person AS P
INNER JOIN person_info as PI
ON P.person_ID = PI.person_ID
LEFT JOIN season AS S
ON S.season_ID = P.Season_ID
LEFT JOIN series
ON series.Series_ID = S.Series_ID
LEFT JOIN MOvies AS M
ON M.Movie_ID = P.Movie_ID
WHERE pi.person_LName ='Dinklage';

-----------------------------------------
--- count_DVDs
--- COUNTS ALL DVD's with same Movie_ID or DVD_Series_Split_ID
---
---------------------------------------

DELIMITER //
CREATE PROCEDURE `count_DVDs`()
BEGIN
	SELECT M.movie_Title, series.Series_Title, DSS.Disc_Num, DSI.Movie_ID, DSI.DVD_Series_Split_ID, 
             count(DSI.Movie_ID) AS NumberOfDVDs_movies, 
             count(DSI.DVD_Series_Split_ID) AS NumberOFDVDs_Series 
	FROM dvd_specific_info AS DSI
	LEFT JOIN movies AS M
		ON M.movie_ID = DSI.movie_ID
	LEFT JOIN dvd_series_split AS DSS
		ON DSS.DVD_Series_Split_ID = DSI.DVD_Series_Split_ID
	LEFT JOIN season as S
		ON S.season_ID = DSS.season_ID
	LEFT JOIN series
		on series.series_ID = S.Series_ID
	group by DSI.movie_ID, DSI.DVD_Series_Split_ID;
	
    END;

------------------------------------
--- count_DVDs_with_status_ready
--- SELECT DVDs from dvd_specific_info with Status=Ready
---
------------------------------------
	SELECT M.movie_Title, series.Series_Title, DSS.Disc_Num, DSI.Movie_ID, DSI.DVD_Series_Split_ID, 
		count(DSI.Movie_ID) AS NumberOfDVDs_movies, 
		count(DSI.DVD_Series_Split_ID) AS NumberOFDVDs_Series 
    FROM dvd_specific_info AS DSI
	LEFT JOIN movies AS M
		ON M.movie_ID = DSI.movie_ID
	LEFT JOIN dvd_series_split AS DSS
		ON DSS.DVD_Series_Split_ID = DSI.DVD_Series_Split_ID
	LEFT JOIN season as S
		ON S.season_ID = DSS.season_ID
	LEFT JOIN series
		on series.series_ID = S.Series_ID
	WHERE DSI.DVD_Status = 'Ready'
	group by DSI.movie_ID, DSI.DVD_Series_Split_ID;
    
  */  


call count_DVDs();

call count_DVDs_with_status_ready();

call select_all_customer_queue();

call select_by_genre('Sci fi');

call select_by_genre('Adventure');

call select_by_person('Dinklage');

call select_by_person('Seinfeld');

call select_persons_from_movies_or_series('Game of Thrones');

call select_customers_payment_history('Castle');

    





  SELECT  CQ.cust_ID, Series.series_title, S.Season_Num, 
		E.Ep_Num, DSS.Disc_Num, E.Ep_Title,   M.Movie_Title
  FROM cust_queue AS CQ
  
  LEFT JOIN dvd_series_split AS DSS			
	ON DSS.DVD_Series_Split_ID = CQ.DVD_Series_Split_ID 
  LEFT JOIN dvd_specific_info AS DSI		
	ON DSI.DVD_Series_Split_ID = DSS.DVD_Series_Split_ID

  LEFT JOIN episode AS E 		
  ON e.Ep_ID = CQ.EP_ID
  LEFT JOIN Season AS S
	ON S.Season_ID = E.season_ID 
	OR DSS.Season_ID = S.Season_ID  
  LEFT JOIN series
	ON series.series_ID= S.series_ID
  LEFT JOIN Movies AS M			
	ON M.movie_ID=CQ.movie_ID;
    
    
    
SELECT c.cust_LName, c.cust_FNAme, myTable.Cust_Online_Date_watched, Series.series_title, S.Season_Num,E.Ep_Num, E.Ep_Title, M.Movie_Title
			FROM Customers AS c
			INNER JOIN cust_online_viewing_history 	 -- cust_online_viewing_history AS COnline
				ON cust_online_viewing_history.Cust_ID = c.Cust_ID
			LEFT JOIN episode AS E
				ON E.Ep_ID = COnline.Ep_ID
			LEFT JOIN Season AS S
				ON S.Season_ID = E.season_ID
			LEFT JOIN series
				ON series.series_ID= S.series_ID
			LEFT JOIN Movies AS M
				ON M.Movie_ID = COnline.movie_ID
			ORDER BY c.Cust_LName, c.Cust_FName, myTable.Cust_Online_Date_watched;