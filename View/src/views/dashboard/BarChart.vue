<template>
  <div v-loading="barChartLoading" :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import axios from 'axios'
import {getBarChartData} from '../../../src/api/chart'

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

      this.chart.setOption({
        color: ['#2b4b6b'],
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
    },

   getLoadingBarChartData() {
      getBarChartData().then(res => {
        this.barData = res.data;
        // 在原来的基础上扩大 7 倍
        for (let i = 0; i < this.barData.length; i++) {
          this.barData[i] = this.barData[i] * 7;
        }
        this.barChartLoading = false;
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
