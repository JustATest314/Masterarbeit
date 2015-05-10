# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table question (
  question_id               varchar(255) not null,
  question_text             varchar(255),
  vote_score                integer,
  user_id                   varchar(255),
  constraint pk_question primary key (question_id))
;

create sequence question_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists question;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists question_seq;

