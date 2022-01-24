insert into event (date_time, event_type, place, speaker, title, id)
VALUES (TIMESTAMP '2021-08-04 03:02:01', 'eventType1', 'place1', 'speaker1',
        'title1', gen_random_uuid()),
       (TIMESTAMP '2021-12-01 15:00:00', 'eventType2', 'place2', 'speaker2',
        'title2', gen_random_uuid()),
       (TIMESTAMP '2021-05-20 12:00:00', 'eventType3', 'place3', 'speaker3',
        'title3', gen_random_uuid());
