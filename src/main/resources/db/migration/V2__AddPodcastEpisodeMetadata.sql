alter table podcast_episode 
add column episode_number int not null default 1;

alter table podcast_episode 
add column show_notes text not null default '';

alter table podcast_episode 
add column description text not null default '';