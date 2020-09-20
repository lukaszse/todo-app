drop table if exists projects;
create table projects(
    id int primary key auto_increment,
    description varchar(100)
);

alter table task_groups add column project_id int null;
alter table task_groups add foreign key (project_id) references projects (id);

drop table if exists project_steps;
create table project_steps(
    id int primary key auto_increment,
    description varchar(100),
    project_id int,
    foreign key (project_id) references projects (id),
    days_to_deadline int
);