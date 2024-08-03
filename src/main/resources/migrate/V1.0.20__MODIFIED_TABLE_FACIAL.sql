
alter table users
    drop COLUMN feature_face;

alter table facial
    add feature_face mediumblob null;