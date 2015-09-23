create table appuser (
        id number,
        userName varchar2(30),
        password varchar2(40),
        constraint appuser_pk primary key (id),
        constraint app_username_uk unique (userName)
);


create table appfractal (
        fid number PRIMARY KEY,
        c_x varchar2(5),
        c_y varchar2(5),
		id number, 
		CONSTRAINT fk_appuser 
               FOREIGN KEY (id)
               REFERENCES appuser(id)        
);

