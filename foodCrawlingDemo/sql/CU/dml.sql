-- 총 레코드 수
select count(*) from cu_tbl;

-- 마지막 레코드 내용 확인
select * 
from (
    select m.*, rownum as num
    from (
        select * from cu_tbl     
    ) m
)    
where num = 6161;
