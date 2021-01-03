CREATE DEFINER=`root`@`%` TRIGGER `Compute_Mandatory_Points` AFTER INSERT ON `answer` FOR EACH ROW BEGIN

    declare _points integer;
    declare _firstTime boolean;
    declare _load integer;

    set _load = 2 - (select isMandatory from question where questionId = new.questionId and productId = new.productId); #first point ever

    if exists(select * from reward as R where R.userId = new.userId and R.productId = new.productId) then
        set _points = _load + (select points from reward as R where R.userId = new.userId and R.productId = new.productId);
        update reward
        set points = _points
        where (userId = new.userId and productId = new.productId);
    else set _points =  _load;
    insert into reward (userId, productId, points) values  (new.userId, new.productId, _points);

    end if;



END