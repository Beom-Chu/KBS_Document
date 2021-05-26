
/* 그루핑  Row 값 합치기 */
with tb as (
	select 1 as no, 'apple' as name, 'red' as color from dual
	union all select 2, 'banana', 'yellow' from dual
	union all select 3, 'orange', 'yellow' from dual
	union all select 4, 'tomato', 'red' from dual
	union all select 5, 'grape', 'purple' from dual
	union all select 6, 'strawberry', 'red' from dual
)
select 
	color
	,string_agg((name) , ',') as same_color_name
	,concat('{',string_agg(to_json(name) || ':' || no , ',') , '}') as same_color_name_no_json
	from tb
group by color

;

/* 그루핑 없이 특정 컬럼 기준으로 Row 값 합치기 */
with tb as (
	select 1 as no, 'apple' as name, 'red' as color from dual
	union all select 2, 'banana', 'yellow' from dual
	union all select 3, 'orange', 'yellow' from dual
	union all select 4, 'tomato', 'red' from dual
	union all select 5, 'grape', 'purple' from dual
	union all select 6, 'strawberry', 'red' from dual
)
select 
	 no
	,name
	,color
	,string_agg((name) , ',') over(partition by color) as same_color_name
	,concat('{',string_agg(to_json(name) || ':' || no , ',') over(partition by color), '}') as same_color_name_no_json
	from tb
order by no

;
