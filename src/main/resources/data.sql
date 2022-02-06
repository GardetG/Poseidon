insert into Users(fullname, username, password, role)
    values('Administrator', 'admin', '$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa', 'ADMIN');
insert into Users(fullname, username, password, role)
    values('User', 'user', '$2a$10$pBV8ILO/s/nao4wVnGLrh.sa/rnr5pDpbeC4E.KNzQWoy8obFZdaa', 'USER');

insert into BidList(BidListId, account, type, bidQuantity, askQuantity, bid, ask)
    values(1, 'Account Test', 'Type Test', 10.0, 0, 0, 0);
insert into BidList(BidListId, account, type, bidQuantity, askQuantity, bid, ask)
    values(2, 'Account to delete Test', 'Type to delete Test', 20.0, 0, 0, 0);

insert into CurvePoint(Id, CurveId, term, value)
    values(1, 10, 10.0, 20.0);
insert into CurvePoint(Id, CurveId, term, value)
    values(2, 11, 15.0, 30.0);