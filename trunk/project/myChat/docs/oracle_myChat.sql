create table Users  (
   userId                varchar2(10)    not null primary key,
  uName             VARCHAR2(20) unique,
   uPassword          VARCHAR2(30),
   uAge              INTEGER default 0,
  uEmail           VARCHAR2(20)
);

create table Messages  (
   mId                varchar2(10)    not null primary key,
   mTo                varchar2(10) ,
   mFrom              varchar2(10) ,
   mMessage           VARCHAR2(255),
   mDate              DATE default sysdate,
   mServerMessage     VARCHAR2(200)
);
alter table Messages
   add constraint FK_MESSAGS_USERS_to foreign key (mTo)
      references Users (userId);

alter table Messages
   add constraint FK_MESSAGES_USERS_from foreign key (mFrom)
      references Users (userId);
-- Create sequence 
create sequence USERS_ID
minvalue 1
maxvalue 9999999
start with 1
increment by 1
cache 20
cycle
order;
-- Create sequence 
create sequence MESSAGES_MID
minvalue 1
maxvalue 9999999
start with 1
increment by 1
cache 20
cycle
order;
--触发器
create or replace trigger Users_id_tri
  before insert on users  
  for each row
begin
  select 'u'|| to_char(USERS_ID.NEXTVAL,'FM000000') into :new.userId from dual;
end Users_id_tri;
------------
create or replace trigger Messages_mid_tri
  before insert on Messages  
  for each row
begin
  select 'm'|| to_char(USERS_ID.NEXTVAL,'FM000000') into :new.mId from dual;
end Messages_mid_tri;
--------------增加系统用户
insert into users (uname,upassword,uage,uemail)values('系统','系统',00,'myChat@163.com');
insert into users (uname,upassword,uage,uemail)values('所有用户','所有用户',00,'myChat@163.com');
