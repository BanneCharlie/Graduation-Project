<template>
  <div v-loading="pieChartLoading" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    }
  },
  data() {
    return {
      /*
        返回 Json格式 
        {
          code: 200 / 404 / 500,
          message: "success",
          data:{
              total: 2000 ,
              pieColumn:[],
              pieData:[]
              
              //total 数据总数一定要和 pieData 数据中 每个数据的和对应
              }
        }

      */
      chart: null,
      
      total: 20000,
        // 饼形图数据
      // 饼形图 列信息
      pieColumn:[
        'A', 'B', 'C', 
        'D', 'E', 'F',
        'G', 'H', 'I',
      ],
      // 饼形图 数值信息
      pieData:[
              {value: 546, name: 'A'},
              {value: 653, name: 'B'},
              {value: 456, name: 'C'},
              {value: 432, name: 'D'},
              {value: 1048, name: 'E'},
              {value: 562, name: 'F'},
              {value: 997, name: 'G'},
              {value: 442, name: 'H'},
              {value: 1234, name: 'I'},
          ],
      pieChartLoading: true,
    }
  },
  methods: {
    loadingPieChartData(){
      /*
        发送 ajax 请求 从后台获取 bar 图表数据
        ....
      */
      setTimeout(() => {
        this.$nextTick(() => {
          this.initChart()
        })
          this.tableLoading = false;
        }, 3000);
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      let total = this.total;
      this.chart.setOption({
        title: {
            zlevel: 0,
            text: [
                '{value|' + total + '}',
                '{name|总办理个数}',
            ].join('\n'),
            rich: {
                value: {
                    color: '#303133',
                    fontSize: 40,
                    fontWeight: 'bold',
                    lineHeight: 40,
                },
                name: {
                    color: '#909399',
                    lineHeight: 20
                },
            },
            top: 'center',
            left: '145',
            textAlign: 'center',
            textStyle: {
                rich: {
                    value: {
                        color: '#303133',
                        fontSize: 40,
                        fontWeight: 'bold',
                        lineHeight: 40,
                    },
                    name: {
                        color: '#909399',
                        fontSize: 20,
                        lineHeight: 20
                    },
                },
            },
        },
        tooltip: {  
            trigger: 'item',
            formatter: "{a}:{b} <br/> 处理个数: {c} ({d}%)",
          },
          legend: {
            orient: 'vertical',
            x: 'right',
            data: this.pieColumn,
            itemGap: 10,
            top: 'middle',
            align: 'left',
            formatter: function (name) {
                return name;
            }
        },
        series: [
            {
                name: '部门名称',
                type: 'pie',
                radius: ['80%', '84%'],
                center: [150, '50%'],
                stillShowZeroSum: false,
                avoidLabelOverlap: false,
                zlevel: 1,
                animationEasing: 'cubicInOut',
                animationDuration: 3600,
                label: {
                    normal: {
                        padding: [30, 30, 30, 30],
                        backgroundColor: '#fff',
                        show: false,
                        position: 'center',
                        formatter: [
                            '{value|{c}}',
                            '{name|{b}}'
                        ].join('\n'),
                        rich: {
                            value: {
                                color: '#303133',
                                fontSize: 40,
                                fontWeight: 'bold',
                                lineHeight: 40,
                            },
                            name: {
                                color: '#909399',
                                fontSize:25,
                                lineHeight: 25
                            },
                        },
                    },
                    emphasis: {
                        show: true,
                        textStyle: {
                            fontSize: '16',
                            fontWeight: 'bold'
                        }
                    }
                },
                data: this.pieData
            }
        ],
      })
    }
  },
  mounted() {

    setTimeout(() => {
      this.$nextTick(() => {
        this.initChart()
        })
      this.pieChartLoading = false;
    }, 3000);
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
}
</script>
