CREATE TABLE tlog (
id int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id' ,
operation_time datetime NOT NULL COMMENT '操作时间' ,
type varchar(32) COMMENT '日志类型' ,
content varchar(256) COMMENT '日志内容' ,
log_status varchar(8) COMMENT '状态' ,
flag char(1) COMMENT '标志' ,
PRIMARY KEY (`id`)
);

DROP FUNCTION IF EXISTS rand_num;
DELIMITER $$
CREATE FUNCTION rand_num(num INT, op INT)
 RETURNS int(11)
BEGIN
  DECLARE i int DEFAULT 1;
  DECLARE chr VARCHAR(255) DEFAULT '';
  DECLARE n int;
	SET n = FLOOR(1 + RAND() * OP);
	WHILE  i<=num
	DO
		SET chr = concat(chr,n);
		SET i=i+1;
	END WHILE;
  RETURN chr;
END $$
DELIMITER ;

DROP FUNCTION IF EXISTS rand_str;
DELIMITER $$
CREATE FUNCTION rand_str(begin INT, end INT)
RETURNS VARCHAR(255)
BEGIN
    DECLARE chars_str varchar(100) DEFAULT 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    DECLARE return_str varchar(255) DEFAULT '';
    DECLARE n INT;
    DECLARE i INT DEFAULT 0;
    SET n = FLOOR(begin + RAND()*(end-begin+1));
    WHILE i < n DO
        SET return_str = concat(return_str,substring(chars_str , FLOOR(1 + RAND()*62 ),1));
        SET i = i +1;
    END WHILE;
    RETURN return_str;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS pro_gen;
DELIMITER $$
CREATE PROCEDURE pro_gen(in mykey VARCHAR(32), in num INT)
BEGIN
    DECLARE i INT DEFAULT 1;
    WHILE i<=num DO
        INSERT INTO tlog(operation_time,type,content,log_status,flag) VALUES(DATE_ADD(mykey,  INTERVAL  FLOOR(1 + (RAND() * 86400))   SECOND ),rand_num(4,7),rand_str(12,16),rand_num(2,2),rand_num(1,4));
        SET i = i+1;
    END WHILE;
END $$
DELIMITER ;

#select t.date_key,count(1) from (select date_format(operation_time,'%Y-%m-%d') date_key, a.* from TLog a) t
group by t.date_key order by date_key

#call pro_gen('2019-07-18',2000);

