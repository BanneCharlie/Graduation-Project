<template>
  <div v-loading="barChartLoading" :class="className" :style="{height:height,width:width}" />
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
      chart: null,
        // 柱形图数据
      // 柱形图 列信息
      barColumn:['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],      
      // 柱形图 数值信息
      barData: [432, 652, 423, 334, 390, 330, 220, 200, 334, 390, 330, 1000],
      barChartLoading: true,
    }
  },
  methods: {
    loadingBarChartData(){
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

      this.chart.setOption({
        color: ['#18ffff'],
        
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: this.barColumn,
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
          name: '办理数目',
          type: 'bar',
          stack: 'vistors',
          barWidth: '60%',
          data: this.barData,
          animationEasing: 'cubicInOut',
          animationDuration: 3600
        }]
      })
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  mounted(){
    this.loadingBarChartData();
  }
}
</script>
