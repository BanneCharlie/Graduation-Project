<template>
  <div v-loading="pieChartLoading" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getPieChartData } from '@/api/chart'

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
      chart: null,

      total: 270,
        // 饼形图数据
      // 饼形图 列信息
      pieColumn:["电梯", "起重机械", "场内专用车辆", "质检", "科研", "其他"],
      // 饼形图 数值信息
      pieData:[
              {value: 50, name: '电梯'},
              {value: 65, name: '起重机械'},
              {value: 45, name: '场内专用车辆'},
              {value: 44, name: '质检'},
              {value: 10, name: '科研'},
              {value: 56, name: '其他'},
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
      this.getLoadingBarChartData();

      setTimeout(() => {
        this.$nextTick(() => {
          this.initChart()
        })
          this.tableLoading = false;
        }, 1000);
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      let total = this.total;
      this.chart.setOption({
        title: {
            zlevel: 0,
            text: [
                '{value|' + total + '}',
                '{name|总检验个数}',
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
                name: '设备类型',
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
    },

    getLoadingBarChartData() {
      getPieChartData().then(response => {
        this.total = response.data.total;
        this.pieData = response.data.pieData;
        this.pieColumn = response.data.pieColumn;
      })
    }
  },
  mounted() {

    this.loadingPieChartData();

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
