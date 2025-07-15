create table content_submission (
    id bigint generated always as identity primary key,
    url varchar(1000) not null,
    title varchar(500) null,
    summary text null,
    date_created timestamp not null,
    date_modified timestamp null,
    submission_status varchar(50) not null
);

create table podcast_episode(
    id bigint generated always as identity primary key,
    title varchar(500) not null,
    audio_file varchar(1000) not null,
    script text null,
    date_created timestamp not null
);