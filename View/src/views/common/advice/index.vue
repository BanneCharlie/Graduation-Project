<template>
  <div style="margin-top: 0 !important;">
    <el-card  class="box-card" shadow="never">
      <div slot="header" class="clearfix item">
        <span><i class="el-icon-upload" style="margin-right: 20px;"><b>办理流程</b></i></span>
      </div>
    <el-table :data="adviceList" border style="width: 100%">
      <el-table-column align="center" prop="currentActName" label="办理阶段名称" width="180">
      </el-table-column>
      <el-table-column align="center" prop="fillUserName" label="办理过程" width="180">
      </el-table-column>
      <el-table-column align="center" prop="creatorUsername" label="办理人" width="180">
      </el-table-column>
      <el-table-column align="center" prop="createTime" label="办理时间" width="180">
      </el-table-column>
      <el-table-column prop="adviceContent" label="办理意见">
      </el-table-column>
    </el-table>
    <el-table :data="defaultList" :cell-style="cellStyle" border style="width: 100%" :show-header="hiddenTableHeader" al>
      <el-table-column align="center" prop="pendingStaff" width="180" label="待处理人员" ></el-table-column>
      <el-table-column prop= "usernames" label="待处理人员名称"></el-table-column>
    </el-table>
    </el-card>
  </div>
</template>

<script>
  import {
    getAdvice
  } from '@/api/dispatch'

  export default {
    name: "advicePage",
    data() {
      return {
        adviceList: [],
        usernames: '',
        defaultList: [
          {
            pendingStaff: '待处理人员',
            usernames: ''
          }
        ],
        hiddenTableHeader: false //隐藏表头
      }
    },
    methods: {
      getAdvice() {
        getAdvice(this.piid).then(response => {
          if (response.code === 200) {
            this.adviceList = response.data.adviceStrategyList;
            this.defaultList[0].usernames = response.data.usernames
          }
        })
      },
      cellStyle(row,column,rowIndex,columnIndex) {
        if(row.rowIndex == this.defaultList.length - 1) {
          return 'color : #fd6b0a'
        }
      }
    },
    props: ['piid'],
    mounted: function() {
      this.getAdvice()
    },
  }
</script>
