create table content_submission
(
    id bigint generated always as identity primary key,
    url varchar(1000) not null,
    title varchar(500) null,
    summary text null,
    instructions text null,
    date_created timestamp not null,
    date_modified timestamp null,
    submission_status varchar(50) not null
);

create table podcast_episode
(
    id bigint generated always as identity primary key,
    title varchar(500) not null,
    audio_file varchar(1000) not null,
    script jsonb null,
    episode_number int not null default 1,
    show_notes text not null,
    description text not null,
    date_created timestamp not null
);

create sequence podcast_episode_number_seq start with 1 increment by 1;

create table podcast_host
(
    id bigint generated always as identity,
    name varchar(250) not null,
    style_instructions text not null,
    language_patterns text not null,
    voice_id varchar(250) not null,
    host_index int not null,
    primary key (id)
);

insert into podcast_host (
    host_index, name, style_instructions, language_patterns, voice_id
)
values (
    1,
    'Joop Snijder',
    '',
    '',
    'Vm0vfS4svZOhBRMa4ap3'
);

insert into podcast_host (
    host_index, name, style_instructions, language_patterns, voice_id
)
values (
    2,
    'Willem Meints',
    '',
    '',
    'XeK7XhqnGf6zErDLDutX'
);
