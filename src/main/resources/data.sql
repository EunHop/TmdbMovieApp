insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo@test.com', '{noop}1234', 'choo', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo2@test.com', '{noop}1234', 'choo2', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo3@test.com', '{noop}1234', 'choo3', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo4@test.com', '{noop}1234', 'choo4', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo5@test.com', '{noop}1234', 'choo5', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo6@test.com', '{noop}1234', 'choo6', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo7@test.com', '{noop}1234', 'choo7', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo8@test.com', '{noop}1234', 'choo8', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo9@test.com', '{noop}1234', 'choo9', 'USER', true, now(), now());
insert into user (`email`, `password`, `name`, `role`, `enabled`, `created_at`, `modified_at`)
values ('choo10@test.com', '{noop}1234', 'choo10', 'USER', true, now(), now());

insert into video (`id`, `score`, `release_date`, `title`, `poster_path`, `media_type`, `tagline`, `created_at`, `modified_at`)
values (575264, 77, '2023-07-08', '미션 임파서블: 데드 레코닝 PART ONE', '/nQsWPG020kSWdOl3EhFXRNE2s0n.jpg', 'movie', '가장 위험한 작전, 그의 마지막 선택', now(), now());

insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (true, 575264, 1, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 2, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 3, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 4, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 5, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 6, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 7, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 8, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 9, 'ajalkdjhfaks', now(), now());
insert into user_and_video (`wish`, `video_id`, `user_id`, `review`, `created_at`, `modified_at`)
values (false, 575264, 10, 'ajalkdjhfaks', now(), now());