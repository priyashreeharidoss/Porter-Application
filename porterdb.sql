DROP DATABASE IF EXISTS porterdb;
CREATE DATABASE porterdb;
USE porterdb;
CREATE TABLE driver_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name CHAR(60),
    pass CHAR(20),
    dob DATE,
    gender CHAR(6),
    phno CHAR(20),
    address CHAR(80),
    license_photo BLOB,
    license_no CHAR(30),
    exp_date DATE,
    type_of_vehicle CHAR(40)
);
CREATE TABLE user_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name CHAR(60),
    pass CHAR(20),
    email CHAR(30),
    dob DATE,
    phno CHAR(20),
    address CHAR(80)
);
CREATE TABLE user_home (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    pickpoint CHAR(30),
    droppoint CHAR(30),
    select_vehicle CHAR(10),
    book_status BOOLEAN,
    esti_km FLOAT,
    calculate_fare FLOAT,
    FOREIGN KEY (user_id) REFERENCES user_details(id)
);
SELECT * FROM user_details;
SELECT * FROM driver_details;
SELECT * FROM user_home;
