# MY SQL CODE FOR TRIGGER 1 #
create trigger "Compute_Mandatory_Points" after insert on answer
    for each row
    begin
        declare _points integer;

        if (exists(select * from reward as R where R.userId = new.userId and R.productId = new.productId))
            then set _points = 1 + (select points from reward as R where R.userId = new.userId and R.productId = new.productId);
            else set _points = 1;

        end if;

        update reward
        set points = _points
        where (userId = new.userId and productId = new.productId);

    end;
