DROP TABLE IF EXISTS drone_medication;
DROP TABLE IF EXISTS drone;
DROP TABLE IF EXISTS medication;

CREATE TABLE IF NOT EXISTS drone
(
    `battery_capacity` int          NOT NULL,
    `model`            tinyint      NOT NULL,
    `state`            tinyint      NOT NULL,
    `weight_limit`     int          NOT NULL,
    `serial_number`    varchar(100) NOT NULL,
    PRIMARY KEY (`serial_number`),
    CONSTRAINT `drone_chk_1` CHECK ((`model` between 0 and 3)),
    CONSTRAINT `drone_chk_2` CHECK ((`state` between 0 and 5))
);


CREATE TABLE IF NOT EXISTS medication
(
    weight    int          NOT NULL,
    code      varchar(255) NOT NULL,
    imageuuid varchar(255) DEFAULT NULL,
    name      varchar(255) DEFAULT NULL,
    image     longblob,
    PRIMARY KEY (code)
);

CREATE TABLE if not exists drone_medication
(
    drone_serial_number varchar(100) NOT NULL,
    medication_code     varchar(255) NOT NULL,
    KEY FKhcdfa8utdc0l9dgl30onq2mgr (drone_serial_number),
    KEY FKis0pb1m8yvw6he6dngdd7kijn (medication_code),
    CONSTRAINT FKhcdfa8utdc0l9dgl30onq2mgr FOREIGN KEY (drone_serial_number) REFERENCES drone (serial_number),
    CONSTRAINT FKis0pb1m8yvw6he6dngdd7kijn FOREIGN KEY (medication_code) REFERENCES medication (code)
);

INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (100, 1, 0, 200, '1559082b-aac4-41b0-b442-edeb60436c50');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (50, 0, 0, 100, '1d7fd006-37c2-48cf-bdf5-5c639403fe62');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (80, 2, 0, 300, '36f70a64-4666-4fc5-ae63-fe0796cd8b8b');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (100, 0, 0, 100, '36fce6b3-7781-401f-b59a-0532f08d1ade');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (26, 0, 0, 100, '4a400bfe-1e2f-4f28-8fbe-6690b42fe869');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (100, 3, 0, 500, '4b021e54-97b3-4781-8c99-e950b91ec94b');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (100, 1, 0, 200, '4bf1944d-7a01-4c93-a14d-bb6beffe2ecf');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (50, 0, 0, 100, '510ec7fe-8094-4ddf-ad06-838001d1bc20');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (20, 2, 0, 300, '79e4a1c2-8fb6-4899-a10b-9f3c6463b3e3');
INSERT INTO drone (battery_capacity, model, state, weight_limit, serial_number)
VALUES (100, 1, 0, 200, '9ab5c1ec-69d1-4c20-b130-fd93da481860');



insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1A', null, 'Med-1', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1B', null, 'Med-2', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1C', null, 'Med-3', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1D', null, 'Med-4', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1E', null, 'Med-5', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1F', null, 'Med-6', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1J', null, 'Med-7', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1H', null, 'Med-8', null);
insert into medication (weight, code, imageuuid, name, image)
values (20, 'MED_CODE_0FAD1I', null, 'Med-9', null);


