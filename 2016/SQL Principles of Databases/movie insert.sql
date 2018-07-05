 -- INSERT INTO Movies (Movie_Title, Movie_YearRelease, Movie_CountryOrigin)
 -- VALUES ('The Lord of the Rings: The Fellowship of the Ring', 2001, 'USA');

-- SELECT Movie_Title FROM Movies;
DELIMITER //
CREATE PROCEDURE `insert_to_movie` (IN myMovie varchar(255))
	/*
    BEGIN
		SELECT * FROM Movies;
	END //
    */
	BEGIN
		IF (myMovie = (SELECT Movie_Title FROM Movies WHERE Movie_Title = myMovie)) 
        THEN
        SELECT Movie_Title FROM Movies WHERE Movie_Title = myMovie;
    END IF;
    END;
    
-- 	SELECT * FROM Movies
--   WHERE EXISTS (SELECT Movie_Title FROM Movies WHERE Movie_Title = myMovie);


  call insert_to_movie('Mad Max: Fury Road')