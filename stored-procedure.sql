use moviedb;

CREATE TABLE IF NOT EXISTS employees (
    email varchar(50) NOT NULL,
    password varchar(20) NOT NULL,
    fullname varchar(100),
    PRIMARY KEY (email)
);

INSERT INTO employees VALUES('classta@email.edu','classta','TA CS122B');


-- change delimiter to $$
delimiter $$


CREATE PROCEDURE add_movie(in movie_title varchar(100), in m_year int, in m_director varchar(100), 
                        in s_name varchar(100), in g_name varchar(32))
BEGIN
DECLARE new_mid varchar(10);
DECLARE new_sid varchar(10);
DECLARE new_gid int;
DECLARE s_id varchar(10);
DECLARE g_id int;
DECLARE s_flag int;

IF NOT EXISTS (SELECT 1 FROM movies m WHERE m.title = movie_title and m.year = m_year and m.director = m_director) THEN 
    -- add movies if no duplicates
    CALL getNextMovieId(new_mid);
    INSERT INTO movies VALUES (new_mid, movie_title, m_year, m_director);
        
    IF NOT EXISTS (SELECT 1 FROM stars WHERE stars.name = s_name) THEN
    -- if star is new
        CALL getNextStarId(new_sid);
        INSERT INTO stars (id, name) VALUES(new_sid, s_name);
        INSERT INTO stars_in_movies VALUES(new_sid, new_mid);
        SET s_flag := 0;
    ELSE
    -- if star is not new
        CALL getStarId(s_name, s_id);
        INSERT INTO stars_in_movies VALUES(s_id, new_mid);
        SET s_flag := 1;
    END IF;
                
    IF NOT EXISTS (SELECT 1 FROM genres WHERE genres.name = g_name) THEN
    -- if genre is new
        CALL getNextGenreId(new_gid);
        INSERT INTO genres VALUES(new_gid, g_name);
        INSERT INTO genres_in_movies VALUES(new_gid, new_mid);
        if (s_flag = 0) then
            -- star new genre new
            select concat("New Movie Id: ",new_mid, ", New Star Id: ", new_sid, ", New Genre Id: ", new_gid) as answer;
        else
            -- star existing genre new
            select concat("New Movie Id: ",new_mid, ", Existing Star Id: ", new_sid, ", New Genre Id: ", new_gid) as answer;
        end if;
    ELSE
    -- if genre is not new
        CALL getGenreId(g_name, g_id);
        INSERT INTO genres_in_movies VALUES(g_id, new_mid);
        if (s_flag = 0) then
            -- star new genre existing
            select concat("New Movie Id: ",new_mid, ", New Star Id: ", new_sid, ", Existing Genre Id: ", new_gid) as answer;
        else
            -- star existing genre existing
            select concat("New Movie Id: ",new_mid, ", Existing Star Id: ", new_sid, ", Existing Genre Id: ", new_gid) as answer;
        end if;
    END IF;
ELSE
    select concat("Duplicate Movie") as answer;
END IF;    
END $$
--
DELIMITER ;



-- change delimiter to $$
delimiter $$

CREATE PROCEDURE getGenreId(IN g_name varchar(100), OUT g_id int)
BEGIN
        set g_id := (SELECT g.id FROM genres g WHERE g.name = g_name limit 1);
END $$
--
DELIMITER ;


-- change delimiter to $$
delimiter $$

CREATE PROCEDURE getNextGenreId(OUT g_id int)
BEGIN
    SET g_id := (select (max(id) + 1) from genres as answer);
END $$
--
DELIMITER ;


-- change delimiter to $$
delimiter $$

CREATE PROCEDURE getNextMovieId(OUT m_id varchar(10))
BEGIN
    SET m_id := (select concat("tt", (select LPAD((SUBSTRING(max(id), 3,9) + 1), 7 ,"0" )from movies)));
END $$
--
DELIMITER ;



-- change delimiter to $$
delimiter $$

CREATE PROCEDURE getNextStarId(OUT s_id varchar(10))
BEGIN
    SET s_id := (select concat("nm", (select (SUBSTRING(max(id), 3,9) + 1) from stars)));
END $$
--
DELIMITER ;


-- change delimiter to $$
delimiter $$

CREATE PROCEDURE getStarId(in s_name varchar(100), OUT s_id varchar(10))
BEGIN
    SET s_id := (SELECT s.id FROM stars s WHERE s.name = s_name Limit 1);
END $$
--
DELIMITER ;


-- parsing -- parsing
-- parsing
-- parsing
-- parsing


-- change delimiter to $$
delimiter $$

CREATE PROCEDURE add_movie_parse(in m_director varchar(100), in movie_id varchar(10), in movie_title varchar(100), in m_year int, in g_name varchar(32))
BEGIN
DECLARE new_mid varchar(10) ;
DECLARE new_gid int;
DECLARE g_id int;

IF NOT EXISTS (SELECT 1 FROM movies m WHERE m.id = movie_id ) THEN 
    -- use existing movies id if not id duplicates
    INSERT INTO movies VALUES (movie_id, movie_title, m_year, m_director);
    -- update the genre
    IF NOT EXISTS (SELECT 1 FROM genres WHERE genres.name = g_name) THEN
    -- if genre is new
        CALL getNextGenreId(new_gid);
        INSERT INTO genres VALUES(new_gid, g_name);
        INSERT INTO genres_in_movies VALUES(new_gid, movie_id);
        select concat("Existing Genre Id: ", new_gid) as answer;
    ELSE
    -- if genre is not new
        CALL getGenreId(g_name, g_id);
        INSERT INTO genres_in_movies VALUES(g_id, movie_id);
        select concat("New Genre Id: ", g_id) as answer;
    END IF;
ELSE
    -- generate new movies id if id duplicates
    INSERT INTO movies VALUES (concat("x", movie_id), movie_title, m_year, m_director);
    -- update the genre
    IF NOT EXISTS (SELECT 1 FROM genres WHERE genres.name = g_name) THEN
    -- if genre is new
        CALL getNextGenreId(new_gid);
        INSERT INTO genres VALUES(new_gid, g_name);
        INSERT INTO genres_in_movies VALUES(new_gid, movie_id);
        select concat("Existing Genre Id: ", new_gid) as answer;
    ELSE
    -- if genre is not new
        CALL getGenreId(g_name, g_id);
        INSERT INTO genres_in_movies VALUES(g_id, movie_id);
        select concat("New Genre Id: ", g_id) as answer;
    END IF;
END IF;    

END $$
--
DELIMITER ;



-- change delimiter to $$
delimiter $$

CREATE PROCEDURE add_star_wy(in movie_id varchar(10), in s_name varchar(100), in s_year int)
BEGIN
DECLARE s_id int;
DECLARE new_sid int;

IF NOT EXISTS (SELECT 1 FROM stars s WHERE s.name=s_name) THEN 
    -- The star is new 
    CALL getNextStarId(new_sid);
    INSERT INTO stars (id, name, birthYear) VALUES(new_sid,s_name, s_year);
    INSERT INTO stars_in_movies VALUES(new_sid, movie_id);
ELSE
-- if star is not new
    CALL getStarId(s_name, s_id);
    INSERT INTO stars (id, name) VALUES(s_id, s_name);
    INSERT INTO stars_in_movies VALUES(s_id, movie_id);
END IF;    
END $$
--
DELIMITER ;


-- change delimiter to $$
delimiter $$

CREATE PROCEDURE add_star_woy(in movie_id varchar(10), in s_name varchar(100))
BEGIN
DECLARE s_id int;
DECLARE new_sid int;

IF NOT EXISTS (SELECT 1 FROM stars s WHERE s.name=s_name) THEN 
    -- The star is new 
    CALL getNextStarId(new_sid);
    INSERT INTO stars (id, name) VALUES(new_sid,s_name);
    INSERT INTO stars_in_movies VALUES(new_sid, movie_id);
ELSE
-- if star is not new
    CALL getStarId(s_name, s_id);
    INSERT INTO stars (id, name) VALUES(s_id, s_name);
    INSERT INTO stars_in_movies VALUES(s_id, movie_id);
END IF;    
END $$
--
DELIMITER ;



