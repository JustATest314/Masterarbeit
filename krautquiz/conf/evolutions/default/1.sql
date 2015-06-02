# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table answer (
  answer_id                 varchar(255) not null,
  question_id               varchar(255),
  answer_text               varchar(255),
  vote_score                integer,
  owner_id                  varchar(255),
  page                      integer,
  constraint pk_answer primary key (answer_id))
;

create table nutzer (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_nutzer primary key (email))
;

create table question (
  question_id               varchar(255) not null,
  question_text             varchar(255),
  vote_score                integer,
  owner_id                  varchar(255),
  page                      integer,
  constraint pk_question primary key (question_id))
;

create sequence answer_seq;

create sequence nutzer_seq;

create sequence question_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists answer;

drop table if exists nutzer;

drop table if exists question;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists answer_seq;

drop sequence if exists nutzer_seq;

drop sequence if exists question_seq;

