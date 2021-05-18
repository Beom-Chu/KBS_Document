/* Basic */
select 
      smp_cd 
    , smp_nm 
    , high_smp_cd
    , sort_no 
    , level
 from qms.tbl_qm_smp_mgmt
where prdtn_corp_id = 'HA'          
  and fac_id = '2403'
start with nvl(high_smp_cd, '') = '' connect by prior smp_cd = high_smp_cd 
order siblings by sort_no

/*
 * connect by 컬럼이 유니크하지 않을 경우 인라인뷰 서브쿼리로 걸러낸 후 처리
 */
select 
	  smp_cd
    , smp_nm
    , high_smp_cd
    , sort_no
from (select
	      smp_cd
	    , smp_nm
	    , nvl(high_smp_cd,'') as high_smp_cd
	    , sort_no
	 from tbl_qm_smp_mgmt         
	where prdtn_corp_id = 'HA'
	  and fac_id = '2403'
  )
start with high_smp_cd = '' connect by prior smp_cd = high_smp_cd 
order siblings by sort_no