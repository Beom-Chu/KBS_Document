픽셀별로 구간 나눠서 볼수 있도록.
zoneAxis: 'x'
zones 활용



<script src="https://code.highcharts.com/highcharts.js"></script>
<div id="container" ></div>




var colors = Highcharts.getOptions().colors;
var colIdx = 0;

Highcharts.chart('container', {
		chart : {
    	type : 'area'
    },
    series: [{
        data: [
          ['mstNo1-4',10],
          ['mstNo1-5',10],
          [null],
          [null],
          ['mstNo2-1',10],
          ['mstNo2-2',10],
          ['mstNo2-3',10],
          ['mstNo2-4',10],
          ['mstNo2-5',10],
          [null],
          [null],
          ['mstNo3-1',10],
          ['mstNo3-2',10]
        ],
        marker: { enabled: false },	//그래프 점 제거
        opacity : 0.4,	//불투명도 설정
        /* fillOpacity : 0.4, */	//불투명도 설정
        /* lineColor: 'rgb(255,255,255)', */	//라인 색상 제거
        zoneAxis: 'x',
        zones: [{
            value: 1,
            color: colors[colIdx++%10]
        }, {
            value: 8,
            color: colors[colIdx++%10]
        }, {
            color: colors[colIdx++%10]
        }]
    },
    {
     	data: [null,5, 5, null, 5, 5, null, null, 5, 5, 5, null,5],
      zoneAxis: 'x',
        zones: [{
            value: 2,
            color: colors[colIdx++%10]
        }, {
            value: 5,
            color: colors[colIdx++%10]
        }, {
            value: 10,
            color: colors[colIdx++%10]
        }, {
            color: colors[colIdx++%10]
        }]
      }
    ],
    yAxis : [
    	{
      	title : null,
      	lineWidth: 0,
        offset: 0,			//차트와 y축과의 거리
      },
      {
      	title : null,
      	opposite : true,
        lineWidth: 0
      }
    ]
});
